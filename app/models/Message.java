package models;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Message extends Model {

    @Required
    public String author;

    @Lob
    @Required
    @MaxSize(10000)
    public String content;

    @Required
    @ManyToOne
    public Post parentPost;

    @Required
    public Date postedAt;

    public Message(User author, String content, Post parentPost) {
        this.author = author.username;
        this.content = content;
        this.parentPost = parentPost;
        this.postedAt = new Date();
    }

    @Override
    public String toString() {
        return String.format("%s - %s %s", author, parentPost.toString(), postedAt.toString());

    }
}
