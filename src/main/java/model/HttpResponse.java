package model;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) throws IOException {
        this.dos = dos;
    }

}
