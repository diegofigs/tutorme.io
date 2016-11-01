package models;

import org.springframework.ui.Model;
import play.data.Form;
import play.data.validation.Constraints;
import play.db.Database;

import javax.inject.Singleton;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

import static play.mvc.Results.ok;

/**
 * Created by diegofigs on 10/4/16.
 */
@Entity
@Table(name = ("users"))
public class User{

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Constraints.Email
    @Constraints.Required
    @Column
    private String email;

    @Constraints.Required
    @Column
    private String password;

    public User() {

    }

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return this.firstname + " " + this.lastname + "(" + this.id + ")" +
                "\n\tEmail: " + this.email +
                "\n\tPassword: " + this.password;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
