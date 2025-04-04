package controller;

import model.HttpRequest;
import model.HttpResponse;

import static enums.URL.INDEX_HTML;
import static enums.URL.LOGIN_HTML;

public class ListController implements Controller {
    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) {
        if(httpRequest.logined()){
            httpResponse.forward(INDEX_HTML.getUrl());
            return;
        }
        httpResponse.response302Redirect(LOGIN_HTML.getUrl(), false);
    }
}
