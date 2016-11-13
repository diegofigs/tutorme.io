package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by diegofigs on 11/2/16.
 */
@Entity
@Table(name = ("tutors"))
public class Tutor extends User{

    @Column
    private String summary;

    @Column
    private String interests;

    @Column
    private String education;

    @Column
    private String certifications;

    public Tutor() {
        super.setType(0);
    }

    public Tutor(String firstname, String lastname, String email, String password){
        super(firstname, lastname, email, password, 0);
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

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }
}
