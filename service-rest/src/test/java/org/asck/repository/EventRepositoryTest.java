package org.asck.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.asck.repository.EventRepository;
import org.asck.repository.model.EventTableModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class EventRepositoryTest {

	@Autowired
	EventRepository underTest;
	
	@Test
	public void testFindAll() {
		List<EventTableModel> list = underTest.findAll();
		assertNotNull(list);
		assertEquals(2, list.size());
		assertTrue(list.contains(new EventTableModel(1L, "Tägliche Essen-Umfrage")));
	}
	
}
