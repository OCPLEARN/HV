package de.ocplearn.hv.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/",
            "classpath:/signin"
            };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }	
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/signin").setViewName("/signin/signin");
        //registry.addViewController("/logout_page").setViewName("logout_page");
        //registry.addViewController("/logout").setViewName("logout");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }    
    
}
