package org.asck.api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.asck.api.repository.model.EventTableModel;
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
	public void testFindAllByOwnedBy() {
		List<EventTableModel> list = underTest.findAllByOwnedBy(1L);
		assertNotNull(list);
		assertEquals(0, list.size());
		// assertTrue(list.contains(new EventTableModel(1L, "Tägliche Essen-Umfrage", 1L)));

		List<EventTableModel> list2 = underTest.findAllByOwnedBy(2L);
		assertNotNull(list2);
		assertEquals(0, list2.size());
		// assertTrue(list2.contains(new EventTableModel(3L, "Freespace 2019", 2L)));
	}
	
}
