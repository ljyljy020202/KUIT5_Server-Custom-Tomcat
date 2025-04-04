package controller;

import model.HttpRequest;
import model.HttpResponse;

import static enums.URL.*;

public class ListController implements Controller {
    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) {
        if(httpRequest.logined()){
            httpResponse.forward(LIST_HTML.getUrl());
            return;
        }
        httpResponse.redirect(LOGIN_HTML.getUrl(), false);
    }
}
