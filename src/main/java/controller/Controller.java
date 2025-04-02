package controller;

import model.HttpRequest;
import model.HttpResponse;

public interface Controller {
    void execute(HttpRequest httpRequest, HttpResponse httpResponse);
}
