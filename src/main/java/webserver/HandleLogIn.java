package webserver;

import db.MemoryUserRepository;
import http.util.HttpResponseUtils;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class HandleLogIn {
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
        HttpResponseUtils.response302Redirect(dos,"/index.html", true);
        System.out.println("로그인 성공!");
    }

    private static void loginFailed(DataOutputStream dos) throws IOException {
        HttpResponseUtils.response302Redirect(dos,"/user/login_failed.html", false);
        System.out.println("로그인 실패");
    }
}
