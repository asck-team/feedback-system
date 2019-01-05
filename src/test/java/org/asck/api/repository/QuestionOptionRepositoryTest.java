package org.asck.api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.asck.api.repository.model.QuestionOptionTableModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
