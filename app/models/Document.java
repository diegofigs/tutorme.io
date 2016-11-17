package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by luisr on 11/13/2016.
 */

public class Document {

    private Long ID;

    private String title;

    private String path;

    private String description;

    public Document(Long ID, String title, String description, String path){

        this.ID = ID;

        this.title = title;

        this.description = description;

        this.path = path;
    }

    public Long getID(){return ID;}

    public String getTitle(){return title;}

    public String getDescription(){return description;}

    public String getPath(){return path;}

    public void setID(Long newID){this.ID = newID;}

    public void setTitle(String newTitle){this.title = newTitle;}

    public void setDescription(String newDescription){this.description = newDescription;}

    public void setPath(String newPath){this.path = newPath;}

}
