package controller;

import db.MemoryUserRepository;
import http.util.HttpResponseUtils;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static enums.URL.INDEX_HTML;
import static enums.URL.LOGIN_FAILED_HTML;

public class LoginController implements Controller {
    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) {

    }

    public static void handle(DataOutputStream dos, HashMap<String, String> map) throws IOException {
        String userId = map.get("userId");
        String password = map.get("password");

        User user = MemoryUserRepository.getInstance().findUserById(userId);
        if(user != null && user.getPassword().equals(password)) {
            loginSuccess(dos);
        }else{
            loginFailed(dos);
        }
    }

    private static void loginSuccess(DataOutputStream dos) throws IOException {
        HttpResponseUtils.response302Redirect(dos, INDEX_HTML.getUrl(), true);
        System.out.println("로그인 성공!");
    }

    private static void loginFailed(DataOutputStream dos) throws IOException {
        HttpResponseUtils.response302Redirect(dos, LOGIN_FAILED_HTML.getUrl(), false);
        System.out.println("로그인 실패");
    }
}
