package models;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by diegofigs on 11/2/16.
 */
@Entity
@Table(name = ("sections"))
public class Section extends Course{

    @Column
    private Long course_id;

    public Section() {

    }

    public Section(String title, String description) {
        super(title, description);
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }
}
