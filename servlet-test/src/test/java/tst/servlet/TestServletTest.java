package tst.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class TestServletTest {
	private static final String URL = "http://localhost:8080/servlet-test";
//	private static final String URL = "http://localhost:8080/spring-controller-test";
//	private static final String URL = "http://localhost:8080/spring-controller-test-1.0.0";
//	private static final String URL = "http://localhost:8080/grails-test";
//	private static final String URL = "http://localhost:8080/grails-test-0.1";
//	private static final String URL = "http://localhost:3000/nodejs-test";
	private static final int THREADS = 100;
	private static final int REQUESTS = 2000;
	
	@Test
	public void testGet() throws Exception {
		sendRequest();
	}

	@Test
	public void testGetLoad() {
		
		final AtomicLong elapsedTime = new AtomicLong();
		final AtomicLong errorsCount = new AtomicLong();
		
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);

		for (int i = 0; i < REQUESTS; i++) {

			executor.execute(new Runnable() {
				public void run() {
					try {
						long elapsed = sendRequest();
						elapsedTime.addAndGet(elapsed);
					} catch (Exception e) {
						e.printStackTrace();
						errorsCount.addAndGet(1);
					}
				}
			});

			if (errorsCount.get() > 0) {
				break;
			}
		}

		executor.shutdown();

		try {
		    executor.awaitTermination(15, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (errorsCount.get() > 0) {
			fail("Processing errors occured: " + errorsCount);
		}

		System.out.println(String.format("Test %s.testGetLoad elapsed: %,d(ms) average response: %.3f(ms) threads: %,d requests per thread: %,d", 
				getClass().getName(), elapsedTime.get(), (double)elapsedTime.get() / (THREADS * REQUESTS), THREADS, REQUESTS));
	}
	
	
	private long sendRequest() throws Exception {
		long start = System.currentTimeMillis();
		
		HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
		
		StringBuffer response = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		reader.close();
		connection.disconnect();

		long elapsed = System.currentTimeMillis() - start;
		
		assertNotNull(response);
		assertTrue(response.length() > 0);
		System.out.println(String.format("Test %s received reponse: %s elapsed: %s", getClass().getName(), response.substring(0, Math.min(50, response.length())), elapsed));
		
		return elapsed;
	}
}
