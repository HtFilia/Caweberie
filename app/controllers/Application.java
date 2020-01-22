package controllers;

import org.h2.engine.Session;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

@With(Security.class)
public class Application extends Controller {
    @Before
    public static void addDefaults() {
        renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
        renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }

    public static boolean isConnected() {
        return session.get("username") != null;
    }

    public static String getCurrentUsername() {
        User currentUser = User.find("byEmail", session.get("username")).first();
        return isConnected() ? currentUser.username : "null";
    }
    public static List<Subberry> getHighestSubberries() {
        return Subberry.find("order by nbUsers desc").from(0).fetch(3);
    }

    public static void index() {
        String username = getCurrentUsername();
        List<Subberry> highestSubberries = getHighestSubberries();
        List<Post> mostRecentPosts = Post.find("order by postedAt desc").from(0).fetch(3);
        render(highestSubberries, username, mostRecentPosts);
    }

    public static void showSub(Long id) {
        String username = getCurrentUsername();
        List<Subberry> highestSubberries = getHighestSubberries();
        Subberry sub = Subberry.findById(id);
        render(highestSubberries, username, sub);
    }

    public static void showPost(Long id) {
        String username = getCurrentUsername();
        List<Subberry> highestSubberries = getHighestSubberries();
        Post post = Post.findById(id);
        render(highestSubberries, username, post);
    }

    public static void addPost(Long id, String authorName, String title, String content) {
        Subberry subberry = Subberry.findById(id);
        User user = User.find("byUsername", authorName).first();
        System.out.println(user);
        subberry.addPost(user, title, content);
        showPost(id);
    }

    public static void addMessage(Long postId, String author, String content) {
        Post post = Post.findById(postId);
        post.addMessage(author, content);
        showPost(postId);
    }
}