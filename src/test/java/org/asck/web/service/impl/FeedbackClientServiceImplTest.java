package org.asck.web.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.asck.api.exceptions.EntityNotFoundException;
import org.asck.api.repository.model.QuestionTableModel;
import org.asck.api.service.IFeedbackService;
import org.asck.api.service.model.QuestionType;
import org.asck.web.exceptions.ClientServiceRuntimeException;
import org.asck.web.service.model.Answer;
import org.asck.web.service.model.AnswerReport;
import org.asck.web.service.model.Event;
import org.asck.web.service.model.Option;
import org.asck.web.service.model.Question;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RunWith(SpringRunner.class)
@RestClientTest(FeedbackClientServiceImpl.class)
@AutoConfigureJsonTesters
public class FeedbackClientServiceImplTest {

	@Autowired
	private FeedbackClientServiceImpl client;

	@MockBean
	IFeedbackService feedbackService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testSaveAnswer() throws Exception {
		Answer saveAnswer = new Answer(1L, 2L, "remark", LocalDateTime.now());

		when(client.getFeedbackService().saveAnswer(new org.asck.api.service.model.Answer(saveAnswer.getQuestionId(), saveAnswer.getOptionId(), saveAnswer.getRemark(), saveAnswer.getAnsweredAt()))).thenReturn(1L);

		client.saveAnswer(saveAnswer);

		verify(client.getFeedbackService()).saveAnswer(new org.asck.api.service.model.Answer(saveAnswer.getQuestionId(), saveAnswer.getOptionId(), saveAnswer.getRemark(), saveAnswer.getAnsweredAt()));

	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleEvents(Long)}.
	 */
	@Test
	public void testLeseAlleEvents_RESTAPIReturnsStatusNoContent_ReturnEmptyList() throws Exception {

		when(client.getFeedbackService().findEventsOwnedBy(1L)).thenReturn(new ArrayList<>());

		List<Event> leseAlleEvents = client.leseAlleEvents(1L);

		verify(client.getFeedbackService()).findEventsOwnedBy(1L);

		assertNotNull(leseAlleEvents);
		assertTrue(leseAlleEvents.isEmpty());

	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleEvents(Long)}.
	 */
	@Test
	public void testLeseAlleEvents_ReturnListWithEvents() throws Exception {

		List<org.asck.api.service.model.Event> events = new ArrayList<>();
		events.add(org.asck.api.service.model.Event.builder().id(1L).name("Event1").build());
		events.add(org.asck.api.service.model.Event.builder().id(2L).name("Event2").build());

		when(client.getFeedbackService().findEventsOwnedBy(1L)).thenReturn(events);


		List<Event> leseAlleEvents = client.leseAlleEvents(1L);

		verify(client.getFeedbackService()).findEventsOwnedBy(1L);

		assertNotNull(leseAlleEvents);
		assertFalse(leseAlleEvents.isEmpty());
		assertEquals(2, leseAlleEvents.size());
		assertEquals(Event.builder().id(1L).name("Event1").build(), leseAlleEvents.get(0));
		assertEquals(Event.builder().id(2L).name("Event2").build(), leseAlleEvents.get(1));
	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleEvents(Long)} .
	 */
	@Test
	public void testLeseAlleEvents_NoEventsFound_ReturnEmptyList() throws Exception {

		when(client.getFeedbackService().findEventsOwnedBy(1L)).thenReturn(null);

		List<Event> events = client.leseAlleEvents(1L);

		verify(client.getFeedbackService()).findEventsOwnedBy(1L);

		assertNotNull(events);
		assertTrue(events.isEmpty());
	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleFragenZuEvent(java.lang.Long)}.
	 */
	@Test
	public void testLeseAlleFragenZuEvent_ReturnsNullEvent() throws Exception {

		when(client.getFeedbackService().findEventById(1L)).thenReturn(null);

		List<Question> questions = client.leseAlleFragenZuEvent(1L);
		assertNotNull(questions);
		assertTrue(questions.isEmpty());
	}

