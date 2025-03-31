package webserver;

import http.util.HttpRequestUtils;
import http.util.HttpResponseUtils;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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

            String line = br.readLine();
            log.info("Request: " + line);

            if(line != null)
                handleRequest(line, br, dos);

        } catch (IOException | URISyntaxException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
    }

    private void handleRequest(String line, BufferedReader br, DataOutputStream dos) throws IOException, URISyntaxException {
        String[] tokens = line.split(" ");

        if(tokens[0].equals("GET")) {
            URI uri = new URI(tokens[1]);
            String path = uri.getPath();
            String query = uri.getQuery();

            if(query == null) {
                HttpResponseUtils.serveFile(dos, path);
            }else {
                HashMap<String, String> map = (HashMap<String, String>) HttpRequestUtils.parseQueryParameter(query);
                if(path.equals("/user/signup")){
                    HandleSignUp.handle(br, dos, map);
                }else if(path.equals("/user/login")){
                    HandleLogIn.handle(br, dos, map);
                }
            }

        }else if(tokens[0].equals("POST")) {

        }
    }
}
