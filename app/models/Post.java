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
    @ManyToOne
    public User author;

    @Required
    public String title;

    @Required
    public String content;

    @Required
    public Date postedAt;

    @Required
    @OneToMany(mappedBy = "parentPost", cascade = CascadeType.ALL)
    public List<Message> messages;

    public Post(Subberry sub, User author, String title, String content) {
        this.sub = sub;
        this.author = author;
        this.title = title;
        this.content = content;
        this.messages = new ArrayList<>();
        this.postedAt = new Date();
    }

    public Post addMessage(User author, String content) {
        Message newMessage = new Message(author, content, this).save();
        this.messages.add(newMessage);
        this.save();
        return this;
    }

    public Post deleteMessage(int i) {
        this.messages.remove(i);
        this.save();
        return this;
    }

    public Post modifyMessage(int i, String content) {
        this.messages.get(i).content = content;
        this.save();
        return this;
    }

    @Override
    public String toString() {
        return title;
    }
}
