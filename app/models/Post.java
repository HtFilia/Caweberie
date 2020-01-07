package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Post extends Model {

    @Required
    @ManyToOne
    public Subberry sub;

    @Required
    public String title;

    @Required
    @ManyToOne
    public User author;

    @Required
    public Date postedAt;

    @Required
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    public List<Message> messages;

    public Post(Subberry sub, String title, User author, Message initialMessage) {
        this.sub = sub;
        this.title = title;
        this.author = author;
        this.messages = new ArrayList<>();
        this.messages.add(initialMessage);
        this.postedAt = new Date();
    }

    public Post addMessage(User author, String content) {
        Message newMessage = new Message(author, content, this).save();
        this.messages.add(newMessage);
        this.save();
        return this;
    }

    @Override
    public String toString() {
        return title;
    }
}