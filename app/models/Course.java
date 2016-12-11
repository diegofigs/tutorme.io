package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Constraint;
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
    private Long tutor_id;

    @Constraints.Required
    @Column
    private String title;

    @Column
    private String description;

    public Course() {

    }

    public Course(Long tutor_id, String title, String description) {
        this.tutor_id = tutor_id;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTutor_id() {
        return tutor_id;
    }

    public void setTutor_id(Long tutor_id) {
        this.tutor_id = tutor_id;
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
