package models;

import javafx.collections.transformation.SortedList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;

/**
 * Created by luisr on 11/13/2016.
 */

public class Video {

    private Long ID;

    private String title;

    private String URL;

    private ArrayList<Comment> comments;

    public Video(Long ID, String title, String URL){

        this.ID = ID;

        this.title = title;

        this.URL = URL;

        this.comments = new ArrayList<Comment>();
    }

    public Long getID(){return ID;}

    public String getTitle(){return title;}

    public String getURL(){return URL;}

    public ArrayList<Comment> getComments(){return comments;}

    public Comment getComment(int ID){
        for(Comment c : comments)
            if(c.getId()==ID)
                return c;
        return null;
    }

    public void addComment(Comment c){comments.add(c);}

    public void setID(Long newID){this.ID = newID;}

    public void setTitle(String newTitle){this.title = newTitle;}

    public void setURL(String newURL){this.URL = newURL;}

    public Comment deleteComment(int ID){
        for(int i=0;i<comments.size();i++)
            if(comments.get(i).getId()==ID)
                return comments.remove(i);
        return null;
    }
}
