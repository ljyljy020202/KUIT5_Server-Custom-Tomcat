package model;

import http.util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static enums.URL.INDEX_HTML;
import static enums.URL.ROOT;

public class HttpResponse {
    private static final Logger log = Logger.getLogger(HttpResponseUtils.class.getName());
    private static final String WEB_ROOT = "webapp";
    private static DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) throws IOException {
        this.dos = dos;
    }

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) throws IOException {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void response302Redirect(DataOutputStream dos, String redirectUrl, boolean logined) {
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

    public static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }
    public static void forward(String filePath){
        String contentType = "html";
        if(filePath.equals(ROOT.getUrl())) {
            filePath = INDEX_HTML.getUrl();
        }
        if(filePath.endsWith("css")){
            contentType = "css";
        }
        try {
            Path path = Paths.get(WEB_ROOT, filePath);
            if (Files.exists(path) && !Files.isDirectory(path)) {
                byte[] body = Files.readAllBytes(path);
                response200Header(dos, body.length, contentType);
                responseBody(dos, body);
            } else {
                //response404(dos);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error serving file: " + filePath, e);
        }
    }
}
