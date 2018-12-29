package org.asck.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.asck.exceptions.EntityNotFoundException;
import org.asck.repository.AnswerRepository;
import org.asck.repository.EventRepository;
import org.asck.repository.QuestionOptionRepository;
import org.asck.repository.QuestionRepository;
import org.asck.repository.QuestionTypeRepository;
import org.asck.repository.model.QuestionTableModel;
import org.asck.service.model.Question;
import org.asck.service.model.QuestionType;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.AccessLevel;
import lombok.Getter;

@RunWith(SpringRunner.class)
@Getter(AccessLevel.PROTECTED)
public class FeedbackServiceImplTest {

	@TestConfiguration
	static class Conf {

		@Bean
		public FeedbackServiceImpl createUnderTets() {
			return new FeedbackServiceImpl();
		}

	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@MockBean
	private QuestionRepository questionRepository;
	@MockBean
	private EventRepository eventRepository;
	@MockBean
	private QuestionTypeRepository questionTypeRepository;
	@MockBean
	private QuestionOptionRepository questionOptionRepository;
	@MockBean
	private AnswerRepository answerRepository;

	@Autowired
	private FeedbackServiceImpl underTest;

	@After
	public void after() {
		verifyNoMoreInteractions(getQuestionRepository());
		verifyNoMoreInteractions(getAnswerRepository());
		verifyNoMoreInteractions(getEventRepository());
		verifyNoMoreInteractions(getQuestionOptionRepository());
		verifyNoMoreInteractions(getQuestionTypeRepository());
	}

