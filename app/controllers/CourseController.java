package controllers;

import models.Course;
import models.Section;
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
import java.util.ArrayList;

import static play.mvc.Results.badRequest;
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
}
