package org.asck.uiTests;

import org.asck.api.repository.EventRepository;
import org.asck.api.repository.UserRepository;
import org.asck.api.repository.model.EventTableModel;
import org.asck.api.repository.model.UserTableModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DbConfigPostgresContainers.class})
@ActiveProfiles("DaoTest")
public class TestDao {


    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    @Transactional
    public void addEvents() {

        eventRepository.deleteAll();

        for (int i=1; i<=1000; i++){
            eventRepository.save(EventTableModel.builder().id(-1L).name("Event" + i).ownedBy(1L).build());
        }

        List<EventTableModel> allEvents = eventRepository.findAll();
        assertTrue(allEvents != null);
        assertEquals(1000, allEvents.size());
        assertEquals(5, allEvents.get(0).getId().longValue());
        assertEquals("Event1", allEvents.get(0).getName());

        int expectedId = 5;
        for (EventTableModel e : allEvents) {
            System.out.println(e.toString());
            assertEquals(expectedId,e.getId().longValue());
            expectedId++;
        }
    }

    @Test
    @Transactional
    public void addUsers() {

        // delete from test data from csv
        userRepository.deleteAll();

        for (int i=1; i<=1000; i++){
            userRepository.save(UserTableModel.builder().email("email" + i).id(-1L).password("pass").build());
        }


        Iterable<UserTableModel> all = userRepository.findAll();

        int expectedId = 5;
        for (UserTableModel u : all) {
            System.out.println(u.toString());
            assertEquals(expectedId,u.getId().longValue());
            expectedId++;
        }

    }
}
