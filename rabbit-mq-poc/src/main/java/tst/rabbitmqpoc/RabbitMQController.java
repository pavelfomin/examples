package tst.rabbitmqpoc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import tst.rabbitmqpoc.producer.RabbitMQProducer;

@Controller
@RequestMapping("/test/*")
public class RabbitMQController {

	@Autowired private RabbitMQProducer rabbitMQProducer;

	@RequestMapping()
	public String view(Model model) {

		return "view";
	}

	@RequestMapping()
	public void publish(HttpServletRequest request, HttpServletResponse response) throws Exception {

		byte[] payload = readRequestPayload(request);
		Map<String, Object> headers = getHeadersMap(request);
		rabbitMQProducer.publish(payload, headers);
	}

	private Map<String, Object> getHeadersMap(HttpServletRequest request) {

		Map<String, Object> headers = new HashMap<String, Object>();

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);
			headers.put(name, value);
		}

		return headers;
	}

	private byte[] readRequestPayload(HttpServletRequest request) throws IOException {

		ServletInputStream inputStream = null;

		byte[] payload = null;
		try {
			inputStream = request.getInputStream();
			payload = read(inputStream);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return payload;
	}

	private byte[] read(InputStream input) throws IOException {

		ByteArrayOutputStream output = null;

		try {
			byte[] buffer = new byte[1024];
			int readCount;
			output = new ByteArrayOutputStream();

			while ((readCount = input.read(buffer)) != -1) {
				output.write(buffer, 0, readCount);
			}
		} finally {
			if (output != null) {
				output.close();
			}
		}

		return output.toByteArray();
	}
}
