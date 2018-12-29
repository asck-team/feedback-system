package org.asck.service.impl;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.exceptions.EntityNotFoundException;
import org.asck.repository.AnswerRepository;
import org.asck.repository.EventRepository;
import org.asck.repository.QuestionOptionRepository;
import org.asck.repository.QuestionRepository;
import org.asck.repository.QuestionTypeRepository;
import org.asck.repository.model.AnswerTableModel;
import org.asck.repository.model.EventTableModel;
import org.asck.repository.model.QuestionOptionTableModel;
import org.asck.repository.model.QuestionTableModel;
import org.asck.service.IFeedbackService;
import org.asck.service.model.Answer;
import org.asck.service.model.Event;
import org.asck.service.model.Option;
import org.asck.service.model.Question;
import org.asck.service.model.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.Getter;

@Transactional
@Service
@Getter(value = AccessLevel.PROTECTED)
class FeedbackServiceImpl implements IFeedbackService {

	private static final Logger LOGGER = LogManager.getLogger(FeedbackServiceImpl.class);

	@Autowired
	EventRepository eventRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	QuestionTypeRepository questionTypeRepository;

	@Autowired
	QuestionOptionRepository questionOptionRepository;

	@Autowired
	AnswerRepository answerRepository;

	@Override
	public List<Event> findEvents() {
		return getEventRepository().findAll().stream().map(this::loadEvent).collect(Collectors.toList());
	}

	protected Event loadEvent(EventTableModel event) {
		List<QuestionTableModel> questions = getQuestionRepository().findAllByEventIdOrderByOrder(event.getId());
		return Event.builder().id(event.getId()).name(event.getName())
				.questions(questions.stream().map(this::map).collect(Collectors.toList())).build();
	}

	protected Question map(QuestionTableModel question) {
		List<QuestionOptionTableModel> questionOptions = getQuestionOptionRepository()
				.findAllByQuestionTypeId(question.getQuestionTypeId());
		return Question.builder().id(question.getId()).questionName(question.getQuestionTitle())
				.questionType(QuestionType.getForDBId(question.getQuestionTypeId())).order(question.getOrder())
				.options(questionOptions.stream().map(this::map).collect(Collectors.toList())).build();
	}

	protected Option map(QuestionOptionTableModel option) {
		return Option.builder().id(option.getId()).iconPath(option.getIconPath())
				.optionalDescription(option.getOptionalDescription()).build();
	}

	@Override
	public Event findEventById(Long id) throws EntityNotFoundException {
		Optional<EventTableModel> event = getEventRepository().findById(id);
		if (event.isPresent()) {
			return loadEvent(event.get());
		} else {
			throw new EntityNotFoundException(Event.class, "id", id.toString());
		}
	}

	@Override
	public Question findQuestion(Long eventId, Long questionId) throws EntityNotFoundException {
		Event event = findEventById(eventId);
		Optional<Question> question = event.getQuestions().stream().filter(q -> q.getId() == questionId).findFirst();
		if (question.isPresent()) {
			return question.get();
		} else {
			throw new EntityNotFoundException(Question.class, "id", questionId.toString());
		}
	}

	@Override
	public Long saveAnswer(Answer answer) throws EntityNotFoundException {
		AnswerTableModel savedAnswer = getAnswerRepository().save(AnswerTableModel.builder()
				.questionId(answer.getQuestionId()).questionOptionId(answer.getOptionId()).remark(answer.getRemark())
				.answeredAt(Date.from(answer.getAnsweredAt().atZone(ZoneId.systemDefault()).toInstant())).build());
		return savedAnswer.getId();
	}

	@Override
	public Long saveEvent(@Valid Event event) {
		return getEventRepository().save(EventTableModel.builder().id(event.getId()).name(event.getName()).build())
				.getId();
	}

	protected void shiftOrderDown(QuestionTableModel question) {
		question.setOrder(question.getOrder() - 1);
	}
	
