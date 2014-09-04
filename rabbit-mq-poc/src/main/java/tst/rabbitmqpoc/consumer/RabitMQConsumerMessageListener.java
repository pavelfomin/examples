package tst.rabbitmqpoc.consumer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class RabitMQConsumerMessageListener implements MessageListener {
	private static final Logger logger = LoggerFactory.getLogger(RabitMQConsumerMessageListener.class);
	
	private static Map<String, Object> headersToIgnore = new HashMap<String, Object>();
	static {
		headersToIgnore.put("content-length", true);
	}

	public RabitMQConsumerMessageListener() {
	}

	@Override
	public void onMessage(Message message) {

		//byte[] payload = message.getBody();
		Map<String, Object> headers = message.getMessageProperties().getHeaders();
		if (logger.isInfoEnabled()) {
			logger.info(String.format("onMessage: message=%s headers=%s", message, headers));
		}
	}

}
