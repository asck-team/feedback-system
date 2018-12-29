/**
 * 
 */
package org.asck.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.asck.service.IFeedbackService;
import org.asck.service.model.Answer;
import org.hamcrest.Matchers;
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
public class AnswerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	IFeedbackService feedbackServiceMock;

	@Autowired
	JacksonTester<Answer> json;

	/**
	 * Test method for
	 * {@link org.asck.controller.AnswerController#saveAnswer(org.asck.service.model.Answer)}.
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

}
