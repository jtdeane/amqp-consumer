package ws.cogito.magic;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.amqp.core.Message;

public interface MessageLogging {
	
	static final String trackingId = "trackingId";
	
	default public void logMessage (Message message, Logger logger) {
		
		String tid = null;
		
		//log tracking id if it exists
		if (message.getMessageProperties().getHeaders().containsKey(trackingId)) {
			
			tid = (String)message.getMessageProperties().getHeaders().get(trackingId);
			
			logger.info("Existing Tracking ID: " + trackingId + "=" + tid + " ");
		
		} else {
			
			//create new tracking id
			tid = UUID.randomUUID().toString();
			
			logger.info("Generated Tracking ID: " + trackingId + "=" + tid + " ");
		}
		
		//log the correlation ID if it exists
		
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (correlationId != null) {
		    logger.info("Message Correlation ID: " + correlationId);
		}
		
		//log the message body
		String body = null;
		
		try {
		
			body = new String(message.getBody(), StandardCharsets.UTF_8.name());
		
		} catch (UnsupportedEncodingException e) {
			
			logger.error("Unable to parse message body /n" + e.getMessage());
		}
		
		logger.info("Received Message: \n" + body);
	}
}