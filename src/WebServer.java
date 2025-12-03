import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebServer {
    private static final Map<String, QuestionNavigator> sessions = new ConcurrentHashMap<>();

    public static void start(int port) {
        int[] candidates = new int[]{port, port + 1, port + 2, port + 3, 0};
        IOException last = null;
        for (int p : candidates) {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(p), 0);
                server.createContext("/", new StaticHandler("public/index.html", "text/html"));
                server.createContext("/app.js", new StaticHandler("public/app.js", "application/javascript"));
                server.createContext("/styles.css", new StaticHandler("public/styles.css", "text/css"));
                server.createContext("/api/init", WebServer::handleInit);
                server.createContext("/api/state", WebServer::handleState);
                server.createContext("/api/next", WebServer::handleNext);
                server.createContext("/api/previous", WebServer::handlePrevious);
                server.setExecutor(null);
                server.start();
                int actual = server.getAddress().getPort();
                System.out.println("[SUCCESS] Server started at http://localhost:" + actual + "/");
                return;
            } catch (IOException e) {
                last = e;
            }
        }
        System.out.println("[ERROR] Failed to start server: " + (last != null ? last.getMessage() : "unknown"));
    }

    private static void handleInit(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) { send(exchange, 405, ""); return; }
        String sid = sessionId(exchange);
        QuestionNavigator nav = new ListQuestionNavigator(sampleQuestions());
        sessions.put(sid, nav);
        send(exchange, 200, jsonState(nav));
    }

    private static void handleState(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) { send(exchange, 405, ""); return; }
        QuestionNavigator nav = sessions.computeIfAbsent(sessionId(exchange), k -> new ListQuestionNavigator(sampleQuestions()));
        send(exchange, 200, jsonState(nav));
    }

    private static void handleNext(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) { send(exchange, 405, ""); return; }
        QuestionNavigator nav = sessions.computeIfAbsent(sessionId(exchange), k -> new ListQuestionNavigator(sampleQuestions()));
        String before = nav.currentQuestion();
        String after = nav.next();
        String status = before.equals(after) ? "[WARN] Already at last question" : "[SUCCESS] Moved to next question";
        send(exchange, 200, jsonState(nav, status));
    }

    private static void handlePrevious(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) { send(exchange, 405, ""); return; }
        QuestionNavigator nav = sessions.computeIfAbsent(sessionId(exchange), k -> new ListQuestionNavigator(sampleQuestions()));
        String before = nav.currentQuestion();
        String after = nav.previous();
        String status = before.equals(after) ? "[WARN] Already at first question" : "[SUCCESS] Moved to previous question";
        send(exchange, 200, jsonState(nav, status));
    }

    private static String jsonState(QuestionNavigator nav) {
        return jsonState(nav, "");
    }

    private static String jsonState(QuestionNavigator nav, String status) {
        return "{\"index\":" + nav.currentIndex() + "," +
                "\"total\":" + nav.size() + "," +
                "\"question\":\"" + escape(nav.currentQuestion()) + "\"," +
                "\"hasNext\":" + nav.hasNext() + "," +
                "\"hasPrevious\":" + nav.hasPrevious() + "," +
                "\"status\":\"" + escape(status) + "\"}";
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static List<String> sampleQuestions() {
        return Arrays.asList(
                "1) What is 2 + 2?",
                "2) Define polymorphism.",
                "3) Name the capital of France.",
                "4) Explain HTTP status codes.",
                "5) Write a Java loop example.",
                "6) Describe encapsulation in object-oriented programming.",
                "7) What is Big-O time complexity?",
                "8) Explain the difference between TCP and UDP.",
                "9) What does SQL stand for?",
                "10) Write an example of a for-each loop in Java.",
                "11) What is a RESTful API?",
                "12) Explain inheritance with a simple example.",
                "13) What is the purpose of an interface in Java?",
                "14) Name three HTTP methods and their typical use.",
                "15) What is the capital of Japan?",
                "16) Explain the Model-View-Controller (MVC) pattern.",
                "17) What is a unit test and why is it useful?",
                "18) Describe the difference between stack and heap.",
                "19) What is a lambda expression in Java?",
                "20) Explain the concept of immutability."
        );
    }

    private static void send(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Content-Type", contentType(ex, body));
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    private static String contentType(HttpExchange ex, String body) {
        String path = ex.getRequestURI().getPath();
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".html") || "/".equals(path)) return "text/html";
        return body.startsWith("{") ? "application/json" : "text/plain";
    }

    private static String sessionId(HttpExchange ex) {
        String addr = ex.getRemoteAddress().toString();
        return addr;
    }

    private static Path findFile(String relative) {
        Path wd = Path.of(System.getProperty("user.dir"));
        for (int up = 0; up < 4; up++) {
            Path candidate = wd;
            for (int i = 0; i < up; i++) candidate = candidate.getParent() == null ? candidate : candidate.getParent();
            Path p = candidate.resolve(relative);
            if (Files.exists(p)) return p;
        }
        return Path.of(System.getProperty("user.dir"), relative);
    }

    private static class StaticHandler implements HttpHandler {
        private final String file;
        private final String type;
        StaticHandler(String file, String type) { this.file = file; this.type = type; }
        public void handle(HttpExchange exchange) throws IOException {
            Path p = findFile(file);
            if (Files.exists(p)) {
                byte[] b = Files.readAllBytes(p);
                exchange.getResponseHeaders().set("Content-Type", type);
                exchange.sendResponseHeaders(200, b.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(b); }
            } else {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
            }
        }
    }
}
