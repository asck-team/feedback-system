/**
 * 
 */
package com.badenia.feedback.feedbacksystem.service.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author constantin
 *
 */
public class QuestionTypeDBTest {

	/**
	 * Test method for {@link com.badenia.feedback.feedbacksystem.service.model.QuestionType#getForDBId(long)}.
	 */
	@Test
	public void testGetForDBId() throws Exception {
		assertEquals(QuestionType.FREETEXT, QuestionType.getForDBId(1));
		assertEquals(QuestionType.THREE_SMILEYS, QuestionType.getForDBId(2));
		assertEquals(QuestionType.FIVE_SMILEYS, QuestionType.getForDBId(3));
		assertEquals(QuestionType.YES_NO, QuestionType.getForDBId(4));
	}

}
