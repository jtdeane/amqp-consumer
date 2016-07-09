package ws.cogito.magic.web;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


@Component
public class TrackingIdInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(TrackingIdInterceptor.class);
	
	public static final String TRACKING_ID = "trackingId";

	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		
		if (request.getHeader(TRACKING_ID) != null) {
			
			String tid = (String) request.getHeader(TRACKING_ID);
			
			logger.info("Existing Tracking ID: " + TRACKING_ID + "=" + tid + " ");
			
			response.setHeader(TRACKING_ID, tid);
			
		} else {
			
			String tid = UUID.randomUUID().toString();
			
			logger.info("Generated Tracking ID: " + TRACKING_ID + "=" + tid + " ");
			
			response.setHeader(TRACKING_ID, tid);
		}

		return super.preHandle(request, response, handler);
	}
}