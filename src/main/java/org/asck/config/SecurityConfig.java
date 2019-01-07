package org.asck.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${security.enabled:true}")
    private boolean securityEnabled;
	
	@Autowired
	private UserDetailsService custom;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		if (securityEnabled) {
			auth.userDetailsService(custom).passwordEncoder(encoder());
		}
	}
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (securityEnabled) {
			http.csrf().disable()
            .authorizeRequests()
            .and()
            .exceptionHandling()
            .and()
            .authorizeRequests()
            .antMatchers("/img/**", "/css/**", "/webjars/**", "/signup", "swagger-ui.html" ,"/v1/feedback/**", "/answer")
			.permitAll().anyRequest().authenticated().and()
			.formLogin().loginPage("/login").permitAll().and()
			.logout().permitAll()
            .and()
            .httpBasic();
		} else {
			http.authorizeRequests().antMatchers("/**").permitAll().and().csrf().disable();
		}
	}

}
