package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
                    "FROM sections AS S INNER JOIN courses AS C ON S.course_id = C.id " +
                    "WHERE S.id NOT IN ( " +
                        "SELECT S.id " +
                        "FROM enroll AS E INNER JOIN sections AS S ON E.section_id = S.id " +
                        "INNER JOIN courses AS C ON S.course_id = C.id " +
                        "WHERE E.student_id = ? " +
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

    public Result getSections(Long user_id, Long course_id) {
        Course course = null;
        ArrayList<Section> sectionList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "SELECT * " +
                    "FROM courses " +
                    "WHERE id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, course_id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Get Course: " + course_id);
                Long tutor_id = rs.getLong("tutor_id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                course = new Course(tutor_id, title, description);
                course.setId(course_id);
            }

            if(course.getTutor_id() == user_id){
                Logger.info("For Tutor: " + user_id);
                statement = "SELECT S.id, S.tutor_id, C.title, C.description " +
                        "FROM sections as S JOIN courses as C " +
                        "on S.course_id = C.id " +
                        "and course_id = ?";
                preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setLong(1, course_id);
            }
            else {
                Logger.info("For Student: " + user_id);
                statement = "SELECT S.id, S.tutor_id, C.title, C.description " +
                        "FROM enroll AS E INNER JOIN sections AS S ON E.section_id = S.id " +
                        "INNER JOIN courses AS C ON S.course_id = C.id " +
                        "WHERE E.student_id = ? " +
                        "AND C.id = ?";
                preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setLong(1, user_id);
                preparedStatement.setLong(2, course_id);
            }
            rs = preparedStatement.executeQuery();
            while(rs.next()){
                Long tutor_id = rs.getLong("tutor_id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Section obj = new Section(tutor_id, title, description);
                obj.setId(rs.getLong("id"));
                obj.setCourse_id(course_id);
                sectionList.add(obj);
            }
            if(course != null){
                if(!sectionList.isEmpty()){
                    ObjectNode res = Json.newObject();
                    res.set("course", Json.toJson(course));
                    res.set("sections", Json.toJson(sectionList));
                    return ok(res);
                }
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
        Logger.info("Get Section: " + id);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "SELECT * " +
                    "FROM sections as S INNER JOIN courses as C ON S.course_id = C.id " +
                    "WHERE S.id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
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

    public Result enrollUser(Long section_id) {
        JsonNode req = request().body().asJson();
        Long user_id = req.findPath("user_id").asLong();

        Logger.info("Section: " + section_id);
        Logger.info("Enroll Student: " + user_id);

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "INSERT INTO enroll(student_id, section_id) " +
                    "VALUES (?, ?)";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, user_id);
            preparedStatement.setLong(2, section_id);
            preparedStatement.executeUpdate();
            return ok();
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

    public Result dropUser(Long section_id, Long user_id) {
        Logger.info("Section: " + section_id);
        Logger.info("Drop Student: " + user_id);

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "DELETE FROM enroll " +
                    "WHERE student_id = ? " +
                    "AND section_id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, user_id);
            preparedStatement.setLong(2, section_id);
            preparedStatement.executeUpdate();
            return ok();
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

    public Result createCourse() {
        JsonNode req = request().body().asJson();
        Long tutor_id = req.findPath("tutor_id").asLong();
        String title = req.findPath("title").textValue();
        String description = req.findPath("description").textValue();

        Logger.info("Create Course for Tutor: " + tutor_id);

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "INSERT INTO courses(tutor_id, title, description) " +
                    "VALUES (?, ?, ?) " +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, tutor_id);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, description);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Course course = new Course(tutor_id, title, description);
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

    public Result deleteCourse(Long id) {
        Logger.info("Delete Course: " + id);

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "DELETE FROM courses " +
                    "WHERE id = ? " +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Course course = new Course(rs.getLong("tutor_id"), rs.getString("title"), rs.getString("description"));
                course.setId(id);
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

    public Result createSection() {
        JsonNode req = request().body().asJson();
        Long tutor_id = req.findPath("tutor_id").asLong();
        Long course_id = req.findPath("course_id").asLong();

        Logger.info("Create Section for Course: " + course_id);
        Logger.info("Under Tutor: " + tutor_id);

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement;

            statement = "SELECT title, description " +
                    "FROM courses " +
                    "WHERE id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, course_id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Section section = new Section(tutor_id, rs.getString("title"), rs.getString("description"));
                section.setCourse_id(course_id);

                statement = "INSERT INTO sections(course_id, tutor_id) " +
                        "VALUES (?, ?) " +
                        "RETURNING *";
                preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setLong(1, course_id);
                preparedStatement.setLong(2, tutor_id);
                rs = preparedStatement.executeQuery();
                if(rs.next()){
                    section.setId(rs.getLong("id"));
                    return ok(Json.toJson(section));
                }
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

    public Result deleteSection(Long id) {
        Logger.info("Delete Section: " + id);

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "DELETE FROM sections " +
                    "WHERE id = ? " +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return ok();
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
