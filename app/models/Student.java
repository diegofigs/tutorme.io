package models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by diegofigs on 11/2/16.
 */
@Entity
@Table(name = ("students"))
public class Student extends User{

    @Column
    private String summary;

    @Column
    private String interests;

    @Column
    private String activities;

    @Column
    private String skills;

    public Student() {
        super.setType(1);
    }

    public Student(String firstname, String lastname, String email, String password){
        super(firstname, lastname, email, password, 1);
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
