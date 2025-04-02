package model;

import http.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class HttpRequest {
    private HttpStartLine startLine;
    private Map<String, String> headers;
    private String body;

    private HttpRequest(HttpStartLine startLine, Map<String, String> headers, String body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest from(BufferedReader br) throws IOException, URISyntaxException {
        // 요청 메세지 읽기
        String requestLine = br.readLine();
        // 헤더 읽기
        Map<String, String> headers = IOUtils.readHeaders(br);
        // 바디 읽기
        String body = null;
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            body = IOUtils.readData(br, contentLength);
        }

        return new HttpRequest(new HttpStartLine(requestLine), headers, body);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
    public String getBody() {
        return body;
    }
    public String getMethod() {
        return startLine.getMethod();
    }
    public URI getURI() {
        return startLine.getURI();
    }
    public String getVersion() {
        return startLine.getVersion();
    }
}
