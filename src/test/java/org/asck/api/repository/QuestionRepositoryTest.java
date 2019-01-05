package org.asck.api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.asck.api.repository.model.QuestionTableModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class QuestionRepositoryTest {

	@Autowired
	QuestionRepository underTest;
	
	@Test
	public void testFindAll() {
		List<QuestionTableModel> list = underTest.findAll();
		assertNotNull(list);
		assertEquals(9, list.size());
	}
	
}
