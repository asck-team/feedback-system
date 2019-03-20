package org.asck.web.service.impl;

import java.util.List;

import org.asck.web.service.IFeedbackClientService;
import org.asck.web.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.Getter;

@Service
@Getter(AccessLevel.PROTECTED)
class CustomUserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired private IFeedbackClientService feedbackService;
	
	public CustomUserDetailsServiceImpl() {
		//Empty Constructor
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = getFeedbackService().getUserByEmail(email);

		if (user == null) {
			return null;
		}
		List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(User.ROLE);

		String password = user.getPassword();

		return new org.springframework.security.core.userdetails.User(email, password, auth);
	}
}