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

    private Long lid;

    public Document(Long id,String title, String description, String path, Long lid){

        this.ID = id;

        this.title = title;

        this.description = description;

        this.path = path;

        this.lid = lid;
    }

    public Document(String title, String description, String path, Long lid){


        this.title = title;

        this.description = description;

        this.path = path;

        this.lid = lid;
    }

    public Long getID(){return ID;}

    public String getTitle(){return title;}

    public String getDescription(){return description;}

    public String getPath(){return path;}

    public Long getLId(){return lid;}

    public void setID(Long newID){this.ID = newID;}

    public void setTitle(String newTitle){this.title = newTitle;}

    public void setDescription(String newDescription){this.description = newDescription;}

    public void setPath(String newPath){this.path = newPath;}

    public void setLId(Long newLId){this.lid = newLId;}

}
