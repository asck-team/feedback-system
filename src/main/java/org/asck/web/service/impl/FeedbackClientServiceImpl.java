package org.asck.web.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.web.exceptions.ClientServiceRuntimeException;
import org.asck.web.service.IFeedbackClientService;
import org.asck.web.service.model.Answer;
import org.asck.web.service.model.AnswerReport;
import org.asck.web.service.model.Event;
import org.asck.web.service.model.User;
import org.asck.web.service.model.Option;
import org.asck.web.service.model.Question;
import org.asck.web.service.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AccessLevel;
import lombok.Getter;

@Service
@Getter(AccessLevel.PROTECTED)
class FeedbackClientServiceImpl implements IFeedbackClientService {

	private static final String PATH_ELEMENT_ADMIN = "admin";

	private static final String PATH_ELEMENT_QUESTION_TYPES = "questionTypes";

	private static final String PATH_ELEMENT_OPTIONS = "options";

	private static final String PATH_ELEMENT_QUESTIONS = "questions";

	private static final String PATH_ELEMENT_EVENTS = "events";

	private static final String PATH_ELEMENT_ANSWERS = "answers";
	
	private static final String PATH_ELEMENT_USER = "user";

	private static final Logger LOGGER = LogManager.getLogger(FeedbackClientServiceImpl.class);

	@Value("${service.base.path:http://localhost:8080/v1/feedback}")
	private String basePath;

	private final RestTemplate restTemplate;

	public FeedbackClientServiceImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	protected String createUrlPath(String... pathElements) {
		List<String> path = new ArrayList<>();
		path.add(getBasePath());
		for (String eachPathElement : pathElements) {
			path.add(eachPathElement);
		}
		return path.stream().collect(Collectors.joining("/"));
	}

	@Override
	public List<Event> leseAlleEvents() {
		try {
			ResponseEntity<List<Event>> responseEntity = getRestTemplate().exchange(createUrlPath(PATH_ELEMENT_EVENTS),
					HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {
					});
			if (responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
				return Collections.emptyList();
			} else {
				return responseEntity.getBody();
			}
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new ClientServiceRuntimeException("Error on retrieve events", e);
		}
	}

