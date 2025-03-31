package webserver;

import db.MemoryUserRepository;
import http.util.HttpResponseUtils;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class HandleSignUp {

    public static void handleGet(DataOutputStream dos, HashMap<String, String> map) throws IOException {
        String userId = map.get("userId");
        String password = map.get("password");
        String name = map.get("name");
        String email = map.get("email");

        User user = new User(userId, password, name, email);
        MemoryUserRepository.getInstance().addUser(user);
        System.out.println(userId+" 회원가입 완료");

        HttpResponseUtils.response302Redirect(dos, "/index.html", false);
    }

    public static void handlePost(DataOutputStream dos) throws IOException {

    }
}