	@Test
	public void testLeseAlleFragenZuEvent_ReturnsEmptyList() throws Exception {

		when(client.getFeedbackService().findEventById(1L)).thenReturn(new org.asck.api.service.model.Event(1L, "Test", 1L, new ArrayList<org.asck.api.service.model.Question>()));

		List<Question> questions = client.leseAlleFragenZuEvent(1L);
		assertNotNull(questions);
		assertTrue(questions.isEmpty());
	}
//
	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleFragenZuEvent(java.lang.Long)}.
	 */
	@Test
	public void testLeseAlleFragenZuEvent_ReturnsListWithQuestions() throws Exception {

		List<org.asck.api.service.model.Question> questions = new ArrayList<>();
		questions.add(org.asck.api.service.model.Question.builder().id(1L).questionName("Question1")
				.questionType(QuestionType.FREETEXT).order(1).build());
		questions.add(org.asck.api.service.model.Question.builder().id(2L).questionName("Question2")
				.questionType(QuestionType.FIVE_SMILEYS).order(2).build());
		questions.add(org.asck.api.service.model.Question.builder().id(3L).questionName("Question3")
				.questionType(QuestionType.THREE_SMILEYS).order(3).build());
		questions.add(org.asck.api.service.model.Question.builder().id(4L).questionName("Question4")
				.questionType(QuestionType.YES_NO).order(4).build());
		Long eventId = 1L;

		expectLeseAlleFragenZuEvent(eventId, questions);

		List<Question> questionsClientList = client.leseAlleFragenZuEvent(eventId);
		assertNotNull(questionsClientList);
		assertFalse(questionsClientList.isEmpty());
		assertEquals(4, questionsClientList.size());
		assertEquals(Question.builder().id(1L).questionName("Question1").questionType(QuestionType.FREETEXT.name())
				.order(1).build(), questionsClientList.get(0));
		assertEquals(Question.builder().id(2L).questionName("Question2").questionType(QuestionType.FIVE_SMILEYS.name())
				.order(2).build(), questionsClientList.get(1));
		assertEquals(Question.builder().id(3L).questionName("Question3").questionType(QuestionType.THREE_SMILEYS.name())
				.order(3).build(), questionsClientList.get(2));
		assertEquals(Question.builder().id(4L).questionName("Question4").questionType(QuestionType.YES_NO.name())
				.order(4).build(), questionsClientList.get(3));
	}

	private void expectLeseAlleFragenZuEvent(Long eventId, List<org.asck.api.service.model.Question> questions) throws EntityNotFoundException {
		when(client.getFeedbackService().findEventById(eventId)).thenReturn(new org.asck.api.service.model.Event(eventId, "TestEvent", 1L, questions));
	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleFragenZuEvent(java.lang.Long)}.
	 */
	@Test
	public void testLeseAlleFragenZuEvent_ThrowsException() throws Exception {

		when(client.getFeedbackService().findEventById(1L)).thenThrow(EntityNotFoundException.class);

		thrown.expect(ClientServiceRuntimeException.class);
		thrown.expectMessage(equalTo("Error on retrieve questions for event with id 1"));

		client.leseAlleFragenZuEvent(1L);

	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#getEventById(java.lang.Long)}.
	 */
	@Test
	public void testGetEventById_ReturnEvent() throws Exception {
		expectLeseAlleFragenZuEvent(1L, new ArrayList<>());

		Event event = this.client.getEventById(1L);
		assertNotNull(event);
		assertEquals(Event.builder().id(1L).name("TestEvent").ownedBy(1L).build(), event);
	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#getEventById(java.lang.Long)}.
	 */
	@Test
	public void testGetEventById_ReturnNull() throws Exception {

		when(client.getFeedbackService().findEventById(1L)).thenReturn(null);

		Event event = this.client.getEventById(1L);
		assertNull(event);
	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleOptionenZuEinerFrage(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testLeseAlleOptionenZuEinerFrage() throws Exception {
		List<org.asck.api.service.model.Option> optionList = new ArrayList<>();
		optionList.add(org.asck.api.service.model.Option.builder().id(1L).iconPath("/iconPath1").build());
		optionList.add(org.asck.api.service.model.Option.builder().id(2L).iconPath("/iconPath2").build());

		when(client.getFeedbackService().findQuestion(1L, 1L)).thenReturn(createQuestion(optionList));

		List<Option> options = this.client.leseAlleOptionenZuEinerFrage(1L, 1L);
		assertNotNull(options);
		assertFalse(options.isEmpty());
		assertEquals(2, options.size());
		assertEquals(Option.builder().id(1L).iconPath("/iconPath1").build(), options.get(0));
		assertEquals(Option.builder().id(2L).iconPath("/iconPath2").build(), options.get(1));
	}

	@NotNull
	private org.asck.api.service.model.Question createQuestion(List<org.asck.api.service.model.Option> optionList) {
		return new org.asck.api.service.model.Question(1L, "QuestionName", QuestionType.FREETEXT, optionList, 1, false);
	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#deleteQuestion(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testDeleteQuestion() throws Exception {
		this.client.deleteQuestion(1L, 2L);
	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#deleteEvent(java.lang.Long)}.
	 */
	@Test
	public void testDeleteEvent() throws Exception {
		this.client.deleteEvent(1L);
	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#findOptionById(java.lang.Long)}.
	 */
	@Test
	public void testFindOptionById() throws Exception {

		when(client.getFeedbackService().getOptionById(1L)).thenReturn(new org.asck.api.service.model.Option(1L, "description", "/iconPath1"));

		Option option = this.client.findOptionById(1L);
		assertNotNull(option);
		assertEquals(Option.builder().id(1L).iconPath("/iconPath1").optionalDescription("description").build(), option);
	}
//
	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#readQuestion(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testReadQuestion() throws Exception {
		when(client.getFeedbackService().findQuestion(1L, 2L)).thenReturn(createQuestion(null));

		Question question = this.client.readQuestion(1L, 2L);
		assertNotNull(question);
		assertEquals(Question.builder().id(1L).questionName("QuestionName").order(1).questionType(QuestionType.FREETEXT.name()).build(), question);
	}

	/**
	 * Test method for
	 * {@link org.asck.web.service.impl.FeedbackClientServiceImpl#readAllSupportedQuestionTypes()}.
	 */
	@Test
	public void testReadAllSupportedQuestionTypes() throws Exception {
		List<QuestionType> list = new ArrayList<>();
		list.add(QuestionType.FIVE_SMILEYS);
		list.add(QuestionType.FREETEXT);
		list.add(QuestionType.THREE_SMILEYS);
		list.add(QuestionType.YES_NO);


		List<String> responseList = this.client.readAllSupportedQuestionTypes();
		assertNotNull(responseList);
		assertFalse(responseList.isEmpty());
		assertEquals(4, responseList.size());
		assertEquals(QuestionType.FREETEXT.name(), responseList.get(0));
		assertEquals(QuestionType.THREE_SMILEYS.name(), responseList.get(1));
		assertEquals(QuestionType.FIVE_SMILEYS.name(), responseList.get(2));
		assertEquals(QuestionType.YES_NO.name(), responseList.get(3));
	}

	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#saveEvent(org.asck.web.service.model.Event)}.
	 */
	@Test
	public void testSaveEvent_ReturnsNewEventWithId() throws Exception {

		Event savedEvent = Event.builder().id(null).name("NewEvent").build();
		when(client.getFeedbackService().saveEvent(client.map(savedEvent))).thenReturn(1L);

		savedEvent = this.client.saveEvent(savedEvent);
		assertNotNull(savedEvent);
		assertEquals(Event.builder().id(1L).name("NewEvent").build(), savedEvent);
	}

	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#saveEvent(org.asck.web.service.model.Event)}.
	 */
	@Test
	public void testSaveEvent_ReturnsUpdatedEvent() throws Exception {


		Event updatedEvent = Event.builder().id(3L).name("UpdatedEvent").build();

		when(client.getFeedbackService().saveEvent(client.map(updatedEvent))).thenReturn(3L);

		Event savedEvent = this.client.saveEvent(updatedEvent);
		assertNotNull(savedEvent);
		assertEquals(Event.builder().id(3L).name("UpdatedEvent").build(), savedEvent);
	}

	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#saveQuestion(java.lang.Long, org.asck.web.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_ReturnsNewQuestionWithId() throws Exception {

		Question newQuestion = Question.builder().id(null).questionName("NewQuestionName").order(1).questionType(QuestionType.FIVE_SMILEYS.name()).build();

		when(client.getFeedbackService().saveQuestion(1L , client.map(newQuestion))).thenReturn(2L);

		Question saveQuestion = this.client.saveQuestion(1L, newQuestion);
		assertNotNull(saveQuestion);
		assertEquals(Question.builder().id(2L).questionName("NewQuestionName").order(1).questionType(QuestionType.FIVE_SMILEYS.name()).build(), saveQuestion);
	}

	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#saveQuestion(java.lang.Long, org.asck.web.service.model.Question)}.
	 */
	@Test
	public void testSaveQuestion_ReturnsUpdatedQuestion() throws Exception {

		Question newQuestion = Question.builder().id(2L).questionName("NewQuestionName").order(1).questionType(QuestionType.FIVE_SMILEYS.name()).build();

		when(client.getFeedbackService().saveQuestion(1L , client.map(newQuestion))).thenReturn(2L);

		Question saveQuestion = this.client.saveQuestion(1L, newQuestion);
		assertNotNull(saveQuestion);
		assertEquals(Question.builder().id(2L).questionName("NewQuestionName").order(1).questionType(QuestionType.FIVE_SMILEYS.name()).build(), saveQuestion);
	}

	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#getAllAnswersToEventId(java.lang.Long)}.
	 */
	@Test
	public void testGetAllAnswersToEventId_ReturnsEmptyList() throws Exception {

		expectLeseAlleFragenZuEvent(1L, Collections.emptyList());

		List<AnswerReport> report = this.client.getAllAnswersToEventId(1L);
		assertNotNull(report);
		assertTrue(report.isEmpty());
	}

	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#getAllAnswersToEventId(java.lang.Long)}.
	 */
	@Test
	public void testGetAllAnswersToEventId_OneQuestionForEventExist_ReturnsList() throws Exception {

		List<org.asck.api.service.model.Question> questions = new ArrayList<>();
		questions.add(org.asck.api.service.model.Question.builder().id(2L).order(1).questionName("Question").questionType(QuestionType.YES_NO).build());
		expectLeseAlleFragenZuEvent(1L, questions);

		List<AnswerReport> report = this.client.getAllAnswersToEventId(1L);
		assertNotNull(report);
		assertTrue(report.isEmpty());
	}

	@Test
	public void mapEventApiToClient() {
		List<org.asck.api.service.model.Question> questions = new ArrayList<>();
		questions.add(createQuestion(null));
		Event mappedEvent = client.map(new org.asck.api.service.model.Event(1L, "Event1", 2L, questions));

		assertEquals(new Event(1L, "Event1", 2L), mappedEvent);

	}

	@Test
	public void mapEventClientToApi() {
		org.asck.api.service.model.Event event1 = new org.asck.api.service.model.Event(1L, "Event1", 2L, null);
		Event event2 = new Event(1L, "Event1", 2L);

		org.asck.api.service.model.Event mappedEvent = client.map(event2);

		assertEquals(event1, mappedEvent);

	}

}
