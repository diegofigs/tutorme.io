package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by diegofigs on 11/2/16.
 */
@Entity
@Table(name = ("courses"))
@Inheritance(strategy = InheritanceType.JOINED)
public class Course {

    @Id
    @Column
    private Long id;

    @Constraints.Required
    @Column
    private String title;

    @Column
    private String description;

    public Course() {

    }

    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