	protected void shiftOrderUp(QuestionTableModel question) {
		question.setOrder(question.getOrder() + 1);
	}

	@Override
	public Long saveQuestion(@NotNull Long eventId, @Valid Question question) throws EntityNotFoundException {
		LOGGER.traceEntry("with parameters {} and {}", eventId, question);
		Long id = question.getId();
		List<QuestionTableModel> allQuestion = getQuestionRepository().findAllByEventIdOrderByOrder(eventId);
		if (question.isIdSpecified()) {
			Optional<QuestionTableModel> existQuestionWithIdOnDatabase = allQuestion.stream()
					.filter(q -> q.getId() == question.getId()).findAny();
			if (existQuestionWithIdOnDatabase.isPresent()) {
				if (question.getOrder() > existQuestionWithIdOnDatabase.get().getOrder()) {
					allQuestion.stream().filter(q -> q.getOrder() <= question.getOrder()).forEach(this::shiftOrderDown);
					allQuestion.stream().filter(q -> q.getOrder() > question.getOrder()).forEach(this::shiftOrderUp);
				} else {
					allQuestion.stream().filter(q -> q.getOrder() > question.getOrder()).forEach(this::shiftOrderDown);
					allQuestion.stream().filter(q -> q.getOrder() <= question.getOrder()).forEach(this::shiftOrderUp);
				}
				QuestionTableModel question2Insert = existQuestionWithIdOnDatabase.get();
				question2Insert.setOrder(question.getOrder());
				question2Insert.setQuestionTitle(question.getQuestionName());
				question2Insert.setQuestionTypeId(question.getQuestionType().getDbId());
				allQuestion.sort((a,b) -> Integer.compare(a.getOrder(), b.getOrder()));
				for (int i = 0; i < allQuestion.size(); i++) {
					allQuestion.get(i).setOrder(i + 1);
				}
				
				allQuestion.forEach(this::updateQuestionOnDatabase);
			} else {
				throw new EntityNotFoundException(QuestionTableModel.class, "id", question.getId().toString());
			}
		} else {
			int order = allQuestion.size() + 1;
			QuestionTableModel question2Insert = QuestionTableModel.builder().id(-1L).eventId(eventId)
					.questionTitle(question.getQuestionName()).questionTypeId(question.getQuestionType().getDbId())
					.order(order).build();
			id = getQuestionRepository().save(question2Insert).getId();
		}
		return LOGGER.traceExit(id);
	}

	protected void updateQuestionOnDatabase(QuestionTableModel q) {
		getQuestionRepository().save(q);
	}

	@Override
	public void deleteEvent(@NotNull Long eventId) throws EntityNotFoundException {
		if (getEventRepository().existsById(eventId)) {
			List<QuestionTableModel> allQuestions = getQuestionRepository().findAllByEventIdOrderByOrder(eventId);
			if (!allQuestions.isEmpty()) {
				for (QuestionTableModel eachQuestion : allQuestions) {
					List<AnswerTableModel> allAnswers = getAnswerRepository().findAllByQuestionId(eachQuestion.getId());
					getAnswerRepository().deleteAll(allAnswers);
				}
				getQuestionRepository().deleteAll(allQuestions);
			}
			getEventRepository().deleteById(eventId);
		} else {
			throw new EntityNotFoundException(Event.class, "id", eventId.toString());
		}
	}

	@Override
	public void deleteQuestion(@NotNull Long eventId, @NotNull Long questionId) throws EntityNotFoundException {
		if (getEventRepository().existsById(eventId)) {
			if (getQuestionRepository().existsById(questionId)) {
				List<AnswerTableModel> allAnswersToQuestion = getAnswerRepository().findAllByQuestionId(questionId);
				getAnswerRepository().deleteAll(allAnswersToQuestion);
				getQuestionRepository().deleteById(questionId);
			} else {
				throw new EntityNotFoundException(Question.class, "id", questionId.toString());
			}
		} else {
			throw new EntityNotFoundException(Event.class, "id", eventId.toString());
		}
	}

}
