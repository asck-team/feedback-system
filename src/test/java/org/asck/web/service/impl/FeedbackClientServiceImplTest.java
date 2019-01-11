package org.asck.web.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.asck.api.service.model.QuestionType;
import org.asck.web.exceptions.ClientServiceRuntimeException;
import org.asck.web.service.model.Answer;
import org.asck.web.service.model.Event;
import org.asck.web.service.model.Option;
import org.asck.web.service.model.Question;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.json.JacksonTester;
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
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Autowired
    private FeedbackClientServiceImpl client;
 
    @Autowired
    private MockRestServiceServer server;
 
    @Autowired
    JacksonTester<Answer> json;
    @Autowired
    JacksonTester<List<org.asck.api.service.model.Event>> jsonEventList;
    @Autowired
    JacksonTester<org.asck.api.service.model.Event> jsonEvent;
    @Autowired
    JacksonTester<List<org.asck.api.service.model.Question>> jsonQuestionList;
    @Autowired
    JacksonTester<List<org.asck.api.service.model.Option>> jsonOptionList;
	

	@Test
	public void testGetAllAnswersToEventId() throws Exception {
		Answer saveAnswer = new Answer(1L, 2L, "remark", LocalDateTime.now());
		String jsonString = json.write(saveAnswer).getJson(); 
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/answers")).andExpect(method(HttpMethod.POST)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(content().string(jsonString)).andRespond(withCreatedEntity(new URI("http://localhost/v1/feedback/answers/1")));
		
		
		client.saveAnswer(saveAnswer);
		
		
	}


	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleEvents()}.
	 */
	@Test
	public void testLeseAlleEvents_RESTAPIReturnsStatusNoContent_ReturnEmptyList() throws Exception {
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/events")).andExpect(method(HttpMethod.GET)).andRespond(withNoContent());
		
		List<Event> leseAlleEvents = client.leseAlleEvents();
		assertNotNull(leseAlleEvents);
		assertTrue(leseAlleEvents.isEmpty());
	}
	
	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleEvents()}.
	 */
	@Test
	public void testLeseAlleEvents_RESTAPIReturnsStatusOk_ReturnListWithEvents() throws Exception {
		
		List<org.asck.api.service.model.Event> events = new ArrayList<>();
		events.add(org.asck.api.service.model.Event.builder().id(1L).name("Event1").build());
		events.add(org.asck.api.service.model.Event.builder().id(2L).name("Event2").build());
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/events")).andExpect(method(HttpMethod.GET)).andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON_UTF8).body(jsonEventList.write(events).getJson()));
		
		List<Event> leseAlleEvents = client.leseAlleEvents();
		assertNotNull(leseAlleEvents);
		assertFalse(leseAlleEvents.isEmpty());
		assertEquals(2, leseAlleEvents.size());
		assertEquals(Event.builder().id(1L).name("Event1").build(), leseAlleEvents.get(0));
		assertEquals(Event.builder().id(2L).name("Event2").build(), leseAlleEvents.get(1));
	}
	
	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleEvents()}.
	 */
	@Test
	public void testLeseAlleEvents_RESTAPIReturnsServerError_ThrowsException() throws Exception {
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/events")).andExpect(method(HttpMethod.GET)).andRespond(withServerError());

		thrown.expect(ClientServiceRuntimeException.class);
		thrown.expectCause(isA(HttpServerErrorException.class));
		thrown.expectMessage(is("Error on retrieve events"));
		
		client.leseAlleEvents();
	}


	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleFragenZuEvent(java.lang.Long)}.
	 */
	@Test
	public void testLeseAlleFragenZuEvent_RestAPIReturnsNoContent_ReturnsEmptyList() throws Exception {
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/events/1/questions")).andExpect(method(HttpMethod.GET)).andRespond(withNoContent());
	
		List<Question> questions = client.leseAlleFragenZuEvent(1L);
		assertNotNull(questions);
		assertTrue(questions.isEmpty());
	}
	
	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleFragenZuEvent(java.lang.Long)}.
	 */
	@Test
	public void testLeseAlleFragenZuEvent_RestAPIReturnsSuccess_ReturnsListWithQuestions() throws Exception {
		
		List<org.asck.api.service.model.Question> questions = new ArrayList<>();
		questions.add(org.asck.api.service.model.Question.builder().id(1L).questionName("Question1").questionType(QuestionType.FREETEXT).order(1).build());
		questions.add(org.asck.api.service.model.Question.builder().id(2L).questionName("Question2").questionType(QuestionType.FIVE_SMILEYS).order(2).build());
		questions.add(org.asck.api.service.model.Question.builder().id(3L).questionName("Question3").questionType(QuestionType.THREE_SMILEYS).order(3).build());
		questions.add(org.asck.api.service.model.Question.builder().id(4L).questionName("Question4").questionType(QuestionType.YES_NO).order(4).build());
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/events/1/questions")).andExpect(method(HttpMethod.GET)).andRespond(withSuccess(jsonQuestionList.write(questions).getJson(), MediaType.APPLICATION_JSON_UTF8));
	
		List<Question> questionsClientList = client.leseAlleFragenZuEvent(1L);
		assertNotNull(questionsClientList);
		assertFalse(questionsClientList.isEmpty());
		assertEquals(4, questionsClientList.size());
		assertEquals(Question.builder().id(1L).questionName("Question1").questionType(QuestionType.FREETEXT.name()).order(1).build(), questionsClientList.get(0));
		assertEquals(Question.builder().id(2L).questionName("Question2").questionType(QuestionType.FIVE_SMILEYS.name()).order(2).build(), questionsClientList.get(1));
		assertEquals(Question.builder().id(3L).questionName("Question3").questionType(QuestionType.THREE_SMILEYS.name()).order(3).build(), questionsClientList.get(2));
		assertEquals(Question.builder().id(4L).questionName("Question4").questionType(QuestionType.YES_NO.name()).order(4).build(), questionsClientList.get(3));
	}
	
	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleFragenZuEvent(java.lang.Long)}.
	 */
	@Test
	public void testLeseAlleFragenZuEvent_RestAPIReturnsError_ThrowsException() throws Exception {
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/events/1/questions")).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.NOT_FOUND));
	
		thrown.expect(ClientServiceRuntimeException.class);
		thrown.expectMessage(equalTo("Error on retrieve questions for event with id 1"));
		thrown.expectCause(isA(HttpClientErrorException.class));
		
		client.leseAlleFragenZuEvent(1L);
		
	}


	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#getEventById(java.lang.Long)}.
	 */
	@Test
	public void testGetEventById_RESTApiReturnsSuccess_ReturnEvent() throws Exception {
		
		String jsonString = jsonEvent.write(org.asck.api.service.model.Event.builder().id(1l).name("Question").build()).getJson();
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/events/1")).andExpect(method(HttpMethod.GET)).andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON_UTF8).body(jsonString));
		
		Event event = this.client.getEventById(1L);
		assertNotNull(event);
		assertEquals(Event.builder().id(1L).name("Question").build(), event);
	}
	
	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#getEventById(java.lang.Long)}.
	 */
	@Test
	public void testGetEventById_RESTApiReturnsNotFound_ReturnNull() throws Exception {
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/events/1")).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.NOT_FOUND));
		
		Event event = this.client.getEventById(1L);
		assertNull(event);
	}


	/**
	 * Test method for {@link org.asck.web.service.impl.FeedbackClientServiceImpl#leseAlleOptionenZuEinerFrage(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testLeseAlleOptionenZuEinerFrage() throws Exception {
		List<org.asck.api.service.model.Option> optionList = new ArrayList<>();
		optionList.add(org.asck.api.service.model.Option.builder().id(1L).iconPath("/iconPath1").build());
		optionList.add(org.asck.api.service.model.Option.builder().id(2L).iconPath("/iconPath2").build());
		String jsonString = jsonOptionList.write(optionList).getJson();
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/events/1/questions/1/options")).andExpect(method(GET)).andRespond(withSuccess(jsonString, MediaType.APPLICATION_JSON_UTF8));
		List<Option> options = this.client.leseAlleOptionenZuEinerFrage(1L, 1L);
		assertNotNull(options);
		assertFalse(options.isEmpty());
		assertEquals(2, options.size());
		assertEquals(Option.builder().id(1L).iconPath("/iconPath1").build(), options.get(0));
		assertEquals(Option.builder().id(2L).iconPath("/iconPath2").build(), options.get(1));
	}

	

}
