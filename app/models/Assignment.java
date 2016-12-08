package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;

/**
 * Created by luisr on 11/13/2016.
 */

public class Assignment {

    private Long ID;

    private String title;


    private Date deadline;

    private String path;

    private ArrayList<Submission> submissions;
    private String description;

    private Long lid;

    public Assignment(Long ID, String title, Date deadline, String description, String path, Long lid){

        this.lid = lid;

        this.ID = ID;

        this.title = title;

        this.deadline = deadline;

        this.description = description;

        this.path = path;

        this.submissions = new ArrayList<>();
    }

    public ArrayList<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(ArrayList<Submission> submissions) {
        this.submissions = submissions;
    }

    public Assignment(String title, Date deadline, String description, String path, Long lid){

        this.lid = lid;

        this.title = title;

        this.deadline = deadline;

        this.description = description;

        this.path = path;
    }


    public Long getID(){return ID;}

    public Long getLID(){return lid;}

    public String getTitle(){return title;}

    public Date getDeadline(){return deadline;}

    public String getDescription(){return description;}

    public String getPath(){return path;}

    public void setID(Long newID){this.ID = newID;}

    public void setLID(Long newID){this.lid = newID;}

    public void addSubmission(Submission s){
        submissions.add(s);
    }

    public Submission removeSubmissionAt(int i){
        return submissions.remove(i);
    }

    public void setTitle(String newTitle){this.title = newTitle;}

    public void setDescription(String newDescription){this.description = newDescription;}

    public void setDeadline(Date newDeadline){this.deadline = newDeadline;}

    public void setPath(String newPath){this.path = newPath;}



}
