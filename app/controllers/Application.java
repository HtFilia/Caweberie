package controllers;

import org.h2.engine.Session;
import play.*;
import play.data.validation.Required;
import play.mvc.*;
import play.modules.paginate.ValuePaginator;

import java.util.*;

import models.*;

@With(Security.class)
public class Application extends Controller {

    @Before
    public static void addDefaults() {
        flash.put("url", request.path);
    }

    public static boolean isConnected() {
        return session.get("username") != null;
    }

    public static User getCurrentUser() {
        return User.find("byEmail", session.get("username")).first();
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
        List<Post> toPrintPosts = Post.find("order by postedAt desc").from(0).fetch();
        ValuePaginator paginator = new ValuePaginator(toPrintPosts);
        paginator.setPageSize(4);
        render(highestSubberries, username, toPrintPosts, paginator);
    }

    public static void showAllSubs() {
        String username = getCurrentUsername();
        List<Subberry> highestSubberries = getHighestSubberries();
        ValuePaginator paginatorSub = new ValuePaginator(Subberry.find("order by nbUsers desc").from(0).fetch());
        paginatorSub.setPageSize(10);
        render(paginatorSub, username, highestSubberries);
    }


    public static void showPosts(List<Post> toPrintPosts) {
        String username = getCurrentUsername();
        List<Subberry> highestSubberries = getHighestSubberries();
        render(highestSubberries, username, toPrintPosts);
    }

    public static void showSub(Long id) {
        String username = getCurrentUsername();
        List<Subberry> highestSubberries = getHighestSubberries();
        Subberry sub = Subberry.findById(id);
        ValuePaginator paginator = new ValuePaginator(sub.posts);
        render(highestSubberries, username, sub, paginator);
    }

    public static void showPost(Long id) {
        String username = getCurrentUsername();
        List<Subberry> highestSubberries = getHighestSubberries();
        Post post = Post.findById(id);
        ValuePaginator paginatorMessages = new ValuePaginator(post.messages);
        paginatorMessages.setPageSize(10);
        render(highestSubberries, username, post, paginatorMessages);
    }

    public static void createSub() {
        String username = getCurrentUsername();
        List<Subberry> highestSubberries = getHighestSubberries();
        render(highestSubberries, username);
    }

    public static void addPost(Long id, String authorName,
                               @Required String title,
                               @Required String content) {
        Subberry subberry = Subberry.findById(id);
        User user = User.find("byUsername", authorName).first();
        subberry.addPost(user, title, content);
        showPost(id);
    }

    public static void addMessage(Long postId, String author, @Required String content) {
        Post post = Post.findById(postId);
        post.addMessage(author, content);
        showPost(postId);
    }

    public static void addSub(String authorName, @Required String title) {
        User currentUser = User.find("byEmail", authorName).first();
        Subberry newSub = new Subberry(currentUser, title).save();
        showSub(newSub.id);
    }

    public static void signup() {
        if (isConnected()) {
            index();
        }
        render();
    }

    public static void createUser(@Required String email,
                                  @Required String password,
                                  @Required String repeatedPassword,
                                  @Required String fullname) throws Throwable {
        // Check if username already taken
        User newUser = User.find("byEmail", email).first();
        if (newUser != null) {
            flash.keep("/");
            flash.error("Cet email est déjà utilisé.");
            params.flash();
            signup();
        }

        // Check if there is a password
        if (password.equals("")) {
            flash.keep("url");
            flash.error("Il faut rentrer un mot de passe");
            params.flash();
            signup();
        }

        // Check if both passwords match
        if (!password.equals(repeatedPassword)) {
            flash.keep("url");
            flash.error("Les mots de passe ne correspondent pas.");
            params.flash();
            signup();
        }

        // Check if there is a username
        if (fullname.equals("")) {
            flash.keep("url");
            flash.error("Il faut rentrer un pseudo.");
            params.flash();
            signup();
        }

        // Save new user to DB
        new User(email, password, fullname).save();

        // Good message to user then redirect to previous page
        redirectToOriginalUrl();
    }

    static void redirectToOriginalUrl() {
        String url = flash.get("url");
        if (url == null) {
            url = Play.ctxPath + "/";
        }
        redirect(url);
    }
}