	@Override
	public List<Question> leseAlleFragenZuEvent(Long eventId) {
		try {
			ResponseEntity<List<Question>> response = getRestTemplate().exchange(
					createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS), HttpMethod.GET,
					null, new ParameterizedTypeReference<List<Question>>() {
					});
			if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
				return Collections.emptyList();
			} else {
				return response.getBody();
			}
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new ClientServiceRuntimeException("Error on retrieve questions for event with id " + eventId, e);
		}

	}

	@Override
	public List<Option> leseAlleOptionenZuEinerFrage(Long eventId, Long questionId) {
		ResponseEntity<List<Option>> response = getRestTemplate()
				.exchange(
						createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS,
								questionId.toString(), PATH_ELEMENT_OPTIONS),
						HttpMethod.GET, null, new ParameterizedTypeReference<List<Option>>() {
						});
		return response.getBody();
	}

	@Override
	public Event saveEvent(Event event) {
		Event newOrUpdatedEvent = event;
		if (event.getId() != null) {
			getRestTemplate().put(createUrlPath(PATH_ELEMENT_EVENTS, event.getId().toString()), event);
			LOGGER.info("updated Event: {}", newOrUpdatedEvent);
		} else {
			URI location = getRestTemplate().postForLocation(createUrlPath(PATH_ELEMENT_EVENTS),
					Event.builder().id(-1L).name(event.getName()).build());
			newOrUpdatedEvent = getRestTemplate().getForObject(location, Event.class);
			LOGGER.info("created Event: {}", newOrUpdatedEvent);
		}
		return newOrUpdatedEvent;
	}

	@Override
	public Event getEventById(Long id) {
		try {
			ResponseEntity<Event> response = getRestTemplate().exchange(
					createUrlPath(PATH_ELEMENT_EVENTS, id.toString()), HttpMethod.GET, null,
					new ParameterizedTypeReference<Event>() {
					});
			return response.getBody();
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return null;
			}
			throw new ClientServiceRuntimeException("Error on retrieve event with id " + id);
		}
	}

	@Override
	public Question readQuestion(Long eventId, Long questionId) {
		ResponseEntity<Question> response = getRestTemplate().exchange(
				createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS, questionId.toString()),
				HttpMethod.GET, null, new ParameterizedTypeReference<Question>() {
				});
		return response.getBody();
	}

	@Override
	public Question saveQuestion(Long eventId, Question question) {
		Question newOrUpdatedQuestion = question;
		if (question.getId() != null) {
			getRestTemplate().put(createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS,
					question.getId().toString()), question);
			LOGGER.info("updated Question: {}", newOrUpdatedQuestion);
		} else {
			URI uri4CreatedEvent = getRestTemplate().postForLocation(
					createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS), question);
			newOrUpdatedQuestion = getRestTemplate().getForObject(uri4CreatedEvent, Question.class);
			LOGGER.info("created Question: {}", newOrUpdatedQuestion);
		}
		return newOrUpdatedQuestion;
	}

	@Override
	public List<String> readAllSupportedQuestionTypes() {
		List<String> allSupportedQuestionTypes = new ArrayList<>();
		LOGGER.traceEntry();
		ResponseEntity<List<String>> response = getRestTemplate().exchange(
				createUrlPath(PATH_ELEMENT_ADMIN, PATH_ELEMENT_QUESTION_TYPES), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		allSupportedQuestionTypes.addAll(response.getBody());
		return LOGGER.traceExit(allSupportedQuestionTypes);
	}

	@Override
	public void deleteEvent(Long eventId) {
		LOGGER.traceEntry("with Parameters {}", eventId);
		getRestTemplate().delete(createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString()));
		LOGGER.traceExit();
	}

	@Override
	public void deleteQuestion(Long eventId, Long questionId) {
		LOGGER.traceEntry("with Parameters {} and {}", eventId, questionId);
		getRestTemplate().delete(
				createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS, questionId.toString()));
		LOGGER.traceExit();
	}

	@Override
	public Answer saveAnswer(Answer answer) {
		URI uri4CreatedAnswer = getRestTemplate().postForLocation(createUrlPath(PATH_ELEMENT_ANSWERS), answer);
		LOGGER.info("created Answer: {}", uri4CreatedAnswer);
		return answer;
	}

	protected List<Answer> getAllAnswersToQuestion(Long questionId) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(createUrlPath(PATH_ELEMENT_ANSWERS))
				.queryParam("questionId", questionId);
		ResponseEntity<List<Answer>> response = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Answer>>() {
				});
		return response.getBody();
	}

	@Override
	public Option findOptionById(Long optionId) {
		ResponseEntity<Option> response = getRestTemplate().exchange(
				createUrlPath(PATH_ELEMENT_OPTIONS, optionId.toString()), HttpMethod.GET, null,
				new ParameterizedTypeReference<Option>() {
				});
		return response.getBody();
	}

	@Override
	public List<AnswerReport> getAllAnswersToEventId(Long eventId) {
		List<AnswerReport> answersReport = new ArrayList<>();

		List<Question> allQuestionsToEvent = leseAlleFragenZuEvent(eventId);

		for (Question question : allQuestionsToEvent) {
			List<Answer> allAnswersToQuestion = getAllAnswersToQuestion(question.getId());
			for (Answer answer : allAnswersToQuestion) {
				AnswerReport answerReport = new AnswerReport();
				answerReport.setQuestion(question);
				answerReport.setOption(findOptionById(answer.getOptionId()));
				answerReport.setRemark(answer.getRemark());
				answerReport.setAnsweredAt(answer.getAnsweredAt());
				answersReport.add(answerReport);
			}
		}
		return answersReport;
	}

	@Override
	public User getUserByEmail(String email) {
		try {
			ResponseEntity<User> response = getRestTemplate().exchange(
					createUrlPath(PATH_ELEMENT_USER, email), HttpMethod.GET, null,
					new ParameterizedTypeReference<User>() {
					});
			return response.getBody();
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return null;
			}
			throw new ClientServiceRuntimeException("Error on retrieve User with email " + email);
		}
	}

	@Override
	public User saveUser(User user) {
		User newUser = null;
			URI location = getRestTemplate().postForLocation(createUrlPath(PATH_ELEMENT_EVENTS),
					User.builder().id(-1L).email(user.getEmail()).password(user.getPassword()).build());
			newUser = getRestTemplate().getForObject(location, User.class);
			LOGGER.info("created User: {}", newUser);
		
		return newUser;
	}

}
