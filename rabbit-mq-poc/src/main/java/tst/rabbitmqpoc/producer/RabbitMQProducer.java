package tst.rabbitmqpoc.producer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducer {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducer.class);
	
	@Autowired private RabbitTemplate template;

	public void publish(byte[] payload, Map<String, Object> headers) throws Exception {

		MessageProperties messageProperties = new MessageProperties();

		// set headers
		for (String key : headers.keySet()) {
			messageProperties.setHeader(key, headers.get(key));
		}

		// set persistent flag
		messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

		Message message = new Message(payload, messageProperties);

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("publish: message=%s", message));
		}

		template.send(message);
	}

}
