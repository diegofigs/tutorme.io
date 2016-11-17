package models;

import play.data.validation.Constraints;
import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by Andres on 11/17/16.
 */
@Entity
@Table(name = ("comments"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Comment {
    @Id
    @Column
    private Long id;

    @Constraints.Email
    @Constraints.Required
    @Column
    private Long videoId;

    @Constraints.Email
    @Constraints.Required
    @Column
    private String fromEmail;

    @Constraints.Required
    @Column
    private String text;

    @Column
    private Date date;

    @Column
    private String favoriteOf;

    public Comment() {
    }

    public Comment(Long id, Long videoId, String fromEmail, String text, Date date, String favoriteOf) {
        this.id = id;
        this.videoId = videoId;
        this.fromEmail = fromEmail;
        this.text = text;
        this.date = date;
        this.favoriteOf = favoriteOf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getvideoId() {
        return videoId;
    }

    public void setvideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getFrom() {
        return fromEmail;
    }

    public void setFrom(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFavoriteOf() { return favoriteOf; }

    public void setFavoriteOf(String favoriteOf) { this.favoriteOf = favoriteOf; }

}
