package org.asck.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${security.enabled:true}")
    private boolean securityEnabled;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		if (securityEnabled) {
			auth.inMemoryAuthentication().withUser(
					User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
		}
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (securityEnabled) {
			http.authorizeRequests().antMatchers("/img/**", "/css/**", "/webjars/**", "/signup", "swagger-ui.html" ,"/v1/feedback/**", "/answer").permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();
		} else {
			http.authorizeRequests().antMatchers("/**").permitAll().and().csrf().disable();
		}
	}

}
