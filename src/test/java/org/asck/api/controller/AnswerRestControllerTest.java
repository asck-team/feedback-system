/**
 * 
 */
package org.asck.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.asck.api.exceptions.EntityNotFoundException;
import org.asck.api.repository.model.QuestionTableModel;
import org.asck.api.service.IFeedbackService;
import org.asck.api.service.model.Answer;
import org.hamcrest.Matchers;
import org.junit.Ignore;
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
@SpringBootTest(properties = "security.enabled=false")
@AutoConfigureJsonTesters
public class AnswerRestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	IFeedbackService feedbackServiceMock;

	@Autowired
	JacksonTester<Answer> json;
	
	JacksonTester<List<Answer>> jsonList;
	

	/**
	 * Test method for
	 * {@link org.asck.AnswerRestController.AnswerController#saveAnswer(org.asck.service.model.Answer)}.
	 */
	@Test
	public void testSaveAnswer_DateNotSpecified_ReturnBadRequest() throws Exception {

		mockMvc.perform(post("/v1/feedback/answers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(json.write(Answer.builder().optionId(2L).questionId(3L).build()).getJson())).andDo(print())
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.apierror.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.apierror.message", is("Validation error")))
				.andExpect(jsonPath("$.apierror.debugMessage", isEmptyOrNullString()))
				.andExpect(jsonPath("$.apierror.subErrors", Matchers.hasSize(1)))
				.andExpect(jsonPath("$.apierror.subErrors[0].object", is("answer")))
				.andExpect(jsonPath("$.apierror.subErrors[0].field", is("answeredAt")))
				.andExpect(jsonPath("$.apierror.subErrors[0].rejectedValue", nullValue()))
				.andExpect(jsonPath("$.apierror.subErrors[0].message", is("must not be null")));

	}

	/**
	 * Test method for {@link org.asck.AnswerRestController.AnswerController#getAllAnswersToQuestion(java.lang.Long)}.
	 */
	@Test
	public void testGetAllAnswersToQuestion_QuestionIdIsMissing_ReturnBadRequest() throws Exception {
		mockMvc.perform(get("/v1/feedback/answers")).andDo(print())
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.apierror.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.apierror.message", is("questionId parameter is missing")))
				.andExpect(jsonPath("$.apierror.debugMessage", is("Required Long parameter 'questionId' is not present")))
				.andExpect(jsonPath("$.apierror.subErrors", Matchers.nullValue()));
	}
	
	/**
	 * Test method for {@link org.asck.AnswerRestController.AnswerController#getAllAnswersToQuestion(java.lang.Long)}.
	 */
	@Test
	public void testGetAllAnswersToQuestion_QuestionIdIsEmpty_ReturnBadRequest() throws Exception {
		mockMvc.perform(get("/v1/feedback/answers?questionId")).andDo(print())
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.apierror.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.apierror.message", is("questionId parameter is missing")))
				.andExpect(jsonPath("$.apierror.debugMessage", is("Required Long parameter 'questionId' is not present")))
				.andExpect(jsonPath("$.apierror.subErrors", Matchers.nullValue()));
	}
	
	/**
	 * Test method for {@link org.asck.AnswerRestController.AnswerController#getAllAnswersToQuestion(java.lang.Long)}.
	 */
	@Test
	public void testGetAllAnswersToQuestion_QuestionIdHasWrongContent_ReturnBadRequest() throws Exception {
		mockMvc.perform(get("/v1/feedback/answers?questionId=hallo")).andDo(print())
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.apierror.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.apierror.message", is("Wrong type for parameter")))
				.andExpect(jsonPath("$.apierror.debugMessage", is("Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"hallo\"")))
				.andExpect(jsonPath("$.apierror.subErrors", Matchers.nullValue()));
	}
	
	/**
	 * Test method for {@link org.asck.AnswerRestController.AnswerController#getAllAnswersToQuestion(java.lang.Long)}.
	 */
	@Test
	public void testGetAllAnswersToQuestion_QuestionIdDoesntExist_ReturnBadRequest() throws Exception {
		when(feedbackServiceMock.getAllAnswersToQuestion(1L)).thenThrow(new EntityNotFoundException(QuestionTableModel.class, "id","1"));
		
		mockMvc.perform(get("/v1/feedback/answers?questionId=1")).andDo(print())
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.apierror.status", is("NOT_FOUND")))
				.andExpect(jsonPath("$.apierror.message", is("QuestionTableModel was not found for parameters {id=1}")))
				.andExpect(jsonPath("$.apierror.debugMessage", isEmptyOrNullString()))
				.andExpect(jsonPath("$.apierror.subErrors", Matchers.nullValue()));
		
		verify(feedbackServiceMock).getAllAnswersToQuestion(1L);
	}
	
	/**
	 * Test method for {@link org.asck.AnswerRestController.AnswerController#getAllAnswersToQuestion(java.lang.Long)}.
	 */
	@Test
	public void testGetAllAnswersToQuestion_QuestionIdSpecifiedButNotAnswersFound_ReturnsStatusNoContent() throws Exception {
		
		when(feedbackServiceMock.getAllAnswersToQuestion(1L)).thenReturn(new ArrayList<Answer>());
		
		mockMvc.perform(get("/v1/feedback/answers?questionId=1")).andDo(print())
				.andExpect(status().isNoContent());
		
		verify(feedbackServiceMock).getAllAnswersToQuestion(1L);
	}
	
	/**
	 * Test method for {@link org.asck.AnswerRestController.AnswerController#getAllAnswersToQuestion(java.lang.Long)}.
	 */
	@Test
	@Ignore("check why jsonListTester doesnt work")
	public void testGetAllAnswersToQuestion_QuestionIdSpecifiedAndAnswersExists_ReturnsStatusOkAndElementsOfList() throws Exception {
		
		Answer answer = Answer.builder().questionId(1L).optionId(2L).answeredAt(LocalDateTime.now()).build();
		
		when(feedbackServiceMock.getAllAnswersToQuestion(1L)).thenReturn(Arrays.asList(answer));
		
		mockMvc.perform(get("/v1/feedback/answers?questionId=1")).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(content().json(jsonList.write((Arrays.asList(answer))).getJson()));
		
		verify(feedbackServiceMock).getAllAnswersToQuestion(1L);
	}

}
