package org.asck.controller;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;


public class EventsControllerTest {

	@Test
	public void test() {
		final TestExecutor executor = new TestExecutor();
		executor.execute("classpath:test");
		Assert.assertTrue(executor.isAllOK());
		
	}
}


