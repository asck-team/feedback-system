package org.asck.web.service.impl;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.web.service.IFeedbackClientService;
import org.asck.web.service.model.Answer;
import org.asck.web.service.model.AnswerReport;
import org.asck.web.service.model.Event;
import org.asck.web.service.model.Option;
import org.asck.web.service.model.Question;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class FeedbackClientService implements IFeedbackClientService {

	private static final String PATH_ELEMENT_ADMIN = "admin";

	private static final String PATH_ELEMENT_QUESTION_TYPES = "questionTypes";

	private static final String PATH_ELEMENT_OPTIONS = "options";

	private static final String PATH_ELEMENT_QUESTIONS = "questions";

	private static final String PATH_ELEMENT_EVENTS = "events";
	
	private static final String PATH_ELEMENT_ANSWERS = "answers";

	private static final Logger LOGGER = LogManager.getLogger(FeedbackClientService.class);

	private String basePath;

	public FeedbackClientService(String basePath) {
		this.basePath = basePath;
	}
	

	protected String createUrlPath(String... pathElements) {
		List<String> path = new ArrayList<>();
		path.add(basePath);
		for (String eachPathElement : pathElements) {
			path.add(eachPathElement);
		}
		return path.stream().collect(Collectors.joining("/"));
	}
	
	@Override
	public List<Event> leseAlleEvents() {
		ResponseEntity<List<Event>> responseEntity = new RestTemplate().exchange(createUrlPath(PATH_ELEMENT_EVENTS),
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {
				});
		return responseEntity.getBody();
	}

	@Override
	public List<Question> leseAlleFragenZuEvent(Long eventId) {
		ResponseEntity<List<Question>> response = new RestTemplate().exchange(createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Question>>() {
				});
		return response.getBody();
	}

	@Override
	public List<Option> leseAlleOptionenZuEinerFrage(Long eventId, Long questionId) {
		ResponseEntity<List<Option>> response = new RestTemplate().exchange(createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS, questionId.toString(), PATH_ELEMENT_OPTIONS),
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Option>>() {
				});
		return response.getBody();
	}

	@Override
	public Event saveEvent(Event event) {
		if (event.getId() != null) {
			new RestTemplate().put(createUrlPath(PATH_ELEMENT_EVENTS, event.getId().toString()), event);
			LOGGER.info("updated Event: {}", event);
		} else {
			URI uri4CreatedEvent = new RestTemplate().postForLocation(createUrlPath(PATH_ELEMENT_EVENTS), event);
			LOGGER.info("created Event: {}", uri4CreatedEvent);
		}
		return event;
	}

	@Override
	public Event getEventById(Long id) {
		ResponseEntity<Event> response = new RestTemplate().exchange(createUrlPath(PATH_ELEMENT_EVENTS, id.toString()), HttpMethod.GET,
				null, new ParameterizedTypeReference<Event>() {
				});
		return response.getBody();
	}

	@Override
	public Question readQuestion(Long eventId, Long questionId) {
		ResponseEntity<Question> response = new RestTemplate().exchange(
				createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS, questionId.toString()), HttpMethod.GET, null,
				new ParameterizedTypeReference<Question>() {
				});
		return response.getBody();
	}

	@Override
	public Question saveQuestion(Long eventId, Question question) {
		if (question.getId() != null) {
			new RestTemplate().put(createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS, question.getId().toString()), question);
			LOGGER.info("updated Question: {}", question);
		} else {
			URI uri4CreatedEvent = new RestTemplate()
					.postForLocation(createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS), question);
			LOGGER.info("created Question: {}", uri4CreatedEvent);
		}
		return question;
	}

	@Override
	public List<String> readAllSupportedQuestionTypes() {
		List<String> allSupportedQuestionTypes = new ArrayList<>();
		LOGGER.traceEntry();
		ResponseEntity<List<String>> response = new RestTemplate().exchange(createUrlPath(PATH_ELEMENT_ADMIN,PATH_ELEMENT_QUESTION_TYPES),
				HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
				});
		allSupportedQuestionTypes.addAll(response.getBody());
		return LOGGER.traceExit(allSupportedQuestionTypes);
	}

	@Override
	public void deleteEvent(Long eventId) {
		LOGGER.traceEntry("with Parameters {}", eventId);
		new RestTemplate().delete(createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString()));
		LOGGER.traceExit();
	}

	@Override
	public void deleteQuestion(Long eventId, Long questionId) {
		LOGGER.traceEntry("with Parameters {} and {}", eventId, questionId);
		new RestTemplate().delete(createUrlPath(PATH_ELEMENT_EVENTS, eventId.toString(), PATH_ELEMENT_QUESTIONS, questionId.toString()));
		LOGGER.traceExit();
	}


	@Override
	public Answer saveAnswer(Answer answer) {
		URI uri4CreatedAnswer = new RestTemplate().postForLocation(createUrlPath(PATH_ELEMENT_ANSWERS), answer);
		LOGGER.info("created Answer: {}", uri4CreatedAnswer);
		return answer;
	}
	
	protected List<Answer> getAllAnswersToQuestion(Long questionId) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(createUrlPath(PATH_ELEMENT_ANSWERS)).queryParam("questionId", questionId);
		ResponseEntity<List<Answer>> response = new RestTemplate().exchange(builder.toUriString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Answer>>() {
				});
		return response.getBody();
	}
	
	private Option getOptionById(Long optionId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<AnswerReport> getAllAnswersToEventId(Long eventId) {
		List<AnswerReport> answersReport = new ArrayList<>();
		
//		List<Question> allQuestionsToEvent = leseAlleFragenZuEvent(eventId);
//		
//		for (Question question : allQuestionsToEvent) {
//			List<Answer> allAnswersToQuestion = getAllAnswersToQuestion(question.getId());
//			for (Answer answer : allAnswersToQuestion) {
//				AnswerReport answerReport = new AnswerReport();
//				answerReport.setQuestion(question);
//				answerReport.setOption(getOptionById(answer.getOptionId()));
//				answerReport.setRemark(answer.getRemark());
//				answerReport.setAnsweredAt(answer.getAnsweredAt());
//				answersReport.add(answerReport);
//			}
//		}
		
		AnswerReport answerReport = new AnswerReport();
		answerReport.setQuestion(new Question(1L, "questionName", "questionType", 1));
		answerReport.setOption(new Option(1L, "optionalDescription", "iconPath"));
		answerReport.setRemark("remark");
		answerReport.setAnsweredAt(LocalDateTime.now());
		answersReport.add(answerReport);
		
		return answersReport;
	}


	


	
}
