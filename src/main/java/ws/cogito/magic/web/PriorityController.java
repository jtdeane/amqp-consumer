package ws.cogito.magic.web;

import static ws.cogito.magic.web.TrackingIdInterceptor.TRACKING_ID;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ws.cogito.magic.AmqpConsumerProperties;

@RestController
@RequestMapping("/priority")
public class PriorityController {
	
	@Autowired
	@Qualifier("amqpConsumerProperties")
	AmqpConsumerProperties properties;	
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(PriorityController.class);
	
	@RequestMapping(value = "orders", method=RequestMethod.POST, 
			consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String processOrder (@RequestBody String payload,
			HttpServletResponse response) throws Exception { 
		
		String orderConfirmation = null;
		
		logger.info("Processing Priority Message...");
		
		Message orderMessage = MessageBuilder.withBody(payload.getBytes())
				.setContentType(MessageProperties.CONTENT_TYPE_JSON)
				.setContentEncoding(StandardCharsets.UTF_8.name())
				.setHeader(TRACKING_ID, response.getHeader(TRACKING_ID))
				.build();
		
		Message confirmationMessage = rabbitTemplate.sendAndReceive
				(properties.getPriorityRoutingKey(), orderMessage);
		
		orderConfirmation = new String(confirmationMessage.getBody(), StandardCharsets.UTF_8.name());
		
		logger.info("Processed and Returning Message");
		
		return orderConfirmation;
	}
	
	@RequestMapping(value = "vip/orders", method=RequestMethod.POST, 
			consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String processOrderVip (@RequestBody String payload,
			HttpServletResponse response) throws Exception { 
		
		String orderConfirmation = "Forwarded directly to sales...";
		
		logger.info("Processing VIP Message...");
		
		//not replyTo to magic.sales.orders
		Message orderMessage = MessageBuilder.withBody(payload.getBytes())
				.setContentType(MessageProperties.CONTENT_TYPE_JSON)
				.setContentEncoding(StandardCharsets.UTF_8.name())
				.setCorrelationId(UUID.randomUUID().toString().getBytes())
				.setReplyTo(properties.getVipRoutingKey())
				.setHeader(TRACKING_ID, response.getHeader(TRACKING_ID))
				.build();
		
		rabbitTemplate.send(properties.getDirectExchange(), 
				properties.getPriorityRoutingKey(), orderMessage);
		
		logger.info("Processed and VIP Returning Message");
		
		return orderConfirmation;
	}	
}