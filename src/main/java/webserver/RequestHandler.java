package webserver;

import controller.*;
import model.HttpRequest;
import model.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static enums.HttpMethod.GET;

public class RequestHandler implements Runnable{
    Socket connection;
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());
    private Controller controller = new ForwardController();

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        //log.log(Level.INFO, "New Client Connect! Connected IP : " + connection.getInetAddress() + ", Port : " + connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest httpRequest = HttpRequest.from(br);
            HttpResponse httpResponse = new HttpResponse(dos);

            String method = httpRequest.getMethod();
            URI uri = httpRequest.getURI();

            // 요구 사항 1번
            if (method.equals(GET.getMethod()) && uri.getPath().endsWith(".html")) {
                controller = new ForwardController();
            }
            if (uri.getPath().equals("/")) {
                controller = new HomeController();
            }
            // 요구 사항 2,3,4번
            if (uri.getPath().equals("/user/signup")) {
                controller = new SignUpController();
            }
            // 요구 사항 5번
            if (uri.getPath().equals("/user/login")) {
                controller = new LoginController();
            }
            // 요구 사항 6번
            if (uri.getPath().equals("/user/userList")) {
                controller = new ListController();
            }
            controller.execute(httpRequest, httpResponse);

        } catch (IOException | URISyntaxException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
    }
}