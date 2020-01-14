import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    @Before
    public void setup() { Fixtures.deleteDatabase(); }

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
        //assertEquals(1, sub.moderators.size());
        assertEquals(1, sub.users.size());
        assertEquals(0, sub.posts.size());
        //assertTrue(sub.moderators.contains(bob));
        assertTrue(sub.users.contains(bob));
    }

    @Test
    public void createPostTest() {
        // Create a new user and save it
        User bob = new User("boblennon@gmail.com", "boblennonMDP", "BobLennon").save();

        // Create a new sub and save it
        Subberry subFrance = new Subberry(bob, "France").save();

        // Create a new post with first message
        subFrance.addPost(bob, "Révolution", "Il faut lancer la révolution numérique.");

        // Test that the post has been created
        assertEquals(1, subFrance.posts.size());

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

    @Test
    public void createMessageTest() {
        // Create new users and save them
        User bob = new User("boblennon@gmail.com", "boblennonMDP", "BobLennon").save();
        User jeff = new User("mynameisjeff@gmail.com", "jeffMDP", "MyNameJeff").save();
        User tom = new User("tomvde@gmail.com", "tomvdeMDP", "VDESalt").save();

        // Create a new sub and save it
        Subberry franceSub = new Subberry(bob, "France").save();

        // Create a new post
        franceSub.addPost(bob, "Révolution", "Il faut lancer la révolution numérique.");

        // Retrieve Bob's post
        List<Post> bobFrancePosts = Post.find("bySubAndAuthor", franceSub, bob).fetch();
        assertEquals(1, bobFrancePosts.size());
        Post bobPost = bobFrancePosts.get(0);
        assertNotNull(bobPost);

        // Post two messages
        bobPost.addMessage(jeff, "Nice post");
        bobPost.addMessage(tom, "Insane");

        // Count instances
        assertEquals(3, User.count());
        assertEquals(1, bobFrancePosts.size());
        assertEquals(2, bobPost.messages.size());

        // Retrieve Bob's post
        bobPost = Post.find("byAuthor", bob).first();
        assertNotNull(bobPost);

        // Navigate to comments
        assertEquals(2, Message.count());
        assertEquals("MyNameJeff", bobPost.messages.get(0).authorUsername);

        // Delete the post
        bobPost.delete();

        // Check if comments have been deleted
        assertEquals(3, User.count());
        assertEquals(0, Post.count());
        assertEquals(0, Message.count());
    }

    @Test
    public void databaseTest() {
        Fixtures.loadModels("data.yml");

        // Count Things
        assertEquals(2, User.count());
        assertEquals(1, Subberry.count());
        assertEquals(1, Post.count());
        assertEquals(1, Message.count());

        // Retrieve France's sub
        Subberry franceSub = Subberry.find("byTitle", "France").first();
        assertEquals(2, franceSub.users.size());

        // Try to connect as users
        assertNotNull(User.connect("boblennon@gmail.com", "boblennonMDP"));
        assertNotNull(User.connect("jeff@gmail.com", "jeffMDP"));
        assertNull(User.connect("boblennon@gmail.com", "badMDP"));
        assertNull(User.connect("badMail", "boblennonMDP"));

        // Find all of bob's posts
        List<Post> bobPosts = Post.find("author.email", "boblennon@gmail.com").fetch();
        assertEquals(1, bobPosts.size());

        // Find all messages related to bob's posts
        List<Message> bobMessages = Message.find("parentPost.author.email", "boblennon@gmail.com").fetch();
        assertEquals(1, bobMessages.size());

        // Find the most recent post
        Post mostRecentPost = Post.find("order by postedAt desc").first();
        assertNotNull(mostRecentPost);
        assertEquals("Révolution", mostRecentPost.title);

        // Check that this post has 1 message
        assertEquals(1, mostRecentPost.messages.size());

        // Get bob as a user
        User bob = User.connect("boblennon@gmail.com", "boblennonMDP");

        // Post a new message
        mostRecentPost.addMessage(bob, "Hello guys");
        assertEquals(2, mostRecentPost.messages.size());
        assertEquals(2, Message.count());
    }
}
