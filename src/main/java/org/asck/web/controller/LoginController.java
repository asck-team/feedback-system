package org.asck.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.web.exceptions.EmailExistsException;
import org.asck.web.service.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController extends AbstractController {
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private static final Logger LOGGER = LogManager.getLogger(LoginController.class);
	
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
			  BindingResult result, HttpServletRequest request, Errors errors) {
				
				bCryptPasswordEncoder = new BCryptPasswordEncoder();
				String pass = accountDto.getPassword();
				accountDto.setPassword(bCryptPasswordEncoder.encode(accountDto.getPassword()));
		
	    if (!result.hasErrors()) {
	        try {
				createUserAccount(accountDto);
				authWithHttpServletRequest(request, accountDto.getEmail(), pass);
				return "redirect:/";
			} catch (EmailExistsException e) {
				result.rejectValue("email", e.getMessage());
			}
	    }
		
		return "registration";
	}

	private User createUserAccount(@Valid User accountDto) {
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
	
	private void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            LOGGER.error("Error while login ", e);
        }
    }
	
}
