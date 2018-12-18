package com.feedback.service.client.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.feedback.service.client.IFeedbackClientService;
import com.feedback.service.client.model.Answer;
import com.feedback.service.client.model.Event;
import com.feedback.service.client.model.Option;
import com.feedback.service.client.model.Question;

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
}
