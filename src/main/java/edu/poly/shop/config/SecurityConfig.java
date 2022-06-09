package edu.poly.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import edu.poly.shop.service.impl.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
        http.authorizeRequests()
        	.antMatchers( "/", "/css/**", "/images/**", "/site/**").permitAll()
        	.antMatchers("/admin/**/add/**", "/admin/**/edit/**", "/admin/**/delete/**", "/admin/**/saveOrUpdate", "/admin/**/list/**").hasRole("ADMIN")
        	.antMatchers("/admin/**/view/**", "/admin/**/search/**", "/changepassword/**").hasAnyRole("ADMIN", "USER", "AUTHOR")
        	.anyRequest().authenticated()
        	.and()
        		.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
        		.permitAll()
        	.and()
        		.logout()
        		.permitAll()
        	.and()
        		.exceptionHandling().accessDeniedPage("/403")
        	.and()
            	.rememberMe().tokenRepository(this.persistentTokenRepository())
            	.tokenValiditySeconds(1 * 24 * 60 * 60);
	}
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();
        return memory;
    }
}