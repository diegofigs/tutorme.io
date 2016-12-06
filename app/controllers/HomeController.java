package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.impl.crypto.MacProvider;
import models.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

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
        JsonNode req = request().body().asJson();
        String email = req.findPath("email").textValue();
        String password = req.findPath("password").textValue();

        Logger.info("Email: " + email);
        Logger.info("Password: " + password);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "SELECT * FROM users WHERE email = ? AND password = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("User found!");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                Integer type = rs.getInt("type");
                User user;
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
        return notFound();
    }

    @Transactional
    public Result getUsersList() {
        HashMap<String, String> userList = new HashMap<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "SELECT firstname, lastname, email FROM users";
            preparedStatement = conn.prepareStatement(statement);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Logger.info("User added to list");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");

                userList.put(email, firstname + " " + lastname);
            }
            return ok(Json.toJson(userList));
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
    public Result getMessages(String email) {
        ArrayList<Message> messages = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "SELECT * FROM messages WHERE fromEmail = ? OR toEmail = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);
            ResultSet rs = preparedStatement.executeQuery();


            while(rs.next()){
                Logger.info("Message added to list");
                Long id = rs.getLong("id");
                String toEmail = rs.getString("toEmail");
                String fromEmail = rs.getString("fromEmail");
                String messageText = rs.getString("messageText");
                String date = rs.getString("date");

                messages.add(new Message(id, toEmail, fromEmail, messageText, sdf.parse(date)));
            }
            return ok(Json.toJson(messages));
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

    @Transactional
    public Result postMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date now = new Date();
        JsonNode req = request().body().asJson();
        String fromEmail = req.findPath("fromEmail").textValue();
        String toEmail = req.findPath("toEmail").textValue();
        String text = req.findPath("text").textValue();

        Logger.info("From: " + fromEmail);
        Logger.info("To: " + toEmail);
        Logger.info("Text: " + text);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement =  "INSERT INTO messages(fromEmail, toEmail, messageText, date) "+
                                "VALUES (?, ?, ?, ?) " +
                                "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, fromEmail);
            preparedStatement.setString(2, toEmail);
            preparedStatement.setString(3, text);
            preparedStatement.setString(4, sdf.format(now));

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Message Sent!");
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
        return notFound();
    }

    public Result getWall(Long sectionId){
        return ok(angular.render());
    }

    public Result getWallPosts(Long sectionId){
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

    @Transactional
    public Result getLessons(Long sid){
        ArrayList<Lesson> lessons = new ArrayList<>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "SELECT DISTINCT * FROM lessons as l, sections as s WHERE s.id = ? AND s.id = l.sid";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, sid);
            ResultSet rs = preparedStatement.executeQuery();
           while(rs.next()){
                Logger.info("Lesson found!");
                Lesson newLesson = new Lesson(rs.getLong("lid"), rs.getString("name"));
               lessons.add(newLesson);
                String docStatement = "SELECT DISTINCT * FROM documents WHERE lid = ? ";
                PreparedStatement docPreparedStatement = conn.prepareStatement(docStatement);
                docPreparedStatement.setLong(1, newLesson.getID());
                ResultSet drs = docPreparedStatement.executeQuery();

                while(drs.next()){
                    Logger.info("Doc found!");

                    Document newDocument = new Document(drs.getLong("did"), drs.getString("dtitle"), drs.getString("ddescription"), drs.getString("dpath"));
                    newLesson.addDocument(newDocument);
                }

                String aStatement = "SELECT DISTINCT * FROM assignments WHERE lid = ? ";
                PreparedStatement aPreparedStatement = conn.prepareStatement(aStatement);
                aPreparedStatement.setLong(1, newLesson.getID());
                ResultSet ars = aPreparedStatement.executeQuery();

                while(ars.next()){
                    Logger.info("As found!");

                    Assignment newAssignment = new Assignment(ars.getLong("aid"), ars.getString("atitle"), ars.getDate("deadline"), ars.getString("adescription"), ars.getString("apath"));
                    newLesson.addAssignment(newAssignment);
                }

                String vStatement = "SELECT DISTINCT * FROM videos WHERE lid = ? ";
                PreparedStatement vPreparedStatement = conn.prepareStatement(vStatement);
                vPreparedStatement.setLong(1, newLesson.getID());
                ResultSet vrs = vPreparedStatement.executeQuery();

                while(vrs.next()){
                    Logger.info("Vid found!");
                    Video newVideo= new Video(vrs.getLong("vid"), vrs.getString("vtitle"),vrs.getString("URL"));
                    newLesson.addVideo(newVideo);

                    String cStatement = "SELECT DISTINCT * FROM comments WHERE videoId = ? ORDER BY id ";
                    PreparedStatement cPreparedStatement = conn.prepareStatement(cStatement);
                    cPreparedStatement.setLong(1, newVideo.getID());
                    ResultSet crs = cPreparedStatement.executeQuery();

                    while(crs.next()){
                        Logger.info("Comment found!");
                        Comment newComment = new Comment(crs.getLong("id"), crs.getLong("videoId"), crs.getString("fromEmail"), crs.getString("text"),
                                crs.getDate("date"), crs.getString("favoriteOf"));
                        newVideo.addComment(newComment);
                    }

                }







            }
            return ok(Json.toJson(lessons));
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
