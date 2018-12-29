package org.asck.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.asck.repository.QuestionTypeRepository;
import org.asck.repository.model.QuestionTypeTableModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class QuestionTypeRepositoryTest {

	@Autowired
	QuestionTypeRepository underTest;

	@Test
	public void testFindAll() {
		List<QuestionTypeTableModel> list = underTest.findAll();
		assertNotNull(list);
		assertEquals(4, list.size());
		assertTrue(list.contains(new QuestionTypeTableModel(1L,"Freitext")));
		assertTrue(list.contains(new QuestionTypeTableModel(2L,"Drei Smileys")));
		assertTrue(list.contains(new QuestionTypeTableModel(3L,"5 Smileys")));
		assertTrue(list.contains(new QuestionTypeTableModel(4L,"Ja/Nein")));
	}

}
