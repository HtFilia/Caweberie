package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    @Before
    public static void addDefaults() {
        renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
        renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }

    public static void index() {
        Subberry franceSub = Subberry.find("byTitle", "France").first();
        Subberry mangaSub = Subberry.find("byTitle", "Manga").first();
        Subberry programmingSub = Subberry.find("byTitle", "Programming").first();
        //TODO: biggest subs
        List<Post> mostRecentPosts = Post.find("order by postedAt desc").from(0).fetch(3);
        render(franceSub, mangaSub, programmingSub, mostRecentPosts);
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