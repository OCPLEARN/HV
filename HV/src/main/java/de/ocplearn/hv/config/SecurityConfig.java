package de.ocplearn.hv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@ComponentScan
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired  
   private CustomAuthenticationProvider authProvider;  	
	
	 @Override
	  protected void configure(HttpSecurity http) throws Exception {
	      http
	      
	        .authorizeRequests()

	      	//.antMatchers("/**")
	        .antMatchers("/admin**").hasRole("ADMIN")
	        .antMatchers("/user**").hasRole("USER")
	        //.antMatchers("/", "/home", "/js/**", "/css/**").permitAll()
	        .antMatchers("/static/**","/login","/logout","/bootstrap/**" ,"/jquery/**", "/js/**", "/css/**").permitAll()
	      	//.antMatchers("/static/**","/login*","/logout*")
	      		//.permitAll()
	        .antMatchers("/public**").permitAll()
	        .anyRequest().authenticated()
	        	.and()
	        .formLogin().loginPage("/login")
	        	.permitAll()
	        	.and()
	        .logout()
	        	.logoutUrl("/logout_page")		        	
	        	.deleteCookies("JSESSIONID")
	        	.invalidateHttpSession(true)
	        	.permitAll()
	        ;
	      
	      
	  }

	  @Override
	  public void configure(AuthenticationManagerBuilder builder)
	          throws Exception {
	      //builder.authenticationProvider(new CustomAuthenticationProvider());
	      builder.authenticationProvider(authProvider);
	  }

//	  @Bean
//	  public ViewResolver viewResolver() {
//	      InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//	      viewResolver.setPrefix("/WEB-INF/views/");
//	      viewResolver.setSuffix(".jsp");
//	      return viewResolver;
//	  }
	
}
