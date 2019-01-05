package org.asck.api.service.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QuestionTest {

	/**
	 * Test method for {@link org.asck.service.model.Question#isOrderSpecified()}.
	 */
	@Test
	public void testIsOrderSpecified() throws Exception {
		assertFalse(org.asck.api.service.model.Question.builder().build().isOrderSpecified());
		assertFalse(org.asck.api.service.model.Question.builder().order(-1).build().isOrderSpecified());
		assertFalse(org.asck.api.service.model.Question.builder().order(0).build().isOrderSpecified());
		assertTrue(org.asck.api.service.model.Question.builder().order(1).build().isOrderSpecified());
		assertTrue(org.asck.api.service.model.Question.builder().order(100).build().isOrderSpecified());
	}

	/**
	 * Test method for {@link org.asck.service.model.Question#isIdSpecified()}.
	 */
	@Test
	public void testIsIdSpecified() throws Exception {
		assertFalse(org.asck.api.service.model.Question.builder().build().isIdSpecified());
		assertFalse(org.asck.api.service.model.Question.builder().id(null).build().isIdSpecified());
		assertFalse(org.asck.api.service.model.Question.builder().id(-1L).build().isIdSpecified());
		assertTrue(org.asck.api.service.model.Question.builder().id(0L).build().isIdSpecified());
		assertTrue(org.asck.api.service.model.Question.builder().id(1L).build().isIdSpecified());
		assertTrue(org.asck.api.service.model.Question.builder().id(100L).build().isIdSpecified());
	}

}
