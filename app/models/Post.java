package models;

import play.data.validation.MaxSize;
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

    @Lob
    @Required
    @MaxSize(10000)
    public String content;

    @Required
    public Date postedAt;

    public String imgLink;

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
        this.imgLink = "public/images/defaultPost.jpg";
    }

    public Post(Subberry sub, User author, String title, String content, String imgLink) {
        this.sub = sub;
        this.author = author;
        this.title = title;
        this.content = content;
        this.messages = new ArrayList<>();
        this.postedAt = new Date();
        this.imgLink = imgLink;
    }

    public Post addMessage(String authorName, String content) {
        User author = User.find("byUsername", authorName).first();
        Message newMessage = new Message(author, content, this).save();
        this.messages.add(newMessage);
        this.save();
        return this;
    }

    public Post deleteMessage(int id) {
        this.messages.remove(id);
        this.save();
        return this;
    }

    public Post modifyMessage(int id, String content) {
        this.messages.get(id).content = content;
        this.save();
        return this;
    }

    public List<Post> previousPosts() {
        return Post.find("postedAt < ?1 order by postedAt desc").from(0).fetch(4);
    }

    public List<Post> nextPosts() {
        return Post.find("postedAt > ?1 order by postedAt asc").from(0).fetch(4);
    }

    public String previewContent() {
        String[] parts = content.split(" ");
        String preview = "";
        int idx = 0;
        while (preview.length() < 60 && idx < parts.length) {
            preview = preview.concat(" " + parts[idx]);
            idx++;
        }
        if (idx == parts.length) {
            return preview;
        }
        return preview.concat(" ...");
    }

    @Override
    public String toString() {
        return title;
    }
}
