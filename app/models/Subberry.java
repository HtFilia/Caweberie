package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Subberry extends Model {

    @Required
    public String title;

    @Required
    @ManyToMany
    public List<User> moderators;

    @Required
    @ManyToMany
    public List<User> users;

    @OneToMany
    public List<Post> posts;

    public Subberry (User creator, String title) {
        this.title = title;
        this.moderators = new ArrayList<>();
        this.moderators.add(creator);
        this.users = new ArrayList<>();
        this.users.add(creator);
        this.posts = new ArrayList<>();
    }
}
