package ws.cogito.magic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ws.cogito.magic.messaging.EventsMessageListener;
import ws.cogito.magic.messaging.OrdersMessageListener;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(AmqpConsumerProperties.class)
public class AmqpDeclarationsConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(AmqpDeclarationsConfiguration.class);
	
	@Autowired
	@Qualifier("amqpConsumerProperties")
	AmqpConsumerProperties properties;
	
    @Bean
    public ConnectionFactory connectionFactory() {
    	
    	CachingConnectionFactory connectionFactory = null;
    	
    	logger.info("Configuring Connection Factory");
    	
        connectionFactory = new CachingConnectionFactory(properties.getHost());
        connectionFactory.setUsername(properties.getUserName());
        connectionFactory.setPassword(properties.getUserPassword());
        connectionFactory.setVirtualHost(properties.getvHost());
    	
    	return connectionFactory;
    }
    
	/**
	 * In order for all the other configurations to take effect there must
	 * be at least one decleration by the rabbitAdmin
	 * @param connectionFactory
	 * @return RabbitAdmin
	 */
    @Bean
    public RabbitAdmin admin(ConnectionFactory connectionFactory) {
    	
    	logger.info("Kicking off Declarations");
    	
    	RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    	
    	/* 
    	 * *********************IMPORTANT********************************
    	 * 
    	 * None of the other declarations take effect unless at least one 
    	 * declaration is executed by RabbitAdmin.
    	 * 
    	 * *********************IMPORTANT******************************** 
    	 */
    	rabbitAdmin.declareExchange(directExchange());
    	
        return new RabbitAdmin(connectionFactory);
    }
    
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    	
    	RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    	
    	return rabbitTemplate;
    }    
    
    /* ORDERS CONFIGURATION */
    
	@Bean
	Queue ordersQueue() {
		return new Queue(properties.getOrdersQueue(), true);
	}

	@Bean
	DirectExchange directExchange() {
		return new DirectExchange(properties.getDirectExchange());
	}

	@Bean
	Binding ordersBinding(Queue ordersQueue, DirectExchange directExchange) {
		return BindingBuilder.bind(ordersQueue).to(directExchange).
				with(properties.getOrdersQueueBinding());
	}
 
    @Bean
    MessageListenerAdapter ordersMessageListener() throws Exception {

        return new MessageListenerAdapter(new OrdersMessageListener()) {{
            setDefaultListenerMethod("onMessage");
        }};
    }
    
	@Bean
	SimpleMessageListenerContainer ordersContainer(ConnectionFactory connectionFactory, 
			@Qualifier("ordersMessageListener") MessageListenerAdapter orderMessageListener) {
		
		SimpleMessageListenerContainer ordersContainer = 
				new SimpleMessageListenerContainer();
		
		ordersContainer.setConnectionFactory(connectionFactory);
		ordersContainer.setQueueNames(properties.getOrdersQueue());
		ordersContainer.setMessageListener(orderMessageListener);
		
		return ordersContainer;
	}
	
    /* EVENTS CONFIGURATION */
    
	@Bean
	Queue eventsQueue() {
		return new Queue(properties.getEventsQueue(), true);
	}

	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange(properties.getTopicExchange());
	}

	@Bean
	Binding eventsBinding(Queue eventsQueue, TopicExchange topicExchange) {
		return BindingBuilder.bind(eventsQueue).to(topicExchange).
				with(properties.getEventsQueueBinding());
	}
 
    @Bean
    MessageListenerAdapter eventsMessageListener() throws Exception {

        return new MessageListenerAdapter(new EventsMessageListener()) {{
            setDefaultListenerMethod("onMessage");
        }};
    }
    
	@Bean
	SimpleMessageListenerContainer eventsContainer(ConnectionFactory connectionFactory, 
			@Qualifier("eventsMessageListener") MessageListenerAdapter eventsMessageListener) {
		
		SimpleMessageListenerContainer eventsContainer = 
				new SimpleMessageListenerContainer();
		
		eventsContainer.setConnectionFactory(connectionFactory);
		eventsContainer.setQueueNames(properties.getEventsQueue());
		eventsContainer.setMessageListener(eventsMessageListener);
		
		return eventsContainer;
	} 	
}