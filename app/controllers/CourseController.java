package controllers;

import com.fasterxml.jackson.databind.JsonNode;
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

import static play.mvc.Controller.request;
import static play.mvc.Results.*;

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

    public Result getAvailableSections(Long id) {
        ArrayList<Section> sectionsList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "SELECT S.id, S.course_id, C.tutor_id, C.title, C.description " +
                    "FROM sections AS S NATURAL JOIN courses AS C " +
                    "WHERE S.id NOT IN (" +
                        "SELECT S.id " +
                        "FROM enroll AS E NATURAL JOIN sections AS S NATURAL JOIN courses AS C " +
                        "WHERE E.section_id = S.id " +
                        "AND student_id = ? " +
                    ")";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Get Available Sections for Student: " + id);
                Long course_id = rs.getLong("course_id");
                Long tutor_id = rs.getLong("tutor_id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Section obj = new Section(tutor_id, title, description);
                obj.setId(rs.getLong("id"));
                obj.setCourse_id(course_id);
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

                Course obj = new Course(id, title, description);
                obj.setId(rs.getLong("id"));
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
            String statement = "SELECT S.id, E.section_id, S.course_id, C.tutor_id, C.title, C.description " +
                    "FROM enroll AS E NATURAL JOIN sections AS S NATURAL JOIN courses AS C " +
                    "WHERE E.section_id = S.id " +
                    "AND student_id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Get Sections from Student: " + id);
                Long course_id = rs.getLong("course_id");
                Long tutor_id = rs.getLong("tutor_id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Section obj = new Section(tutor_id, title, description);
                obj.setId(rs.getLong("id"));
                obj.setCourse_id(course_id);
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

    public Result getSections(Long course_id) {
        ArrayList<Section> sectionList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "SELECT * " +
                    "FROM sections as S NATURAL JOIN courses as C " +
                    "WHERE course_id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, course_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Get Sections from Course: " + course_id);
                Long tutor_id = rs.getLong("tutor_id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Section obj = new Section(tutor_id, title, description);
                obj.setId(rs.getLong("id"));
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
                    "FROM sections as S NATURAL JOIN courses as C " +
                    "WHERE S.id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Section found!");
                Long course_id = rs.getLong("course_id");
                Long tutor_id = rs.getLong("tutor_id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Section section = new Section(tutor_id, title, description);
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
