package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void showSub(Long id) {
        Subberry sub = Subberry.findById(id);
        render(sub);
    }

    public static void showPost(Long id) {
        Post post = Post.findById(id);
        render(post);
    }
}