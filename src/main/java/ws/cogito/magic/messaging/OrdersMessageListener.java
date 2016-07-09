package ws.cogito.magic.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import ws.cogito.magic.MessageLogging;

public class OrdersMessageListener implements MessageListener, MessageLogging {
	
	private static final Logger logger = LoggerFactory.getLogger(OrdersMessageListener.class);

	@Override
	public void onMessage(Message message) {
		
		logMessage(message, logger);
	}
}