	/**
	 * Test method for
	 * {@link org.asck.service.impl.FeedbackServiceImpl#saveQuestion(java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_NoQuestionsExists_SaveQuestionWithOrder1() throws Exception {

		when(getQuestionRepository().findAllByEventIdOrderByOrder(1L)).thenReturn(new ArrayList<QuestionTableModel>());
		QuestionTableModel questionTableModel2Save = QuestionTableModel.builder().id(-1L).eventId(1L).order(1)
				.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();
		when(getQuestionRepository().save(questionTableModel2Save)).thenReturn(QuestionTableModel.builder().id(2L).build());

		underTest.saveQuestion(1L, Question.create(-1L, "questionName", QuestionType.FIVE_SMILEYS.name()));

		verify(getQuestionRepository()).findAllByEventIdOrderByOrder(1L);
		verify(getQuestionRepository()).save(questionTableModel2Save);
	}

	/**
	 * Test method for
	 * {@link org.asck.service.impl.FeedbackServiceImpl#saveQuestion(java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_NoQuestionsExistsQuestionWithOrder2Defined_SaveQuestionWithOrder1() throws Exception {

		when(questionRepository.findAllByEventIdOrderByOrder(1L)).thenReturn(new ArrayList<QuestionTableModel>());
		QuestionTableModel questionTableModel2Save = QuestionTableModel.builder().id(-1L).eventId(1L).order(1)
				.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();
		when(questionRepository.save(questionTableModel2Save)).thenReturn(QuestionTableModel.builder().id(2L).build());

		underTest.saveQuestion(1L, Question.builder().id(-1L).order(2).questionName("questionName")
				.questionType(QuestionType.FIVE_SMILEYS).build());

		verify(getQuestionRepository()).findAllByEventIdOrderByOrder(1L);
		verify(getQuestionRepository()).save(questionTableModel2Save);
	}

	protected QuestionTableModel createQTM(long id, long eventId, String questionTitle, QuestionType questionType,
			int order) {
		return QuestionTableModel.builder().id(id).eventId(eventId).order(order).questionTitle(questionTitle)
				.questionTypeId(questionType.getDbId()).build();
	}

	/**
	 * Test method for
	 * {@link org.asck.service.impl.FeedbackServiceImpl#saveQuestion(java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_OneQuestionsExists_SaveQuestionWithOrder2() throws Exception {

		QuestionTableModel question1 = createQTM(1L, 1L, "questionName", QuestionType.FIVE_SMILEYS, 1);

		when(questionRepository.findAllByEventIdOrderByOrder(1L)).thenReturn(Arrays.asList(question1));
		QuestionTableModel questionTableModel2Save = QuestionTableModel.builder().id(-1L).eventId(1L).order(2)
				.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();
		when(questionRepository.save(questionTableModel2Save)).thenReturn(QuestionTableModel.builder().id(2L).build());

		underTest.saveQuestion(1L, Question.builder().id(-1L).order(2).questionName("questionName")
				.questionType(QuestionType.FIVE_SMILEYS).build());

		verify(getQuestionRepository()).findAllByEventIdOrderByOrder(1L);
		verify(getQuestionRepository()).save(questionTableModel2Save);
	}

	/**
	 * Test method for
	 * {@link org.asck.service.impl.FeedbackServiceImpl#saveQuestion(java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_OneQuestionsExistsQuestionToSaveIsTheSameButHasNewOrderOf2_SaveQuestionWithOrder1()
			throws Exception {

		when(questionRepository.findAllByEventIdOrderByOrder(1L))
				.thenReturn(Arrays.asList(QuestionTableModel.builder().id(1L).eventId(1L).order(1)
						.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build()));
		QuestionTableModel questionTableModel2Save = QuestionTableModel.builder().id(1L).eventId(1L).order(1)
				.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();
		when(questionRepository.save(questionTableModel2Save)).thenReturn(questionTableModel2Save);

		underTest.saveQuestion(1L, Question.builder().id(1L).order(2).questionName("questionName")
				.questionType(QuestionType.FIVE_SMILEYS).build());

		verify(getQuestionRepository()).findAllByEventIdOrderByOrder(1L);
		verify(getQuestionRepository()).save(questionTableModel2Save);
	}

	/**
	 * Test method for
	 * {@link org.asck.service.impl.FeedbackServiceImpl#saveQuestion(java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_OneQuestionsExistsQuestionToSaveHaveOrderLowerThenZero_SaveQuestionWithOrder2()
			throws Exception {

		when(questionRepository.findAllByEventIdOrderByOrder(1L))
				.thenReturn(Arrays.asList(QuestionTableModel.builder().id(1L).eventId(1L).order(1)
						.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build()));
		QuestionTableModel questionTableModel2Save = QuestionTableModel.builder().id(-1L).eventId(1L).order(2)
				.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();
		when(questionRepository.save(questionTableModel2Save)).thenReturn(QuestionTableModel.builder().id(2L).build());

		underTest.saveQuestion(1L, Question.builder().id(-1L).order(-1).questionName("questionName")
				.questionType(QuestionType.FIVE_SMILEYS).build());

		verify(getQuestionRepository()).findAllByEventIdOrderByOrder(1L);
		verify(getQuestionRepository()).save(questionTableModel2Save);
		verifyNoMoreInteractions(getQuestionRepository());
	}

	/**
	 * Test method for
	 * {@link org.asck.service.impl.FeedbackServiceImpl#saveQuestion(java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_OneQuestionsExistsQuestionToSaveHaveOrderGreaterThenMaxQuestionSize_SaveQuestionWithOrder2()
			throws Exception {

		when(questionRepository.findAllByEventIdOrderByOrder(1L))
				.thenReturn(Arrays.asList(QuestionTableModel.builder().id(1L).eventId(1L).order(1)
						.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build()));
		QuestionTableModel questionTableModel2Save = QuestionTableModel.builder().id(-1L).eventId(1L).order(2)
				.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();
		when(questionRepository.save(questionTableModel2Save)).thenReturn(QuestionTableModel.builder().id(2L).build());

		underTest.saveQuestion(1L, Question.builder().id(-1L).order(3).questionName("questionName")
				.questionType(QuestionType.FIVE_SMILEYS).build());

		verify(getQuestionRepository()).findAllByEventIdOrderByOrder(1L);
		verify(getQuestionRepository()).save(questionTableModel2Save);
	}

	/**
	 * Test method for
	 * {@link org.asck.service.impl.FeedbackServiceImpl#saveQuestion(java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_TwoQuestionsExistsAndQuestionToSaveHaveOrderBetweenBoth_SaveQuestionWithOrder2AndShiftExistingOneToOrder3AndSaveOnDatabase()
			throws Exception {

		QuestionTableModel question1 = QuestionTableModel.builder().id(1L).eventId(1L).order(1)
				.questionTitle("question1").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();
		QuestionTableModel question2 = QuestionTableModel.builder().id(2L).eventId(1L).order(2)
				.questionTitle("question2").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();
		QuestionTableModel question2WithNewOrder = QuestionTableModel.builder().id(2L).eventId(1L).order(3)
				.questionTitle("question2").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();

		when(questionRepository.findAllByEventIdOrderByOrder(1L)).thenReturn(Arrays.asList(question1, question2));
		QuestionTableModel questionTableModel2Save = QuestionTableModel.builder().id(-1L).eventId(1L).order(2)
				.questionTitle("questionName").questionTypeId(QuestionType.FIVE_SMILEYS.getDbId()).build();

		when(questionRepository.save(question2WithNewOrder)).thenReturn(question2WithNewOrder);

		when(questionRepository.save(questionTableModel2Save)).thenReturn(QuestionTableModel.builder().id(2L).build());

		underTest.saveQuestion(1L, Question.builder().id(-1L).order(2).questionName("questionName")
				.questionType(QuestionType.FIVE_SMILEYS).build());

		verify(getQuestionRepository()).findAllByEventIdOrderByOrder(1L);
		verify(getQuestionRepository()).save(question2WithNewOrder);
		verify(getQuestionRepository()).save(questionTableModel2Save);
	}
	
	/**
	 * Test method for
	 * {@link org.asck.service.impl.FeedbackServiceImpl#saveQuestion(java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_QuestionWithIdSpecifedButDoesntExist_ThrowsException()
			throws Exception {
		
		try {
			thrown.expect(EntityNotFoundException.class);
			thrown.expectMessage(Matchers.equalTo(new EntityNotFoundException(QuestionTableModel.class, "id", "1").getLocalizedMessage()));
			
			when(questionRepository.findAllByEventIdOrderByOrder(1L)).thenReturn(new ArrayList<>());
			
			underTest.saveQuestion(1L, Question.builder().id(1L).order(2).questionName("questionName")
					.questionType(QuestionType.FIVE_SMILEYS).build());
			
		} finally {
			verify(getQuestionRepository()).findAllByEventIdOrderByOrder(1L);
		}

	}
	

}
