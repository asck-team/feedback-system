package org.asck.service.client.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.asck.service.client.model.Answer;
import org.junit.Test;

public class FeedbackClientServiceTest {
	
	private String serviceBasePath = "http://localhost:8080/v1/feedback";

	@Test
	public void testGetAllAnswersToQuestion() throws Exception {
//		List<Answer> allAnswersToQuestion = underTest().getAllAnswersToQuestion(2L);
//		assertNotNull(allAnswersToQuestion);
//		assertTrue(allAnswersToQuestion.size() > 0);
	}

    public FeedbackClientService underTest() {
    	return new FeedbackClientService(this.serviceBasePath);
    }

}
