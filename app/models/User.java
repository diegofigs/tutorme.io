package models;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by diegofigs on 10/4/16.
 */
@Entity
@Table(name = ("users"))
@Inheritance(strategy = InheritanceType.JOINED)
public class User{

    @Id
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

    @Column
    private Integer type;

    public User() {

    }

    public User(String firstname, String lastname, String email, String password, int type) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.type = type;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
