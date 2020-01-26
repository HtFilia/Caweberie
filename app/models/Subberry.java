package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Subberry extends Model {

    @Required
    public String title;

    // Il faudra changer la génération des tables SQL à travers d'autres classes
    /*
    @ManyToMany(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
    @JoinColumn(name="email")
    public List<User> moderators;
    */

    @ManyToMany(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
    @JoinColumn(name="username")
    public List<User> users;

    @OneToMany(mappedBy="sub", cascade=CascadeType.ALL)
    public List<Post> posts;

    @Column(name="nbusers")
    public int nbUsers;

    public Subberry (User creator, String title) {
        this.title = title;
        /*
        this.moderators = new ArrayList<>();
        this.moderators.add(creator);
         */
        this.users = new ArrayList<>();
        this.users.add(creator);
        this.nbUsers = 1;
        this.posts = new ArrayList<>();
    }

    private Post templatePost() {
        return new Post(this, new User("email", "bruhpwd", "bruh"), "testtemplate",
                "bruh template");
    }

    public Subberry addPost(User author, String title, String content) {
        Post newPost = new Post(this, author, title, content).save();
        this.posts.add(newPost);
        this.save();
        return this;
    }

    public Post getCarouselPost(int lastNumber) {
        if (posts.size() - 1 - lastNumber >= posts.size() ||
                posts.size() - 1 - lastNumber < 0) {
            return templatePost();
        }
        return posts.get(posts.size() - 1 - lastNumber);
    }

    public Subberry subscribe(String subscriberName) {
        User newUser = User.find("byUsername", subscriberName).first();
        if (newUser != null) {
            this.users.add(newUser);
            this.nbUsers++;
            this.save();
        }
        return this;
    }

    public List<Post> previewPosts() {
        return posts.subList(Math.max(0, posts.size() - 4), posts.size());
    }

    public Subberry unsubscribe(User subscriber) {
        this.users.remove(subscriber);
        this.nbUsers--;
        this.save();
        return this;
    }
}
