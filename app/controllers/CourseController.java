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
        ArrayList<Section> courseList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "SELECT * FROM sections NATURAL JOIN courses";
            preparedStatement = conn.prepareStatement(statement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Get Courses that have Sections");
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Section obj = new Section(title, description);
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
}
