package com.choongang.studyreservesystem.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
		httpSecurity
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login","/register","/help").anonymous()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/user/**").hasAnyRole("ADMIN","USER")
						.anyRequest().permitAll())
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/loginProc")
						.defaultSuccessUrl("/",true)
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/"))
				.csrf(auth -> auth.disable())
				.sessionManagement(session -> session
						.maximumSessions(1)
						.maxSessionsPreventsLogin(true))
				.sessionManagement(fix -> fix
						.sessionFixation().changeSessionId())
//				.httpBasic(Customizer.withDefaults())
				;
		return httpSecurity.build();
	}
    
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}
