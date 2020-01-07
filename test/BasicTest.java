import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    @Test
    public void createAndRetrieveUser() {
        // Create a new user and save it
        new User("boblennon@gmail.com", "boblennonMDP", "BobLul").save();

        // Retrieve the user with e-mail address bob@gmail.com
        User bob = User.find("byEmail", "boblennon@gmail.com").first();

        // Test
        assertNotNull(bob);
        assertEquals("BobLul", bob.username);
    }

    @Test
    public void tryConnectAsUserTest() {
        // Create a new user and save it
        new User("boblennon@gmail.com", "boblennonMDP", "BobLennon").save();

        // Test
        assertNotNull(User.connect("boblennon@gmail.com", "boblennonMDP"));
        assertNull(User.connect("boblennon@gmail.com", "badpassword"));
        assertNull(User.connect("patricketoile@gmail.com", "boblennonMDP"));
    }

    @Test
    public void createSubTest() {
        // Create a new user and save it
        User bob = new User("boblennon@gmail.com", "boblennonMDP", "BobLennon").save();

        // Create a new sub and save it
        new Subberry(bob, "France").save();

        assertEquals(1, Subberry.count());

        List<Subberry> subs = Subberry.find("byTitle", "France").fetch();

        assertEquals(1, subs.size());
        Subberry sub = subs.get(0);
        assertEquals("France", sub.title);
        assertEquals(1, sub.moderators.size());
        assertEquals(1, sub.users.size());
        assertEquals(0, sub.posts.size());
        assertTrue(sub.moderators.contains(bob));
        assertTrue(sub.users.contains(bob));
    }

    @Test
    public void createPostTest() {
        // Create a new user and save it
        User bob = new User("boblennon@gmail.com", "boblennonMDP", "BobLennon").save();

        // Create a new sub and save it
        Subberry subFrance = new Subberry(bob, "France").save();

        // Create a new post with first message
        new Post(subFrance, bob, "Révolution", "Il faut lancer la révolution numérique.");

        // Test that the post has been created
        assertEquals(1, Post.count());

        // Retrieve all posts created by Bob
        List<Post> bobPosts = Post.find("byAuthor", bob).fetch();

        // Retrieve all posts from sub
        List<Post> subPosts = Post.find("bySub", subFrance).fetch();

        // Tests
        assertEquals(1, bobPosts.size());
        assertEquals(1, subPosts.size());
        Post firstBobPost = bobPosts.get(0);
        Post firstSubPost = subPosts.get(0);
        assertNotNull(firstBobPost);
        assertNotNull(firstSubPost);
        assertEquals(bob, firstBobPost.author);
        assertEquals(bob, firstSubPost.author);
        assertEquals("Révolution", firstBobPost.title);
        assertEquals("Révolution", firstSubPost.title);
        assertEquals("Il faut lancer la révolution numérique.", firstBobPost.content);
        assertEquals("Il faut lancer la révolution numérique.", firstSubPost.content);
        assertNotNull(firstBobPost.postedAt);
        assertNotNull(firstSubPost.postedAt);
    }
}
