package org.asck.web.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.asck.web.service.IFeedbackClientService;
import org.asck.web.service.model.Answer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import lombok.AccessLevel;
import lombok.Getter;

//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest(properties = "security.enabled=false")
@Getter(AccessLevel.PROTECTED)
public class FeedbackClientServiceImplTest {
	
	@Autowired
	MockMvc mockMvc;

	@MockBean
	IFeedbackClientService feedbackClientServiceMock;
	
	@Autowired
	JacksonTester<Answer> json;

	@Test
	public void testGetAllAnswersToEventId() throws Exception {
		
		Answer saveAnswer = new Answer(1L, 2L, "remark", LocalDateTime.now());
//		when(feedbackClientServiceMock.saveAnswer(saveAnswer)).thenReturn(saveAnswer);
//		
//		mockMvc.perform(post("/v1/feedback/answers")
//				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//				.content(json.write(Answer.builder().optionId(2L).questionId(3L).build()).getJson())).andDo(print())
//				.andExpect(status().isOk());
//		
//		verify(feedbackClientServiceMock).saveAnswer(saveAnswer);
		
		
//		assertNotNull(allAnswersToQuestion);
//		assertFalse(allAnswersToQuestion.isEmpty());
//		assertEquals(1, allAnswersToQuestion.size());
//		System.out.println(allAnswersToQuestion.toString());
	}

	

}
