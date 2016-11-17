package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisr on 11/13/2016.
 */



public class Lesson {

    private Long ID;


    private String name;

    private ArrayList<Video> videos;

    private ArrayList<Assignment> assignments;

    private ArrayList<Document> documents;

    public Lesson(Long ID, String name){
        this.ID = ID;

        this.name = name;

        videos = new ArrayList<>();

        documents = new ArrayList<>();

        assignments = new ArrayList<>();
    }

    public Long getID(){return ID;}

    public String getName(){return name;}

    public ArrayList<Video> getVideos(){return videos;}

    public ArrayList<Document> getDocuments(){return documents;}

    public ArrayList<Assignment> getAssignments(){return assignments;}

    public Video getVideo(int id){
        for(Video v : videos){
            if(v.getID()==id)
                return v;
        }

        return null;
    }

    public Assignment getAssignment(int id){
        for(Assignment a : assignments){
            if(a.getID()==id)
                return a;
        }

        return null;
    }

    public Document getDocument(int id){
        for(Document d : documents){
            if(d.getID()==id)
                return d;
        }

        return null;
    }

    public void setName(String newName){name = newName;}

    public void addVideo(Video v){ videos.add(v);}

    public void addAssignment(Assignment a){assignments.add(a);}

    public void addDocument(Document d){documents.add(d);}

    public Video deleteVideo(int id){
        for(int i=0;i<videos.size();i++){
            if(videos.get(i).getID()==id)
                return videos.remove(i);
        }

        return null;
    }

    public Assignment deleteAssignment(int id){
        for(int i=0;i<assignments.size();i++){
            if(assignments.get(i).getID()==id)
                return assignments.remove(i);
        }

        return null;
    }

    public Document deleteDocument(int id){
        for(int i=0;i<documents.size();i++){
            if(documents.get(i).getID()==id)
                return documents.remove(i);
        }

        return null;
    }


}
