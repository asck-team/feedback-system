package org.asck.api.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.asck.api.service.IFeedbackService;
import org.asck.api.service.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class UserRestControllerTest {
	
	@MockBean
	IFeedbackService feedbackServiceMock;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	JacksonTester<User> json;

	@Test
	public void testgetUserByEmail() throws Exception {
		
		String email = "user@name.de";
		when(getFeedbackServiceMock().getUserByEmail(email)).thenReturn(new User(1L, email, "pass"));

		getMockMvc()
				.perform(MockMvcRequestBuilders.get("/v1/feedback/user/" + email + "/")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(getFeedbackServiceMock()).getUserByEmail(email);
	}

	@Test
	public void testCreateUser() throws Exception {
		String email = "user@name.de";
		
		when(getFeedbackServiceMock().saveUser(User.builder().id(-1L).email(email).password("pass").build())).thenReturn(1L);

		String jsonValue = json.write(User.builder().id(-1L).email(email).password("pass").build()).getJson();
		
		getMockMvc()
				.perform(post("/v1/feedback/user").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonValue))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.header().stringValues("location", "http://localhost/v1/feedback/user/1"));

		verify(getFeedbackServiceMock()).saveUser(User.builder().id(-1L).email(email).password("pass").build());
	}

}
