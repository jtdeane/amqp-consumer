package ws.cogito.magic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ws.cogito.magic.web.TrackingIdInterceptor;

@Configuration
public class AmpqConsumerWebMvcConfigurerAdapter implements WebMvcConfigurer {
	
	@Autowired
	TrackingIdInterceptor trackingIdInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(trackingIdInterceptor);
	}
}