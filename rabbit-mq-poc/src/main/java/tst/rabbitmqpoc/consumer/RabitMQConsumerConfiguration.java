package tst.rabbitmqpoc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tst.rabbitmqpoc.RabitMQConfiguration;

@Configuration
public class RabitMQConsumerConfiguration extends RabitMQConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(RabitMQConsumerConfiguration.class);

	private static final String RABBITMQ_POC_CONSUMER_FLAG = "rabbitmq.poc.consumer";

	@Value("${nodes}") private String nodes;

	@Bean
	public BeansProxy<SimpleMessageListenerContainer> listenerContainers() {

		if (!Boolean.valueOf(System.getProperty(RABBITMQ_POC_CONSUMER_FLAG))) {
			if (logger.isInfoEnabled()) {
				logger.info(String.format("listenerContainers: property=%s is not set and consumer listeners are disabled", RABBITMQ_POC_CONSUMER_FLAG));
			}
			return null;
		}

		String[] hosts = nodes.split(",");

		// SimpleMessageListenerContainer life cycle calls need to called for each instance
		// Returning the beans proxy is a way for spring container to make life cycle calls on all SimpleMessageListenerContainer instances
		BeansProxy<SimpleMessageListenerContainer> proxy = new BeansProxy<SimpleMessageListenerContainer>();

		for (String node : hosts) {
			SimpleMessageListenerContainer container = createListenerContainer(node);
			proxy.addBean(container);
		}

		return proxy;
	}

	private SimpleMessageListenerContainer createListenerContainer(String host) {

		ConnectionFactory connectionFactory = createConnectionFactory(host);
		createAMQPAdmin(connectionFactory);

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(new RabitMQConsumerMessageListener());
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		container.setChannelTransacted(transactional);
		container.setConcurrentConsumers(concurrency);

		if (logger.isInfoEnabled()) {
			logger.info(String.format("listenerContainer: registered message listener container=%s for host=%s with concurrency=%d queueName=%s transactional=%s", container,
					host, concurrency, queueName, transactional));
		}
		return container;
	}

}
