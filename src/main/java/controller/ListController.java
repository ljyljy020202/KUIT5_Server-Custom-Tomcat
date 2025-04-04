package controller;

import model.HttpRequest;
import model.HttpResponse;

import java.io.IOException;

import static enums.URL.*;

public class ListController implements Controller {
    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if(httpRequest.logined()){
            httpResponse.forward(LIST_HTML.getUrl());
            return;
        }
        httpResponse.redirect(LOGIN_HTML.getUrl(), false);
    }
}
