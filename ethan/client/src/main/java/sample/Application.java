package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.Type;

import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.SockJsClient;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompFrameHandler;



/*
 * WebSocket client application. Performs client side setup and sends
 * messages.
 *
 * @Author Jay Sridhar
 */
public class Application {
	static public class MyStompSessionHandler extends StompSessionHandlerAdapter {
		private String userId;

		public MyStompSessionHandler(String userId) {
			this.userId = userId;
		}

		private void showHeaders(StompHeaders headers) {
			for (Map.Entry<String, List<String>> e : headers.entrySet()) {
				System.err.print("  " + e.getKey() + ": ");
				boolean first = true;
				for (String v : e.getValue()) {
					if (!first)
						System.err.print(", ");
					System.err.print(v);
					first = false;
				}
				System.err.println();
			}
		}

		private void sendJsonMessage(StompSession session) {
			User user = new User();
			user.setName("Toby");
			user.setPass("Flenderson");
			session.send("/app/bfg/ood", user);
			// TODO
			System.out.println("Sent welcome note!");
			
//			ClientMessage msg = new ClientMessage(userId, "hello from spring");
			// session.send("/app/chat/java", msg);
		}

		private void subscribeTopic(String topic, StompSession session) {
			session.subscribe(topic, new StompFrameHandler() {

				@Override
				public Type getPayloadType(StompHeaders headers) {
					return User.class;
				}

				@Override
				public void handleFrame(StompHeaders headers, Object payload) {
					System.err.println(payload.toString());
				}
			});
		}

		@Override
		public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
			System.err.println("Connected! Headers:");
			showHeaders(connectedHeaders);

			subscribeTopic("/topic/response", session);
			sendJsonMessage(session);
		}
	}

	public static void main(String args[]) throws Exception {
		
		/* Create a SockJS client -- fallback for WebSockets */
		WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport(simpleWebSocketClient));
		SockJsClient sockJsClient = new SockJsClient(transports);
		
		/* The SockJSClient is used to create a STOMP client which supports the messaging protocol used */
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		
		/* Map JSOn to POJO & back */
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		// "ws://localhost:9090/chat"
		// ws://proj-309-vc-2.cs.iastate.edu:8080/chat
		String url = "ws://localhost:8080/bfg";
		String userId = "spring-" + ThreadLocalRandom.current().nextInt(1, 99);
		
		StompSessionHandler sessionHandler = new MyStompSessionHandler(userId);
		
		StompSession session = stompClient.connect(url, sessionHandler).get();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		TimeUnit.SECONDS.sleep(1);
		
		for (;;) {
			System.out.print(userId + " add a user!" + " >> ");
			System.out.flush();
			
			User user = new User();
			
			String line = in.readLine();
			if (line == null)
				break;
			if (line.length() == 0)
				continue;
			user.setName(line);
			
			line = in.readLine();
			if (line == null)
				break;
			if (line.length() == 0)
				continue;
			user.setPass(line);
			System.out.println("username: " + user.getName() + ",  password: " + user.getPass());
			session.send("/app/bfg/ood", user);
			session.send("/app/bfg/all", null);
		}
	}
}
