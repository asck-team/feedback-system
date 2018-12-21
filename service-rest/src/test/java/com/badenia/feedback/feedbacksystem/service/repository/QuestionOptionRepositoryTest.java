package com.badenia.feedback.feedbacksystem.service.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.badenia.feedback.feedbacksystem.service.repository.QuestionOptionRepository;
import com.badenia.feedback.feedbacksystem.service.repository.model.QuestionOptionTableModel;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class QuestionOptionRepositoryTest {

	@Autowired
	QuestionOptionRepository underTest;

	@Test
	public void testFindAll() {
		List<QuestionOptionTableModel> list = underTest.findAll();
		assertNotNull(list);
		assertEquals(11, list.size());
	}

}
