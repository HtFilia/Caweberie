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

    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<User> moderators;

    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<User> users;

    @OneToMany(mappedBy="sub", cascade=CascadeType.ALL)
    public List<Post> posts;

    public Subberry (User creator, String title) {
        this.title = title;
        this.moderators = new ArrayList<>();
        this.moderators.add(creator);
        this.users = new ArrayList<>();
        this.users.add(creator);
        this.posts = new ArrayList<>();
    }

    public Subberry createPost(User author, String title, String content) {
        Post newPost = new Post(this, author, title, content).save();
        this.posts.add(newPost);
        this.save();
        return this;
    }

    public Subberry deletePost(int i) {
        this.posts.remove(i);
        return this;
    }

    public Subberry modifyPost(int i, String title, String content) {
        this.posts.get(i).content = content;
        this.posts.get(i).title = title;
        return this;
    }
}
