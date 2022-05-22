package servers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
	public static final int PORT = 8078;
	private final String API_KEY;
	private final HttpServer server;
	private final Map<String, String> dataMap = new HashMap<>();

	public KVServer() throws IOException {
		API_KEY = generateApiKey();
		server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
		server.createContext("/register", (h) -> {
			try {
				System.out.println("\n/register");
				switch (h.getRequestMethod()) {
					case "GET":
						sendText(h, API_KEY);
						break;
					default:
						System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
						h.sendResponseHeaders(405, 0);
				}
			} finally {
				h.close();
			}
		});
		server.createContext("/save", (h) -> {
			try {
				System.out.println("\n/save");
				if (!hasAuth(h)) {
					System.out.println("Запрос неавторизован, нужен параметр в query API_KEY со значением апи-ключа");
					h.sendResponseHeaders(403, 0);
					return;
				}
				switch (h.getRequestMethod()) {
					case "POST":
						String key = h.getRequestURI().getPath().substring("/save/".length());
						if (key.isEmpty()) {
							System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
							h.sendResponseHeaders(400, 0);
							return;
						}
						String value = readText(h);
						if (value.isEmpty()) {
							System.out.println("Value для сохранения пустой. value указывается в теле запроса");
							h.sendResponseHeaders(400, 0);
							return;
						}
						dataMap.put(key, value);
						System.out.println("Значение для ключа " + key + " успешно обновлено!");
						h.sendResponseHeaders(200, 0);
						break;
					default:
						System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
						h.sendResponseHeaders(405, 0);
				}
			} finally {
				h.close();
			}
		});
		server.createContext("/load", (h) -> {

			try {
				String key = h.getRequestURI().getPath().substring("/load/".length());
				String tasksList = dataMap.get(key);
				byte[] tasksListBytes = tasksList.getBytes(StandardCharsets.UTF_8);
				h.getResponseHeaders().add("Content-Type", "application/json");
				h.sendResponseHeaders(200, tasksListBytes.length);
				h.getResponseBody().write(tasksListBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}h.close();
		});
	}

	public void start() {
		System.out.println("Запускаем сервер на порту " + PORT);
		System.out.println("Открой в браузере http://localhost:" + PORT + "/");
		System.out.println("API_KEY: " + API_KEY);
		server.start();
	}

	private String generateApiKey() {
		return "" + System.currentTimeMillis();
	}

	protected boolean hasAuth(HttpExchange h) {
		String rawQuery = h.getRequestURI().getRawQuery();
		return rawQuery != null && (rawQuery.contains("API_KEY=" + API_KEY) || rawQuery.contains("API_KEY=DEBUG"));
	}

	protected String readText(HttpExchange h) throws IOException {
		return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
	}

	protected void sendText(HttpExchange h, String text) throws IOException {
		byte[] resp = text.getBytes(StandardCharsets.UTF_8);
		h.getResponseHeaders().add("Content-Type", "application/json");
		h.sendResponseHeaders(200, resp.length);
		h.getResponseBody().write(resp);
	}

	public void stop() {server.stop(1);
	}
}
