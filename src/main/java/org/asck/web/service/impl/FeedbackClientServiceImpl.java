package org.asck.web.service.impl;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.api.exceptions.EntityNotFoundException;
import org.asck.api.service.IFeedbackService;
import org.asck.api.service.model.QuestionType;
import org.asck.web.exceptions.ClientServiceRuntimeException;
import org.asck.web.service.IFeedbackClientService;
import org.asck.web.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter(AccessLevel.PROTECTED)
class FeedbackClientServiceImpl implements IFeedbackClientService {

	private static final Logger LOGGER = LogManager.getLogger(FeedbackClientServiceImpl.class);


	@Autowired
	IFeedbackService feedbackService;

	public FeedbackClientServiceImpl (IFeedbackService feedbackService){
		this.feedbackService = feedbackService;
	}

	@Override
	public List<Event> leseAlleEvents(Long ownedById) {
		List<org.asck.api.service.model.Event> eventsOwnedBy = new ArrayList<org.asck.api.service.model.Event>();
		eventsOwnedBy = getFeedbackService().findEventsOwnedBy(ownedById);
		if (eventsOwnedBy == null || eventsOwnedBy.isEmpty()) {
			return new ArrayList<Event>();
		}
		return eventsOwnedBy.stream().map(this::map).collect(Collectors.toList());
	}

	@Override
	public List<Question> leseAlleFragenZuEvent(Long eventId) {
		List<Question> questions = new ArrayList<>();
		org.asck.api.service.model.Event event = null;
		try {
			event = getFeedbackService().findEventById(eventId);
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on retrieve questions for event with id " + eventId);
		}
		if (event != null && !event.getQuestions().isEmpty()) {
			questions = event.getQuestions().stream().map(this::map).collect(Collectors.toList());
		}
		return questions;

	}

	@Override
	public List<Option> leseAlleOptionenZuEinerFrage(Long eventId, Long questionId) {
		org.asck.api.service.model.Question question = null;
		try {
			question = getFeedbackService().findQuestion(eventId, questionId);
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on retrieve question for event with id: " + eventId + "and question with id: " + questionId, e);
		}
		return question == null ? null : question.getOptions().stream().map(this::map).collect(Collectors.toList());
	}

	@Override
	public Event saveEvent(Event event) {
		Event newOrUpdatedEvent = event;
		Long newEventId = getFeedbackService().saveEvent(map(newOrUpdatedEvent));
		if (event.getId() != null) {
			LOGGER.info("updated Event: {}", newOrUpdatedEvent);
		} else {
			newOrUpdatedEvent.setId(newEventId);
			LOGGER.info("created Event: {}", newOrUpdatedEvent);
		}
		return newOrUpdatedEvent;
	}

	@Override
	public Event getEventById(Long id) {
		try {

			org.asck.api.service.model.Event eventById = getFeedbackService().findEventById(id);
			return eventById == null ? null : map(eventById);
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on retrieve event with id " + id);
		}
	}

	@Override
	public Question readQuestion(Long eventId, Long questionId) {
		try {
			org.asck.api.service.model.Question question = getFeedbackService().findQuestion(eventId, questionId);
			return question == null ? null : map(question);
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on retrieve question with eventId " + eventId + " and question id " + questionId);
		}
	}

	@Override
	public Question saveQuestion(Long eventId, Question question) {
		Question newOrUpdatedQuestion = question;
		Long newQuestionId;
		try {
			newQuestionId = getFeedbackService().saveQuestion(eventId, map(question));
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on retrieve question with eventId " + eventId + " and question " + question.toString());
		}

		if (question.getId() != null) {
			LOGGER.info("updated Question: {}", newOrUpdatedQuestion);
		} else {
			newOrUpdatedQuestion.setId(newQuestionId);
			LOGGER.info("created Question: {}", newOrUpdatedQuestion);
		}
		return newOrUpdatedQuestion;
	}

	@Override
	public List<String> readAllSupportedQuestionTypes() {
		List<String> allSupportedQuestionTypes = new ArrayList<>();
		LOGGER.traceEntry();
		allSupportedQuestionTypes.addAll(Arrays.asList(QuestionType.values()).stream().map(QuestionType::name).collect(Collectors.toList()));
		return LOGGER.traceExit(allSupportedQuestionTypes);
	}

	@Override
	public void deleteEvent(Long eventId) {
		LOGGER.traceEntry("with Parameters {}", eventId);

		try {
			getFeedbackService().deleteEvent(eventId);
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on delete event with eventId " + eventId);
		}

		LOGGER.traceExit();
	}

