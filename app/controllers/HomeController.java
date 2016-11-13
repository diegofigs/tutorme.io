package controllers;

import io.jsonwebtoken.impl.crypto.MacProvider;
import models.Student;
import models.Tutor;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.db.Database;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    @Inject FormFactory formFactory;

    private Database db;

    @Singleton
    private static final Key key = MacProvider.generateKey();
    private static final long ttl = 1296000000;

    @Inject
    public HomeController(Database db) {
        this.db = db;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(angular.render());
    }


    @Transactional
    public Result postRegister() {
        Form<User> userForm = formFactory.form(User.class);
        User user = userForm.bindFromRequest().get();

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "INSERT INTO users(firstname, lastname, email, password, type)" +
                    "VALUES ( ?, ?, ?, ?, ?)" +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getType());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("User registered!");
                Long id = rs.getLong("id");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Integer type = rs.getInt("type");

                if(type == 0){
                    statement = "INSERT INTO tutors(id) VALUES (?)";
                    preparedStatement = conn.prepareStatement(statement);
                    preparedStatement.setLong(1, id);
                    preparedStatement.executeUpdate();
                    user = new Tutor(firstname, lastname, email, password);
                }
                else{
                    statement = "INSERT INTO students(id) VALUES (?)";
                    preparedStatement = conn.prepareStatement(statement);
                    preparedStatement.setLong(1, id);
                    preparedStatement.executeUpdate();
                    user = new Student(firstname, lastname, email, password);
                }
                user.setId(id);
                return ok(Json.toJson(user));
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

    @Transactional
    public Result postLogin() {
        Form<User> userForm = formFactory.form(User.class);
        User user = userForm.bindFromRequest().get();

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "SELECT * FROM users WHERE email = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, user.getEmail());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("User found!");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Integer type = rs.getInt("type");

                if(type == 0){
                    user = new Tutor(firstname, lastname, email, password);
                }
                else{
                    user = new Student(firstname, lastname, email, password);
                }
                user.setId(rs.getLong("id"));
                return ok(Json.toJson(user));
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
