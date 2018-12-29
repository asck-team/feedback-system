package org.asck.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.asck.controller.MainController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class MainControllerTest {
	
	 private MockMvc mockMvc;
	 private MainController index;

	 @Before
	 public void setup() {
		 this.index = new MainController();
		 this.mockMvc = MockMvcBuilders.standaloneSetup(index).build();
	 }


	@Test
	public void testIndex() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk()).
		andExpect(view().name("index"));
	}

}
