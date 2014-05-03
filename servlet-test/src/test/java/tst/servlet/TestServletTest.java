package tst.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestServletTest {
	private static final String URL = "http://localhost:8080/servlet-test";
	private static final int THREADS = 100;
	private static final int REQUESTS = 100000;
	
	@Test
	public void testGet() throws Exception {
		sendRequest();
	}

	@Test
	public void testGetLoad() {
		
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);

		for (int i = 0; i < REQUESTS; i++) {
			
				executor.execute(new Runnable() {
					public void run() {
						try {
							sendRequest();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		}

		executor.shutdown();

		try {
		    executor.awaitTermination(15, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	private void sendRequest() throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
		
		StringBuffer response = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		reader.close();
		
		assertNotNull(response);
		assertTrue(response.length() > 0);
		System.out.println(getClass().getName() + ".testGet: "+ response);
	}
}
