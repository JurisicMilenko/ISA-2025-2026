package projekat.ISA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import projekat.ISA.Config.RateLimitFilter;

@SpringBootApplication
public class IsaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsaApplication.class, args);
	}
	
	@Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitingFilter() {
        FilterRegistrationBean<RateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitFilter());
        registration.addUrlPatterns("/auth/signup");
        return registration;
    }
}
