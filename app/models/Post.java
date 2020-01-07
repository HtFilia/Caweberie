package models;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post extends Model {

    @Required
    @ManyToOne
    public Subberry sub;

    @Required
    @ManyToOne
    public User author;

    @Required
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    public List<Message> messages;

    public Post(Subberry sub, User author, Message initialMessage) {
        this.sub = sub;
        this.author = author;
        this.messages = new ArrayList<>();
        this.messages.add(initialMessage);
    }
}
