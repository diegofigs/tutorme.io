package models;

import play.data.validation.Constraints;
import javax.persistence.*;
import java.util.Date;


/**
 * Created by Andres on 11/14/16.
 */

@Entity
@Table(name = ("users"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Message {

    @Id
    @Column
    private Long id;

    @Constraints.Email
    @Constraints.Required
    @Column
    private String toEmail;

    @Constraints.Email
    @Constraints.Required
    @Column
    private String fromEmail;

    @Constraints.Required
    @Column
    private String text;

    @Column
    private Date date;

    public Message() {
    }

    public Message(Long id, String toEmail, String fromEmail, String text, Date date) {
        this.id = id;
        this.toEmail = toEmail;
        this.fromEmail = fromEmail;
        this.text = text;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTo() {
        return toEmail;
    }

    public void setTo(String toEmail) {
        this.toEmail = toEmail;
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


}
