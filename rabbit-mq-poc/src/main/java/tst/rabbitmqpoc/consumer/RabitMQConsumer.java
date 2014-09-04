package tst.rabbitmqpoc.consumer;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class RabitMQConsumer {

	public static void main(String[] args) throws Exception {

		AbstractApplicationContext context = new FileSystemXmlApplicationContext("classpath:consumer-context.xml");
		context.registerShutdownHook();
	}

}
