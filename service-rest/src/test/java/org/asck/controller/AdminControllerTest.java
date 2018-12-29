/**
 * 
 */
package org.asck.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.asck.service.IFeedbackService;
import org.asck.service.model.Event;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author constantin
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@RunWith(SpringRunner.class)
@Getter(AccessLevel.PROTECTED)
public class AdminControllerTest {

	@MockBean
	IFeedbackService feedbackServiceMock;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	JacksonTester<Event> json;

	/**
	 * Test method for
	 * {@link org.asck.controller.AdminController#readQuestionTypes()}.
	 */
	@Test
	public void testReadQuestionTypes() throws Exception {
		getMockMvc()
				.perform(MockMvcRequestBuilders.get("/v1/feedback/admin/questionTypes")
						.accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[0]", is("FREETEXT"))).andExpect(jsonPath("$.[1]", is("THREE_SMILEYS")))
				.andExpect(jsonPath("$.[2]", is("FIVE_SMILEYS"))).andExpect(jsonPath("$.[3]", is("YES_NO")));

		verifyNoMoreInteractions(getFeedbackServiceMock());
	}

}
