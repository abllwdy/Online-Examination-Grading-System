//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String sp = System.getProperty("port");
        String ep = System.getenv("PORT");
        int port = 8080;
        try {
            if (sp != null) port = Integer.parseInt(sp);
            else if (ep != null) port = Integer.parseInt(ep);
        } catch (NumberFormatException ignored) {}
        WebServer.start(port);
    }
}
