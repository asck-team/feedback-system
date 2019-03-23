package org.asck.web.controller;

import javax.validation.Valid;

import org.asck.web.exceptions.EmailExistsException;
import org.asck.web.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class LoginController extends AbstractController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		User userDto = new User();
	    model.addAttribute("user", userDto);
	    return "registration";
	}
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute("user") @Valid User accountDto, 
			  BindingResult result, WebRequest request, Errors errors) {
		
		accountDto.setPassword(bCryptPasswordEncoder.encode(accountDto.getPassword()));
		
	    if (!result.hasErrors()) {
	        try {
				createUserAccount(accountDto);
				return "redirect:/login";
			} catch (EmailExistsException e) {
				result.rejectValue("email", e.getMessage());
			}
	    }
		
		return "registration";
	}

	private User createUserAccount(@Valid User accountDto) throws EmailExistsException {
		User userByEmail = null;
		
		try {
			userByEmail = getFeedbackService().getUserByEmail(accountDto.getEmail());
		} catch (Exception e) {
			userByEmail = null;
		}
		
		
		if(userByEmail == null) {
			return getFeedbackService().saveUser(accountDto);
		} else {
			throw new EmailExistsException("Email already exists");
		}
	}
	
}
