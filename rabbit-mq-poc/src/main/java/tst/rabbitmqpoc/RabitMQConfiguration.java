package tst.rabbitmqpoc;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class RabitMQConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(RabitMQConfiguration.class);
	
	@Value("${host}") private String host;
	@Value("${username}") protected String username;
	@Value("${password}") protected String password;

	@Value("${queue.name}") protected String queueName;
	@Value("${queue.durable}") private boolean queueDurable;

	@Value("${exchange.name}") protected String exchangeName;
	@Value("${exchange.durable}") private boolean exchangeDurable;
	@Value("${exchange.type}") private String exchangeType;

	@Value("${routing.key}") protected String routingKey;
	@Value("${binding.arguments}") private String bindingArguments;

	@Value("${transactional}") protected boolean transactional;
	@Value("${concurrency}") protected int concurrency;

	/**
	 * Creates connection factory.
	 * 
	 * @return connection factory
	 */
	@Bean
	public ConnectionFactory connectionFactory() {

		ConnectionFactory connectionFactory = createConnectionFactory(host);
		if (logger.isDebugEnabled()) {
			logger.debug("connectionFactory: connectionFactory=%s", connectionFactory);
		}
		return connectionFactory;
	}

	/**
	 * Creates admin configuration which is applied to the Rabbit MQ server.
	 * 
	 * @return admin configuration
	 */
	@Bean
	public AmqpAdmin amqpAdmin() {

		AmqpAdmin rabbitAdmin = createAMQPAdmin(connectionFactory());

		return rabbitAdmin;
	}

	protected ConnectionFactory createConnectionFactory(String host) {

		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
		if (logger.isDebugEnabled()) {
			logger.debug("createConnectionFactory: connectionFactory=%s", connectionFactory);
		}
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);

		return connectionFactory;
	}

	/**
	 * Creates admin configuration which is applied to the Rabbit MQ server.
	 * 
	 * @param connectionFactory
	 * @return admin configuration
	 */
	protected AmqpAdmin createAMQPAdmin(ConnectionFactory connectionFactory) {

		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

		// declare the exchange
		Exchange exchange = createExchange();
		rabbitAdmin.declareExchange(exchange);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("amqpAdmin: declared exchange=%s type=%s durable=%s", exchangeName, exchangeType, exchangeDurable));
		}

		// create the durable, not exclusive, not autodelete queue with specific name
		Queue queue = new Queue(queueName, queueDurable, false, false);
		rabbitAdmin.declareQueue(queue);
		if (logger.isInfoEnabled()) {
			logger.info("amqpAdmin: declared queue=%s durable=%s", queueName, queueDurable);
		}

		// set the headers matching arguments
		Map<String, Object> bindingArgumentsMap = createBindingArgumentsMap();

		// bind the queue to the exchange with the specific routing key
		Binding binding = new Binding(queueName, DestinationType.QUEUE, exchangeName, routingKey, bindingArgumentsMap);
		rabbitAdmin.declareBinding(binding);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("amqpAdmin: bound echange=%s with queue=%s using routingKey=%s bindingArguments=%s", exchangeName, queueName, routingKey,
					bindingArgumentsMap));
		}

		return rabbitAdmin;
	}

	/**
	 * Parses the binding arguments and returns binding arguments parsed as a Map.
	 * 
	 * @return binding arguments parsed as a Map
	 */
	private Map<String, Object> createBindingArgumentsMap() {

		Map<String, Object> bindingArgumentsMap = null;

		if (StringUtils.isNotEmpty(bindingArguments)) {
			bindingArgumentsMap = new HashMap<String, Object>();

			String[] tokens = bindingArguments.split(";");

			if (tokens == null || tokens.length == 0) {
				throw new IllegalArgumentException("Unable to parse binding arguments with value " + bindingArguments
						+ " using format key1=value2;key2=value2.");
			}

			for (String token : tokens) {
				String[] keyValuePair = token.split("=");
				if (keyValuePair == null || keyValuePair.length != 2) {
					throw new IllegalArgumentException("Unable to parse binding arguments component with value " + token
							+ " using format key1=value2;key2=value2.");

				}

				String key = keyValuePair[0];
				String value = keyValuePair[1];
				bindingArgumentsMap.put(key, value);
			}

		}

		return bindingArgumentsMap;
	}

	/**
	 * Creates the exchange of a particular type.
	 * 
	 * @return exchange of a particular type.
	 */
	private Exchange createExchange() {

		Exchange exchange = null;
		if (ExchangeTypes.DIRECT.equals(exchangeType)) {
			exchange = new DirectExchange(exchangeName, exchangeDurable, false);
		} else if (ExchangeTypes.HEADERS.equals(exchangeType)) {
			exchange = new HeadersExchange(exchangeName, exchangeDurable, false);
		} else if (ExchangeTypes.FANOUT.equals(exchangeType)) {
			exchange = new FanoutExchange(exchangeName, exchangeDurable, false);
		} else if (ExchangeTypes.TOPIC.equals(exchangeType)) {
			exchange = new TopicExchange(exchangeName, exchangeDurable, false);
		} else {
			throw new IllegalArgumentException(String.format("Exchange type=%s is not supported", exchangeType));
		}
		return exchange;
	}

}
