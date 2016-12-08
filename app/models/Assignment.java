package models;
import models.Submission;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by luisr on 11/13/2016.
 */

public class Assignment {

    private ArrayList<Submission> submissions = new ArrayList<>();

    private Long ID;

    private String title;



    private Date deadline;

    private String path;

    private Long lid;

    public Long getLid() {
        return lid;
    }

    public void setLid(Long lid) {
        this.lid = lid;
    }

    private String description;

    public Assignment(Long ID, String title, Date deadline, String description, String path){

        this.ID = ID;

        this.title = title;

        this.deadline = deadline;

        this.description = description;

        this.path = path;
    }

    public Assignment(Long ID, String title, Date deadline, String description, String path, Long lid){

        this.lid = lid;

        this.ID = ID;

        this.title = title;

        this.deadline = deadline;

        this.description = description;

        this.path = path;
    }

    public Assignment(String title, Date deadline, String description, String path, Long lid){

        this.lid = lid;

        this.title = title;

        this.deadline = deadline;

        this.description = description;

        this.path = path;
    }

    public ArrayList<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(ArrayList<Submission> submissions) {
        this.submissions = submissions;
    }

    public void addSubmission(Submission submission){
        submissions.add(submission);
    }

    public Submission removeSubmissionAt(int i){
        return submissions.remove(i);
    }

    public Long getID(){return ID;}

    public String getTitle(){return title;}

    public Date getDeadline(){return deadline;}

    public String getDescription(){return description;}

    public String getPath(){return path;}

    public void setID(Long newID){this.ID = newID;}

    public void setTitle(String newTitle){this.title = newTitle;}

    public void setDescription(String newDescription){this.description = newDescription;}

    public void setDeadline(Date newDeadline){this.deadline = newDeadline;}

    public void setPath(String newPath){this.path = newPath;}



}
