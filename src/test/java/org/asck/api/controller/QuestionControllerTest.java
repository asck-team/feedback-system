package org.asck.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.asck.api.exceptions.EntityNotFoundException;
import org.asck.api.service.IFeedbackService;
import org.asck.api.service.model.Event;
import org.asck.api.service.model.Question;
import org.asck.api.service.model.QuestionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureJsonTesters
public class QuestionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	IFeedbackService feedbackServiceMock;

	@Autowired
	JacksonTester<Question> json;

	@Test
	public void testReadAll_NoEventFoundForId20_ReturnsHttpStatusNotFound() throws Exception {

		when(feedbackServiceMock.findEventById(20L)).thenThrow(new EntityNotFoundException(Event.class, "id", "20"));

		mockMvc.perform(get("/v1/feedback/events/20/questions")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.apierror.status", is("NOT_FOUND")))
				.andExpect(jsonPath("$.apierror.message", is("Event was not found for parameters {id=20}")))
				.andExpect(jsonPath("$.apierror.debugMessage", isEmptyOrNullString()))
				.andExpect(jsonPath("$.apierror.subErrors", nullValue()));

	}

	@Test
	public void testReadAll_NoQuestionsFound_ReturnHttpStatusNoContent() throws Exception {
		when(feedbackServiceMock.findEventById(20L)).thenReturn(new Event(20L, "Question", Collections.emptyList()));

		mockMvc.perform(get("/v1/feedback/events/20/questions")).andDo(print()).andExpect(status().isNoContent())
				.andExpect(content().string(""));

		verify(feedbackServiceMock).findEventById(20L);
	}

	/**
	 * Test method for
	 * {@link org.asck.controller.QuestionController#createQuestion(java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testCreateQuestion() throws Exception {
		when(feedbackServiceMock.saveQuestion(20L,
				Question.builder().id(-1L).questionName("Name").questionType(QuestionType.FIVE_SMILEYS).build()))
						.thenReturn(21L);

		String jsonValue = json
				.write(Question.builder().id(21L).questionName("Name").questionType(QuestionType.FIVE_SMILEYS).build())
				.getJson();

		mockMvc.perform(post("/v1/feedback/events/20/questions").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonValue)).andDo(print()).andExpect(status().isCreated())
				.andExpect(header().string("location", "http://localhost/v1/feedback/events/20/questions/21"));

		verify(feedbackServiceMock).saveQuestion(20L,
				Question.builder().id(-1L).questionName("Name").questionType(QuestionType.FIVE_SMILEYS).build());
	}

	/**
	 * Test method for
	 * {@link org.asck.controller.QuestionController#updateQuestion(java.lang.Long, java.lang.Long, org.asck.service.model.Question)}.
	 */
	@Test
	public void testUpdateQuestion() throws Exception {
		when(feedbackServiceMock.saveQuestion(20L,
				Question.builder().id(21L).questionName("Name").questionType(QuestionType.FIVE_SMILEYS).build()))
						.thenReturn(21L);

		String jsonValue = json
				.write(Question.builder().id(21L).questionName("Name").questionType(QuestionType.FIVE_SMILEYS).build())
				.getJson();

		mockMvc.perform(put("/v1/feedback/events/20/questions/21").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonValue)).andDo(print()).andExpect(status().isNoContent());

		verify(feedbackServiceMock).saveQuestion(20L,
				Question.builder().id(21L).questionName("Name").questionType(QuestionType.FIVE_SMILEYS).build());

	}

	/**
	 * Test method for
	 * {@link org.asck.controller.QuestionController#deleteQuestion(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testDeleteQuestion_EventWithIdDoesntExists_ReturnsNotFoundAndApiError() throws Exception {

		doThrow(new EntityNotFoundException(Event.class, "id", "20")).when(feedbackServiceMock).deleteQuestion(20L,
				21L);

		mockMvc.perform(delete("/v1/feedback/events/20/questions/21")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.apierror.status", is("NOT_FOUND")))
				.andExpect(jsonPath("$.apierror.message", is("Event was not found for parameters {id=20}")))
				.andExpect(jsonPath("$.apierror.debugMessage", isEmptyOrNullString()))
				.andExpect(jsonPath("$.apierror.subErrors", nullValue()));
		verify(feedbackServiceMock).deleteQuestion(20L, 21L);

	}

	/**
	 * Test method for
	 * {@link org.asck.controller.QuestionController#deleteQuestion(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testDeleteQuestion_QuestionWithIdDoesntExists_ReturnsNotFoundAndApiError() throws Exception {

		doThrow(new EntityNotFoundException(Question.class, "id", "21")).when(feedbackServiceMock).deleteQuestion(20L,
				21L);

		mockMvc.perform(delete("/v1/feedback/events/20/questions/21")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.apierror.status", is("NOT_FOUND")))
				.andExpect(jsonPath("$.apierror.message", is("Question was not found for parameters {id=21}")))
				.andExpect(jsonPath("$.apierror.debugMessage", isEmptyOrNullString()))
				.andExpect(jsonPath("$.apierror.subErrors", nullValue()));
		verify(feedbackServiceMock).deleteQuestion(20L, 21L);

	}

	/**
	 * Test method for {@link org.asck.controller.QuestionController#deleteQuestion(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testDeleteQuestion_EventWithIdExistAndQuestionWithIdExistsAndDeleted_ReturnsNoContent() throws Exception {
		
		mockMvc.perform(delete("/v1/feedback/events/20/questions/21")).andDo(print()).andExpect(status().isNoContent());
	
		verify(feedbackServiceMock).deleteQuestion(20L, 21L);
		
	}

}
