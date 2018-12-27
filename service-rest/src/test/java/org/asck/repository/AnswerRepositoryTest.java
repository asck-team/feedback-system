package org.asck.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.asck.repository.AnswerRepository;
import org.asck.repository.model.AnswerTableModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	AnswerRepository underTest;
	
	@Test
	public void testFindAll() {
		List<AnswerTableModel> list = underTest.findAll();
		assertNotNull(list);
		assertEquals(0, list.size());
	}
	
}
