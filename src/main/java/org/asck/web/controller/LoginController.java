package org.asck.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController extends AbstractController {

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		return "registration";
	}
	
}
