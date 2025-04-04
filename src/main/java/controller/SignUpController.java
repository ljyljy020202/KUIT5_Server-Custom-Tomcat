package controller;

import db.MemoryUserRepository;
import http.util.HttpRequestUtils;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

import java.io.IOException;
import java.util.Map;

import static enums.HttpMethod.GET;
import static enums.HttpMethod.POST;
import static enums.URL.INDEX_HTML;

public class SignUpController implements Controller {
    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Map<String, String> map = null;
        String method = httpRequest.getMethod();
        if(method.equals(GET.getMethod())) {
            String query = httpRequest.getURI().getQuery();
            map = HttpRequestUtils.parseQueryParameter(query);
        }
        if(method.equals(POST.getMethod())) {
            String body = httpRequest.getBody();
            map = HttpRequestUtils.parseQueryParameter(body);
        }
        signUp(httpResponse, mapToUser(map));
    }

    public static User mapToUser(Map<String, String> map) throws IOException {
        String userId = map.get("userId");
        String password = map.get("password");
        String name = map.get("name");
        String email = map.get("email");

        User user = new User(userId, password, name, email);
        return user;
    }

    private static void signUp(HttpResponse httpResponse, User user) throws IOException {
        MemoryUserRepository.getInstance().addUser(user);
        System.out.println(user.getUserId()+" 회원가입 완료");

        httpResponse.redirect(INDEX_HTML.getUrl(), false);
    }
}
