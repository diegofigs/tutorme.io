package models;

/**
 * Created by luisr on 12/7/2016.
 */
public class Submission {
    private Integer grade;
    private Long ID;
    private Long stID;
    private Long aID;
    private String path;



    private String email;

    public Submission(Long stID, Long aID, Integer grade, String path){
        this.stID = stID;
        this.aID = aID;
        this.grade = grade;
        this.path = path;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Long getID() {
        return ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getStID() {
        return stID;
    }

    public void setStID(Long stID) {
        this.stID = stID;
    }

    public Long getaID() {
        return aID;
    }

    public void setaID(Long aID) {
        this.aID = aID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
