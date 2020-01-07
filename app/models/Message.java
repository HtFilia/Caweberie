package models;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Lob;
import javax.persistence.ManyToOne;

public class Message extends Model {

    @Required
    public User author;

    @Lob
    @Required
    @MaxSize(10000)
    public String content;

    @Required
    @ManyToOne
    public Post parentPost;

    public Message(User author, String content, Post parentPost) {
        this.author = author;
        this.content = content;
        this.parentPost = parentPost;
    }
}
