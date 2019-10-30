package org.asck.uiTests;

import org.asck.api.repository.EventRepository;
import org.asck.api.repository.model.EventTableModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DbConfigPostgresContainers.class})
@ActiveProfiles("DaoTest")
public class TestDao {


        @Autowired
        private EventRepository eventRepository;


        @Test
        @Transactional
        public void contextLoads() {

            eventRepository.save(EventTableModel.builder().id(-1L).name("Event1").ownedBy(1L).build());
            eventRepository.save(EventTableModel.builder().id(-1L).name("Event2").ownedBy(1L).build());
            eventRepository.save(EventTableModel.builder().id(-1L).name("Event3").ownedBy(1L).build());

            List<EventTableModel> allEvents = eventRepository.findAll();
            Assert.assertTrue(allEvents != null);
            assertEquals(3, allEvents.size());
            assertEquals(5, allEvents.get(0).getId().longValue());
            assertEquals("Event1", allEvents.get(0).getName());
        }
}