	@Override
	public void deleteQuestion(Long eventId, Long questionId) {
		LOGGER.traceEntry("with Parameters {} and {}", eventId, questionId);

		try {
			getFeedbackService().deleteQuestion(eventId,questionId);
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on delete question with eventId " + eventId + " and questionId " + questionId);
		}

		LOGGER.traceExit();
	}

	@Override
	public Answer saveAnswer(Answer answer) {
		Long answerId;
		try {
			 answerId = getFeedbackService().saveAnswer(map(answer));
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on save answer for question with id " + answer.getQuestionId());
		}
		LOGGER.info("created Answer with id: {}", answerId);
		return answer;
	}

	protected List<Answer> getAllAnswersToQuestion(Long questionId) {
		List<Answer> allAnswersToQuestion = new ArrayList<>();
		try {
			List<org.asck.api.service.model.Answer> allAnswersResult = getFeedbackService().getAllAnswersToQuestion(questionId);
			allAnswersToQuestion = allAnswersResult.stream().map(this::map).collect(Collectors.toList());
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on get answers for question with id " + questionId);
		}

		return allAnswersToQuestion;
	}

	@Override
	public Option findOptionById(Long optionId) {
		try {
			org.asck.api.service.model.Option option = getFeedbackService().getOptionById(optionId);
			return option == null ? null : map(option);
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on get option with id " + optionId);
		}
	}

	@Override
	public List<AnswerReport> getAllAnswersToEventId(Long eventId) {
		List<AnswerReport> answersReport = new ArrayList<>();

		List<Question> allQuestionsToEvent = leseAlleFragenZuEvent(eventId);

		for (Question question : allQuestionsToEvent) {
			List<Answer> allAnswersToQuestion = getAllAnswersToQuestion(question.getId());
			if (allAnswersToQuestion != null) {
				for (Answer answer : allAnswersToQuestion) {
					AnswerReport answerReport = new AnswerReport();
					answerReport.setQuestion(question);
					answerReport.setOption(findOptionById(answer.getOptionId()));
					answerReport.setRemark(answer.getRemark());
					answerReport.setAnsweredAt(answer.getAnsweredAt());
					answersReport.add(answerReport);
				}
			}
		}
		return answersReport;
	}

	@Override
	public User getUserByEmail(String email) {
		try {
			return map(getFeedbackService().getUserByEmail(email));
		} catch (EntityNotFoundException e) {
			throw new ClientServiceRuntimeException("Error on retrieve User with email " + email);
		}
	}

	@Override
	public User saveUser(User user) {
		getFeedbackService().saveUser(map(user));
		LOGGER.info("created UserName: {}", user.getEmail());
		return user;
	}

	protected Event map(org.asck.api.service.model.Event event){
		return Event.builder().id(event.getId()).name(event.getName()).ownedBy(event.getOwnedBy()).build();
	}

	protected org.asck.api.service.model.Event map(Event event){
		return org.asck.api.service.model.Event.builder().id(event.getId()).name(event.getName()).ownedBy(event.getOwnedBy()).build();
	}

	protected User map(org.asck.api.service.model.User user) {
		return User.builder().id(user.getId()).email(user.getEmail()).password(user.getPassword()).build();
	}

	protected org.asck.api.service.model.User map(User user) {
		return org.asck.api.service.model.User.builder().id(user.getId()).email(user.getEmail()).password(user.getPassword()).build();
	}

	protected org.asck.api.service.model.Answer map(Answer answer) {
		return org.asck.api.service.model.Answer.builder().optionId(answer.getOptionId()).questionId(answer.getQuestionId()).answeredAt(answer.getAnsweredAt()).remark(answer.getRemark()).build();
	}

	protected Answer map(org.asck.api.service.model.Answer answer) {
		return Answer.builder().optionId(answer.getOptionId()).questionId(answer.getQuestionId()).answeredAt(LocalDateTime.now()).remark(answer.getRemark()).build();
	}

	protected Option map(org.asck.api.service.model.Option option){
		return Option.builder().id(option.getId()).iconPath(option.getIconPath()).optionalDescription(option.getOptionalDescription()).build();
	}

	protected Question map(org.asck.api.service.model.Question question) {
		return Question.builder().id(question.getId()).order(question.getOrder()).questionName(question.getQuestionName()).questionType(question.getQuestionType().name()).answerRequired(question.isAnswerRequired()).build();
	}

	protected org.asck.api.service.model.Question map(Question question) {
		return org.asck.api.service.model.Question.builder().id(question.getId()).order(question.getOrder()).questionName(question.getQuestionName()).questionType(QuestionType.valueOf(question.getQuestionType())).answerRequired(question.isAnswerRequired()).build();
	}
}
