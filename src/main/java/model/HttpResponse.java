package model;

import http.util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpResponse {
    private static final Logger log = Logger.getLogger(HttpResponseUtils.class.getName());
    private static final String WEB_ROOT = "webapp";
    private final DataOutputStream dos;

    private HttpRequestStartLine startLine;
    private Map<String, String> headers;
    private byte[] body;

    public HttpResponse(DataOutputStream dos) throws IOException {
        this.dos = dos;
    }

    public void response200Header(int lengthOfBodyContent, String contentType) throws IOException {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public void response302Redirect(String redirectUrl, boolean logined) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            if(logined) {
                dos.writeBytes("Set-Cookie: logined=true; Path=/; HttpOnly\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public void forward(String filePath){
        String contentType = "html";
        if(filePath.endsWith("css")){
            contentType = "css";
        }
        try {
            Path path = Paths.get(WEB_ROOT, filePath);
            if (Files.exists(path) && !Files.isDirectory(path)) {
                byte[] body = Files.readAllBytes(path);
                response200Header(body.length, contentType);
                responseBody(body);

            } else {
                //response404(dos);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error serving file: " + filePath, e);
        }
    }
    private void writeData(){

    }
}
