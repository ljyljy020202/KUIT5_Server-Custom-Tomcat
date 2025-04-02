package webserver;

import http.util.HttpRequestUtils;
import http.util.HttpResponseUtils;
import http.util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static enums.HttpMethod.GET;
import static enums.HttpMethod.POST;
import static enums.URL.*;

public class RequestHandler implements Runnable{
    Socket connection;
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        //log.log(Level.INFO, "New Client Connect! Connected IP : " + connection.getInetAddress() + ", Port : " + connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            // 요청 메세지 읽기
            String requestLine = br.readLine();
            log.info("Request: " + requestLine);

            // 헤더 읽기
            Map<String, String> headers = IOUtils.readHeaders(br);
            log.info("Headers: " + headers);

            // 바디 읽기
            String body = null;
            if (headers.containsKey("Content-Length")) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                body = IOUtils.readData(br, contentLength);
            }
            log.info("Body: " + body);

            if(requestLine != null)
                handleRequest(requestLine, headers, body, dos);

        } catch (IOException | URISyntaxException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
    }

    private void handleRequest(String line, Map<String, String> headers, String body, DataOutputStream dos) throws IOException, URISyntaxException {
        String[] tokens = line.split(" ");

        boolean logined = false;
        if(headers.containsKey("Cookie") && headers.get("Cookie").equals("logined=true"))
            logined = true;


        if(tokens[0].equals(GET.getMethod())) {
            URI uri = new URI(tokens[1]);
            String path = uri.getPath();
            String query = uri.getQuery();

            if (query == null) {

                if (path.equals(LIST_HTML.getUrl()) && !logined) {
                    HttpResponseUtils.response302Redirect(dos, LOGIN_HTML.getUrl(), false);
                    return;
                }

                HttpResponseUtils.serveFile(dos, path);
            } else {
                HashMap<String, String> map = (HashMap<String, String>) HttpRequestUtils.parseQueryParameter(query);
                if (path.equals(SIGNUP.getUrl())) {
                    HandleSignUp.handleGet(dos, map);
                } else if (path.equals(LOGIN.getUrl())) {
                    HandleLogIn.handle(dos, map);
                }
            }
        }
        if(tokens[0].equals(POST.getMethod())) {

            if(tokens[1].equals(SIGNUP.getUrl())) {
                HandleSignUp.handlePost(dos, body);
            }
        }
    }
}
