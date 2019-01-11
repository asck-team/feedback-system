package org.asck.web.service.impl;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;

import java.net.URI;
import java.time.LocalDateTime;

import org.asck.web.service.model.Answer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

@RunWith(SpringRunner.class)
@RestClientTest(FeedbackClientServiceImpl.class)
@AutoConfigureJsonTesters
public class FeedbackClientServiceImplTest {
	
	@Autowired
    private FeedbackClientServiceImpl client;
 
    @Autowired
    private MockRestServiceServer server;
 
    @Autowired
    JacksonTester<Answer> json;
	

	@Test
	public void testGetAllAnswersToEventId() throws Exception {
		Answer saveAnswer = new Answer(1L, 2L, "remark", LocalDateTime.now());
		String jsonString = json.write(saveAnswer).getJson(); 
		
		this.server.expect(requestTo("http://localhost:8080/v1/feedback/answers")).andExpect(method(HttpMethod.POST)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(content().string(jsonString)).andRespond(withCreatedEntity(new URI("http://localhost/v1/feedback/answers/1")));
		
		
		client.saveAnswer(saveAnswer);
		
		
	}

	

}
