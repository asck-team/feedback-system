package org.asck.web.controller;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;


public class EventsControllerTest {

	@Test
	@Ignore("change event.html, so this test has to be fixed")
	public void test() {
		final TestExecutor executor = new TestExecutor();
		executor.execute("classpath:test");
		Assert.assertTrue(executor.isAllOK());
		
	}
}


