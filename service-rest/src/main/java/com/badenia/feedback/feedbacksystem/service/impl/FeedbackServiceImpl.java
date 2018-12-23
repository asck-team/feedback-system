package com.badenia.feedback.feedbacksystem.service.impl;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badenia.feedback.feedbacksystem.exceptions.EntityNotFoundException;
import com.badenia.feedback.feedbacksystem.service.IFeedbackService;
import com.badenia.feedback.feedbacksystem.service.model.Answer;
import com.badenia.feedback.feedbacksystem.service.model.Event;
import com.badenia.feedback.feedbacksystem.service.model.Option;
import com.badenia.feedback.feedbacksystem.service.model.Question;
import com.badenia.feedback.feedbacksystem.service.model.QuestionType;
import com.badenia.feedback.feedbacksystem.service.repository.AnswerRepository;
import com.badenia.feedback.feedbacksystem.service.repository.EventRepository;
import com.badenia.feedback.feedbacksystem.service.repository.QuestionOptionRepository;
import com.badenia.feedback.feedbacksystem.service.repository.QuestionRepository;
import com.badenia.feedback.feedbacksystem.service.repository.QuestionTypeRepository;
import com.badenia.feedback.feedbacksystem.service.repository.model.AnswerTableModel;
import com.badenia.feedback.feedbacksystem.service.repository.model.EventTableModel;
import com.badenia.feedback.feedbacksystem.service.repository.model.QuestionOptionTableModel;
import com.badenia.feedback.feedbacksystem.service.repository.model.QuestionTableModel;

import lombok.AccessLevel;
import lombok.Getter;

@Transactional
@Service
@Getter(value = AccessLevel.PROTECTED)
class FeedbackServiceImpl implements IFeedbackService {

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
		List<QuestionTableModel> questions = getQuestionRepository().findAllByEventId(event.getId());
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

	@Override
	public Long save(@NotNull Long eventId, @Valid Question question) {
		// lese aller Fragen
		List<QuestionTableModel> allQuestion = getQuestionRepository().findAllByEventId(eventId);
		Optional<Integer> max = allQuestion.stream().map(QuestionTableModel::getOrder)
				.max(Comparator.comparingInt(Integer::valueOf));
		int order = 1;
		if (max.isPresent()) {
			order = max.get() + 1;
		}
		return getQuestionRepository().save(QuestionTableModel.builder().id(question.getId()).eventId(eventId)
				.questionTitle(question.getQuestionName()).questionTypeId(question.getQuestionType().getDbId())
				.order(order).build()).getId();
	}
	
	@Override
	public void deleteEvent(@NotNull Long eventId) throws EntityNotFoundException {
		if (getEventRepository().existsById(eventId)) {
			List<QuestionTableModel> allQuestions = getQuestionRepository().findAllByEventId(eventId);
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
