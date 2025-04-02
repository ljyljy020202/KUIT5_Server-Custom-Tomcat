package webserver;

import db.MemoryUserRepository;
import http.util.HttpRequestUtils;
import http.util.HttpResponseUtils;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static enums.URL.INDEX_HTML;

public class HandleSignUp {

    public static void handleGet(DataOutputStream dos, HashMap<String, String> map) throws IOException {
        String userId = map.get("userId");
        String password = map.get("password");
        String name = map.get("name");
        String email = map.get("email");

        User user = new User(userId, password, name, email);
        signUp(dos, user);
    }

    public static void handlePost(DataOutputStream dos, String body) throws IOException {
        Map<String, String> map = HttpRequestUtils.parseQueryParameter(body);
        handleGet(dos, (HashMap<String, String>) map);
    }

    private static void signUp(DataOutputStream dos, User user) throws IOException {
        MemoryUserRepository.getInstance().addUser(user);
        System.out.println(user.getUserId()+" 회원가입 완료");

        HttpResponseUtils.response302Redirect(dos, INDEX_HTML.getUrl(), false);
    }
}
