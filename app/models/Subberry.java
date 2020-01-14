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

    public Subberry (User creator, String title) {
        this.title = title;
        /*
        this.moderators = new ArrayList<>();
        this.moderators.add(creator);
         */
        this.users = new ArrayList<>();
        this.users.add(creator);
        this.posts = new ArrayList<>();
    }

    public Subberry addPost(User author, String title, String content) {
        Post newPost = new Post(this, author, title, content).save();
        this.posts.add(newPost);
        this.save();
        return this;
    }

    public Post getCarouselPost(int lastNumber) {
        if (posts.size() - 1 - lastNumber >= posts.size()) {
            throw new IllegalArgumentException("Should be lower than # of posts");
        }
        return posts.get(posts.size() - 1 - lastNumber);
    }
}
