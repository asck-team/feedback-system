package org.asck.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.asck.api.exceptions.EntityNotFoundException;
import org.asck.api.service.IFeedbackService;
import org.asck.api.service.model.Event;
import org.asck.api.service.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import lombok.AccessLevel;
import lombok.Getter;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "security.enabled=false")
@AutoConfigureMockMvc
@Getter(AccessLevel.PROTECTED)
@AutoConfigureJsonTesters
public class EventRestControllerTest {

	@MockBean
	IFeedbackService feedbackServiceMock;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	JacksonTester<Event> json;

	@Test
	public void testGetEvents_NoEventsExists_ReturnsHttpStatusNoContent() throws Exception {

		when(getFeedbackServiceMock().findEventsOwnedBy(1L)).thenReturn(new ArrayList<>());

		getMockMvc()
				.perform(MockMvcRequestBuilders.get("/v1/feedback/events/ownedBy/1")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		verify(getFeedbackServiceMock()).findEventsOwnedBy(1L);
	}

	/**
	 * Test method for
	 * {@link org.asck.api.controller.EventRestController#createEvent(org.asck.api.service.model.Event)} .
	 */
	@Test
	public void testCreateEvent() throws Exception {

		when(getFeedbackServiceMock().saveEvent(Event.builder().id(-1L).name("Event").ownedBy(1L).build())).thenReturn(20L);

		String jsonValue = json.write(Event.builder().id(-1L).name("Event").ownedBy(1L).build()).getJson();
		
		getMockMvc()
				.perform(post("/v1/feedback/events").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonValue))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.header().stringValues("location", "http://localhost/v1/feedback/events/20"));

		verify(getFeedbackServiceMock()).saveEvent(Event.builder().id(-1L).name("Event").ownedBy(1L).build());

	}
	
	@Test
	public void testCreateEvent_ProvideIdNull_CallServiceWithNegativeId() throws Exception {

		when(getFeedbackServiceMock().saveEvent(Event.builder().id(-1L).name("Event").ownedBy(1L).build())).thenReturn(20L);

		String jsonValue = json.write(Event.builder().id(null).name("Event").ownedBy(1L).build()).getJson();
		
		getMockMvc()
				.perform(post("/v1/feedback/events").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonValue))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.header().stringValues("location", "http://localhost/v1/feedback/events/20"));

		verify(getFeedbackServiceMock()).saveEvent(Event.builder().id(-1L).name("Event").ownedBy(1L).build());

	}

	/**
	 * Test method for
	 * {@link org.asck.api.controller.EventRestController#getEvent(long)} .
	 */
	@Test
	public void testGetEvent() throws Exception {
		when(getFeedbackServiceMock().findEventById(20L))
				.thenThrow(new EntityNotFoundException(Event.class, "id", "20"));

		getMockMvc().perform(get("/v1/feedback/events/20")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.apierror.status", is("NOT_FOUND")))
				.andExpect(jsonPath("$.apierror.message", is("Event was not found for parameters {id=20}")))
				.andExpect(jsonPath("$.apierror.debugMessage", isEmptyOrNullString()))
				.andExpect(jsonPath("$.apierror.subErrors", nullValue()));
	}

	/**
	 * Test method for {@link org.asck.api.controller.EventRestController#updateEvent(Long, Event)} .
	 */
	@Test
	public void testUpdateEvent() throws Exception {
		when(getFeedbackServiceMock().saveEvent(Event.builder().id(20L).name("Event").ownedBy(1L).build())).thenReturn(20L);

		String jsonValue = json.write(Event.builder().id(-1L).name("Event").ownedBy(1L).build()).getJson();
		
		getMockMvc()
				.perform(put("/v1/feedback/events/20").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonValue))
				.andDo(print()).andExpect(status().isNoContent());
		verify(getFeedbackServiceMock()).saveEvent(Event.builder().id(20L).name("Event").ownedBy(1L).build());
	}

	/**
	 * Test method for {@link org.asck.api.controller.EventRestController#deleteEvent(Long)}.
	 */
	@Test
	public void testDeleteEvent_EventDoesntExist_ReturnsNotFound() throws Exception {
		Mockito.doThrow(new EntityNotFoundException(Event.class, "id", "20")).when(getFeedbackServiceMock()).deleteEvent(20L);

		getMockMvc()
				.perform(delete("/v1/feedback/events/20"))
				.andDo(print()).andExpect(status().isNotFound());

		verify(getFeedbackServiceMock()).deleteEvent(20L);
	}
	
	/**
	 * Test method for {@link org.asck.api.controller.EventRestController#deleteEvent(Long)}.
	 */
	@Test
	public void testDeleteEvent_EventExistAndDeleted_ReturnsNoContent() throws Exception {

		getMockMvc()
				.perform(delete("/v1/feedback/events/20"))
				.andDo(print()).andExpect(status().isNoContent());

		verify(getFeedbackServiceMock()).deleteEvent(20L);
	}

}
