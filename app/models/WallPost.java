package models;

import play.data.validation.Constraints;
import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by Andres on 11/17/16.
 */
@Entity
@Table(name = ("users"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class WallPost {
    @Id
    @Column
    private Long id;

    @Constraints.Email
    @Constraints.Required
    @Column
    private Long sectionId;

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
    private ArrayList<String> favoriteOf;

    public WallPost() {
    }

    public WallPost(Long id, Long sectionId, String fromEmail, String text, Date date, ArrayList<String> favoriteOf) {
        this.id = id;
        this.sectionId = sectionId;
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

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
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

    public ArrayList<String> getFavoriteOf() { return favoriteOf; }

    public void setFavoriteOf(ArrayList<String> favoriteOf) { this.favoriteOf = favoriteOf; }

    public void addFavorite(String email) { this.favoriteOf.add(email); }

}
