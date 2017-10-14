package ws.cogito.magic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="amqp")
public final class AmqpConsumerProperties {

	//connections
	@Value("${amqp.hostname:localhost}")
	private String host;
	
	private String vHost;
	private String userName;
	private String userPassword;
	
	//exchanges
	private String directExchange;	
	private String topicExchange;	
	
	//queues
	private String ordersQueue;
	private String eventsQueue;
	
	//bindings
	private String ordersQueueBinding;
	private String eventsQueueBinding;
	
	//routing
	private String priorityRoutingKey;
	private String vipRoutingKey;

	public String getVipRoutingKey() {
		return vipRoutingKey;
	}
	public void setVipRoutingKey(String vipRoutingKey) {
		this.vipRoutingKey = vipRoutingKey;
	}
	public String getPriorityRoutingKey() {
		return priorityRoutingKey;
	}
	public void setPriorityRoutingKey(String priorityRoutingKey) {
		this.priorityRoutingKey = priorityRoutingKey;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getvHost() {
		return vHost;
	}
	public void setvHost(String vHost) {
		this.vHost = vHost;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getDirectExchange() {
		return directExchange;
	}
	public void setDirectExchange(String directExchange) {
		this.directExchange = directExchange;
	}
	public String getTopicExchange() {
		return topicExchange;
	}
	public void setTopicExchange(String topicExchange) {
		this.topicExchange = topicExchange;
	}
	public String getOrdersQueue() {
		return ordersQueue;
	}
	public void setOrdersQueue(String ordersQueue) {
		this.ordersQueue = ordersQueue;
	}
	public String getEventsQueue() {
		return eventsQueue;
	}
	public void setEventsQueue(String eventsQueue) {
		this.eventsQueue = eventsQueue;
	}
	public String getOrdersQueueBinding() {
		return ordersQueueBinding;
	}
	public void setOrdersQueueBinding(String ordersQueueBinding) {
		this.ordersQueueBinding = ordersQueueBinding;
	}
	public String getEventsQueueBinding() {
		return eventsQueueBinding;
	}
	public void setEventsQueueBinding(String eventsQueueBinding) {
		this.eventsQueueBinding = eventsQueueBinding;
	}
}