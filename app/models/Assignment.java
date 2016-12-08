package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by luisr on 11/13/2016.
 */

public class Assignment {

    private Long ID;

    private String title;


    private Date deadline;

    private String path;


    private String description;

    public Assignment(Long ID, String title, Date deadline, String description, String path){

        this.ID = ID;

        this.title = title;

        this.deadline = deadline;

        this.description = description;

        this.path = path;
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
