package controllers;

import models.Course;
import models.Section;
import models.WallPost;
import models.Comment;
import play.Logger;
import play.data.FormFactory;
import play.db.Database;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.notFound;
import static play.mvc.Results.ok;

/**
 * Created by diegofigs on 11/16/16.
 */
public class CourseController {
    @Inject
    FormFactory formFactory;

    private Database db;

    @Inject
    public CourseController(Database db) {
        this.db = db;
    }

    public Result getCourses() {
        ArrayList<Course> courseList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "SELECT * FROM courses";
            preparedStatement = conn.prepareStatement(statement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Get Courses");
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Course obj = new Course(title, description);
                obj.setId(id);
                courseList.add(obj);
            }
            if(!courseList.isEmpty()){
                return ok(Json.toJson(courseList));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return badRequest();
    }

    public Result getTutorCourses(Long id){
        ArrayList<Course> courseList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "SELECT * FROM courses WHERE tutor_id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Get Courses from Tutor: " + id);
                String title = rs.getString("title");
                String description = rs.getString("description");

                Course obj = new Course(title, description);
                obj.setId(id);
                courseList.add(obj);
            }
            if(!courseList.isEmpty()){
                return ok(Json.toJson(courseList));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return notFound();
    }

    public Result getStudentSections(Long id){
        ArrayList<Section> sectionsList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "SELECT S.id, E.section_id, C.title, C.description " +
                    "FROM enroll AS E NATURAL JOIN sections AS S NATURAL JOIN courses AS C " +
                    "WHERE E.section_id = S.id " +
                    "AND student_id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Get Sections from Student: " + id);
                String title = rs.getString("title");
                String description = rs.getString("description");

                Section obj = new Section(title, description);
                obj.setId(id);
                sectionsList.add(obj);
            }
            if(!sectionsList.isEmpty()){
                return ok(Json.toJson(sectionsList));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return notFound();
    }

    public Result getCourse(Long id){
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "SELECT * FROM courses WHERE id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, id.toString());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Course found!");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Course course = new Course(title, description);
                course.setId(rs.getLong("id"));
                return ok(Json.toJson(course));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return badRequest();
    }

    public Result getSections() {
        ArrayList<Section> sectionList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "SELECT * FROM sections NATURAL JOIN courses";
            preparedStatement = conn.prepareStatement(statement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Get Sections");
                Long id = rs.getLong("id");
                Long course_id = rs.getLong("course_id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Section obj = new Section(title, description);
                obj.setId(id);
                obj.setCourse_id(course_id);
                sectionList.add(obj);
            }
            if(!sectionList.isEmpty()){
                return ok(Json.toJson(sectionList));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return badRequest();
    }

    public Result getSection(Long id){
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "SELECT * " +
                    "FROM sections NATURAL JOIN courses " +
                    "WHERE id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, id.toString());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Section found!");
                Long course_id = rs.getLong("course_id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Section section = new Section(title, description);
                section.setId(rs.getLong("id"));
                section.setCourse_id(course_id);
                return ok(Json.toJson(section));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return badRequest();
    }

    public Result getWall(Long sectionId){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ArrayList<WallPost> wallPosts = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

        try {
            conn = db.getConnection();

            String statement =  "SELECT * " +
                                "FROM wallPosts " +
                                "WHERE sectionId = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, sectionId);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Logger.info("WallPost added");
                Long id = rs.getLong("id");
                String fromEmail = rs.getString("fromEmail");
                String text = rs.getString("text");
                String date = rs.getString("date");
                String favoriteOf = rs.getString("favoriteOf");

                wallPosts.add(new WallPost(id, sectionId, fromEmail, text,
                        sdf.parse(date), favoriteOf));
            }
            if(!wallPosts.isEmpty()){
                return ok(Json.toJson(wallPosts));
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return badRequest();
    }

    public Result getComments(Long videoId){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Comment> comments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

        try {
            conn = db.getConnection();

            String statement =  "SELECT * " +
                                "FROM comments " +
                                "WHERE videoId = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, videoId);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Logger.info("Comment added");
                Long id = rs.getLong("id");
                String fromEmail = rs.getString("fromEmail");
                String text = rs.getString("text");
                String date = rs.getString("date");
                String favoriteOf = rs.getString("favoriteOf");

                comments.add(new Comment(id, videoId, fromEmail, text,
                        sdf.parse(date), favoriteOf));
            }
            if(!comments.isEmpty()){
                return ok(Json.toJson(comments));
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return badRequest();
    }
}
