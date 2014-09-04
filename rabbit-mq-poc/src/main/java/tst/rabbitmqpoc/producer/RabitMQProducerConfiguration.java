package tst.rabbitmqpoc.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tst.rabbitmqpoc.RabitMQConfiguration;

@Configuration
public class RabitMQProducerConfiguration extends RabitMQConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(RabitMQProducerConfiguration.class);
	
	@Bean
	public RabbitTemplate rabbitTemplate() {

		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setChannelTransacted(transactional);
		template.setExchange(exchangeName);
		template.setRoutingKey(routingKey);

		if (logger.isInfoEnabled()) {
			logger.info(String.format("rabbitTemplate: created Rabbit Template for exchangeName=%s routingKey=%s transactional=true", exchangeName, routingKey, transactional));
		}

		return template;
	}

}
