package http.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpResponseUtils {
    private static final Logger log = Logger.getLogger(HttpResponseUtils.class.getName());
    private static final String WEB_ROOT = "webapp";

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void response302Redirect(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
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

    public static void serveFile(DataOutputStream dos, String filePath) {
        if(filePath.equals("/")) {
            filePath = "/index.html";
        }
        try {
            Path path = Paths.get(WEB_ROOT, filePath);
            if (Files.exists(path) && !Files.isDirectory(path)) {
                byte[] body = Files.readAllBytes(path);
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else {
                //response404(dos);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error serving file: " + filePath, e);
        }
    }
}
