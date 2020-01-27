package controllers;

import models.*;
import play.Play;

public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {
        return User.connect(username, password) != null;
    }

    static void onAuthenticated() {
        redirectToOriginalUrl();
    }

    static void onDisconnected() {
        redirectToOriginalUrl();
    }

    static void redirectToOriginalUrl() {
        String url = flash.get("url");
        if (url == null) {
            url = Play.ctxPath + "/";
        }
        redirect(url);
    }

    static boolean check(String profile) {
        if ("admin".equals(profile)) {
            return User.find("byEmail", connected()).<User>first().isAdmin;
        }
        return false;
    }
}