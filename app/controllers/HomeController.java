package controllers;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.impl.crypto.MacProvider;
import models.*;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.db.Database;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.*;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    @Inject FormFactory formFactory;

    @Inject
    MailerClient mailerClient;

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
                Email obj = new Email()
                        .setSubject("Account Confirmation")
                        .setFrom("TutorMe.io <no-reply@tutorme.io>")
                        .addTo(firstname + " " + lastname + " <"+ email +">")
                        // sends text, HTML or both...
                        .setBodyHtml("<html><body><form action=\"http://localhost:9000/users/activate/"+id +"\" method=\"post\">\n" +
                                "<h1> Hello "+ firstname + "</h1>\n" +
                                "<p>You have been registered at TutorMe.io, please confirm your account by clicking <input type=\"submit\"></input></p>" +
                                "</form></body></html>");
                mailerClient.send(obj);
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
    public Result activateUser(Long id) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "UPDATE users " +
                    "SET active = TRUE " +
                    "WHERE id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            Logger.info("User " + id + " Activated!");
            return ok("User Activated!");
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

            String statement = "SELECT * FROM users " +
                    "WHERE email = ? AND password = ? " +
                    "AND active = TRUE";
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
    public Result updateUser(Long id) {
        JsonNode req = request().body().asJson();
        String email = req.findPath("email").textValue();
        String password = req.findPath("password").textValue();
        String firstname = req.findPath("firstname").textValue();
        String lastname = req.findPath("lastname").textValue();
        int type = req.findPath("type").asInt();


        Logger.info("Update User: "+id);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "UPDATE users " +
                    "SET firstname = ?, " +
                    "lastname = ?, " +
                    "email = ?, " +
                    "password = ? " +
                    "WHERE id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
            User user = new User(firstname, lastname, email, password, type);
            user.setId(id);
            Logger.info("User " + id + " Updated!");
            return ok(Json.toJson(user));
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

            String statement = "SELECT * FROM messages WHERE fromEmail = ? OR toEmail = ? ORDER BY id";
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
                                "WHERE sectionId = ? " +
                                "ORDER BY id";
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
    public Result postPost() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date now = new Date();
        JsonNode req = request().body().asJson();
        String fromEmail = req.findPath("fromEmail").textValue();
        String sectionId = req.findPath("sectionId").textValue();
        String text = req.findPath("text").textValue();

        Logger.info("From: " + fromEmail);
        Logger.info("Section: " + sectionId);
        Logger.info("Text: " + text);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement =  "INSERT INTO wallposts(fromEmail, sectionId, text, date, favoriteOf) "+
                    "VALUES (?, ?, ?, ?, '{}') " +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, fromEmail);
            preparedStatement.setInt(2, Integer.parseInt(sectionId));
            preparedStatement.setString(3, text);
            preparedStatement.setString(4, sdf.format(now));

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Post posted!");
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

    @Transactional
    public Result makeFavorite() {
        JsonNode req = request().body().asJson();
        String email = req.findPath("email").textValue();
        Integer pid = req.findPath("pid").intValue();
        String current = req.findPath("current").textValue();

        Logger.info("Post ID: " + pid);
        Logger.info("New fav by: " + email);
        Logger.info("Current: " + current);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement =  "UPDATE wallposts " +
                                "SET favoriteOf = favoriteOf || ? " +
                                "WHERE id = ? " +
                                "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, pid);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Post made favorite!");
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

    @Transactional
    public Result removeFavorite() {
        JsonNode req = request().body().asJson();
        String email = req.findPath("email").textValue();
        Integer pid = req.findPath("pid").intValue();
        String current = req.findPath("current").textValue();

        Logger.info("Post ID: " + pid);
        Logger.info("Remove fav by: " + email);
        Logger.info("Current: " + current);

        String newFavoriteOf = current.replace(email, "");
        newFavoriteOf = newFavoriteOf.replace("{", "");
        newFavoriteOf = newFavoriteOf.replace("}", "");
        newFavoriteOf = newFavoriteOf.replace("\"", "");
        StringBuilder temp = new StringBuilder(newFavoriteOf).reverse();
        temp = new StringBuilder(temp.toString().replaceFirst(",", ""));
        newFavoriteOf = temp.reverse().toString();
        Logger.info(newFavoriteOf);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement =  "UPDATE wallposts " +
                    "SET favoriteOf = ARRAY[?] " +
                    "WHERE id = ? " +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, newFavoriteOf);
            preparedStatement.setInt(2, pid);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Removed favorite!");
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

    @Transactional
    public Result postComment() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date now = new Date();
        JsonNode req = request().body().asJson();
        String fromEmail = req.findPath("fromEmail").textValue();
        Long videoId = req.findPath("videoId").longValue();
        String text = req.findPath("text").textValue();

        Logger.info("From: " + fromEmail);
        Logger.info("VideoId: " + videoId);
        Logger.info("Text: " + text);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement =  "INSERT INTO comments(fromEmail, videoId, text, date, favoriteOf) "+
                    "VALUES (?, ?, ?, ?, '{}') " +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, fromEmail);
            preparedStatement.setLong(2, videoId);
            preparedStatement.setString(3, text);
            preparedStatement.setString(4, sdf.format(now));

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Comment posted!");
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

    @Transactional
    public Result removeFavoriteComment() {
        JsonNode req = request().body().asJson();
        String email = req.findPath("email").textValue();
        Integer cid = req.findPath("cid").intValue();
        Integer vid = req.findPath("vid").intValue();
        String current = req.findPath("current").textValue();

        Logger.info("Comment ID: " + cid);
        Logger.info("Video ID: " + vid);
        Logger.info("Remove fav by: " + email);
        Logger.info("Current: " + current);

        String newFavoriteOf = current.replace(email, "");
        newFavoriteOf = newFavoriteOf.replace("{", "");
        newFavoriteOf = newFavoriteOf.replace("}", "");
        newFavoriteOf = newFavoriteOf.replace("\"", "");
        StringBuilder temp = new StringBuilder(newFavoriteOf).reverse();
        temp = new StringBuilder(temp.toString().replaceFirst(",", ""));
        newFavoriteOf = temp.reverse().toString();
        Logger.info(newFavoriteOf);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement =  "UPDATE comments " +
                    "SET favoriteOf = ARRAY[?] " +
                    "WHERE id = ? " +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, newFavoriteOf);
            preparedStatement.setInt(2, cid);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Removed favorite on comment!");
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

    @Transactional
    public Result makeFavoriteComment() {
        JsonNode req = request().body().asJson();
        String email = req.findPath("email").textValue();
        Integer cid = req.findPath("cid").intValue();
        Integer vid = req.findPath("vid").intValue();
        String current = req.findPath("current").textValue();

        Logger.info("Comment ID: " + cid);
        Logger.info("Video ID: " + vid);
        Logger.info("New fav by: " + email);
        Logger.info("Current: " + current);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement =  "UPDATE comments " +
                    "SET favoriteOf = favoriteOf || ? " +
                    "WHERE id = ? " +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, cid);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Comment made favorite!");
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

    public Result lessons(Long sectionId){
        return ok(angular.render());
    }

    @Transactional
    public Result getLessons(Long sid){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        ArrayList<Lesson> lessons = new ArrayList<>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement crPreparedStatement = null;
        try {
            conn = db.getConnection();

            String statement = "SELECT DISTINCT * FROM lessons as l, sections as s WHERE s.id = ? AND s.id = l.sid";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, sid);
            ResultSet rs = preparedStatement.executeQuery();

            String crStatement = "SELECT DISTINCT * FROM courses as c, sections as s WHERE s.id = ? AND s.course_id = c.id";
            crPreparedStatement = conn.prepareStatement(crStatement);
            crPreparedStatement.setLong(1, sid);
            ResultSet cRs = crPreparedStatement.executeQuery();
            cRs.next();
            String cTitle = cRs.getString("title");

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

                    Document newDocument = new Document(drs.getLong("did"), drs.getString("dtitle"), drs.getString("ddescription"), drs.getString("dpath"), newLesson.getID());
                    newLesson.addDocument(newDocument);
                }

                String aStatement = "SELECT DISTINCT * FROM assignments WHERE lid = ? ";
                PreparedStatement aPreparedStatement = conn.prepareStatement(aStatement);
                aPreparedStatement.setLong(1, newLesson.getID());
                ResultSet ars = aPreparedStatement.executeQuery();

                while(ars.next()){
                    Logger.info("As found!");

                    Assignment newAssignment = new Assignment(ars.getLong("aid"), ars.getString("atitle"), ars.getDate("deadline"), ars.getString("adescription"), ars.getString("apath"),newLesson.getID());
                    newLesson.addAssignment(newAssignment);
                    String subStatement = "SELECT DISTINCT * FROM submissions WHERE aid = ? ";
                    PreparedStatement subPreparedStatement = conn.prepareStatement(subStatement);
                    subPreparedStatement.setLong(1, newAssignment.getID());
                    ResultSet subrs = subPreparedStatement.executeQuery();
                    while(subrs.next()){
                        String eStatement = "SELECT DISTINCT * FROM users WHERE id = "+subrs.getLong("stid");
                        PreparedStatement ePreparedStatement = conn.prepareStatement(eStatement);
                        ResultSet ers = ePreparedStatement.executeQuery();
                        ers.next();
                        Submission newSubmission = new Submission(subrs.getLong("stid"), subrs.getLong("aid"), subrs.getInt("grade"), subrs.getString("spath"));
                        newSubmission.setID(subrs.getLong("suid"));
                        newSubmission.setEmail(ers.getString("email"));
                        newAssignment.addSubmission(newSubmission);
                    }
                }

                String vStatement = "SELECT DISTINCT * FROM videos WHERE lid = ? ";
                PreparedStatement vPreparedStatement = conn.prepareStatement(vStatement);
                vPreparedStatement.setLong(1, newLesson.getID());
                ResultSet vrs = vPreparedStatement.executeQuery();

                while(vrs.next()){
                    Logger.info("Vid found!");
                    Video newVideo= new Video(vrs.getLong("vid"), vrs.getString("vtitle"),vrs.getString("URL"), newLesson.getID());
                    newLesson.addVideo(newVideo);

                    String cStatement = "SELECT DISTINCT * FROM comments WHERE videoId = ? ORDER BY id ";
                    PreparedStatement cPreparedStatement = conn.prepareStatement(cStatement);
                    cPreparedStatement.setLong(1, newVideo.getID());
                    ResultSet crs = cPreparedStatement.executeQuery();

                    while(crs.next()){
                        Logger.info("Comment found!");
                        Comment newComment = new Comment(crs.getLong("id"), crs.getLong("videoId"), crs.getString("fromEmail"), crs.getString("text"),
                                sdf.parse(crs.getString("date")), crs.getString("favoriteOf"));
                        newVideo.addComment(newComment);
                    }

                }







            }
            ArrayList<Object> data = new ArrayList<>();
            data.add(lessons);
            data.add(cTitle);
            return ok(Json.toJson(data));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
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
    public Result postLessons(Long sectionId, String lessonName) {

        Lesson newLesson = new Lesson(lessonName);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "INSERT INTO lessons(name, sid)" +
                    "VALUES ( ?, ?)" +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,newLesson.getName());
            preparedStatement.setLong(2,sectionId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Lesson Added!");
                Long id = rs.getLong("lid");


                newLesson.setId(id);
                return ok(Json.toJson(newLesson));
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
    public Result postVideo(String title, String src, Long lId){
//        String s="https://youtu.be/";
//        if(src.contains(s))
//            src = src.substring(s.length()-1);

        Video newVideo = new Video(title,src, lId);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "INSERT INTO videos(vtitle, URL, lid)" +
                    "VALUES ( ?, ?, ?)" +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,newVideo.getTitle());
            preparedStatement.setString(2,newVideo.getURL());
            preparedStatement.setLong(3,newVideo.getLID());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("New Video Added!");
                Long id = rs.getLong("vid");


                newVideo.setID(id);
                return ok(Json.toJson(newVideo));
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

    public Result uploadDocument(){

        DynamicForm input = formFactory.form().bindFromRequest();
        String title= input.get("title");
        String description = input.get("description");
        Long lid = Long.parseLong(input.get("lid"));
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> doc = body.getFile("file");
        Long id=null;

        Document newDoc = new Document(title,description,null, lid);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "INSERT INTO documents(dtitle, ddescription, lid)" +
                    "VALUES ( ?, ?, ?)" +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,newDoc.getTitle());
            preparedStatement.setString(2,newDoc.getDescription());
            preparedStatement.setLong(3,newDoc.getLId());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("New Document Added!");
                id = rs.getLong("did");


                newDoc.setID(id);
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

        String bucketName     = "tutorme-io.herokuapp.com";
        String keyName        = "doc"+id+"_"+doc.getFilename();
        System.out.println(keyName);

        if (doc != null) {
            String fileName = doc.getFilename();
            String contentType = doc.getContentType();
            File file = doc.getFile();

            AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider(new ProfilesConfigFile(new File("credentials.txt")), "default"));
            try {
                System.out.println("Uploading a new object to S3 from a file\n");
                s3client.putObject(new PutObjectRequest(
                        bucketName, keyName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                String url =s3client.getUrl(bucketName,keyName).toString();
                try {
                    conn = db.getConnection();

                    String statement = "UPDATE documents " +"SET dpath = ?"
                            + " WHERE did = "+id;
                    preparedStatement = conn.prepareStatement(statement);
                    preparedStatement.setString(1,url);
                    newDoc.setPath(url);


                    preparedStatement.executeUpdate();

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

            } catch (AmazonServiceException ase) {
                System.out.println("Caught an AmazonServiceException, which " +
                        "means your request made it " +
                        "to Amazon S3, but was rejected with an error response" +
                        " for some reason.");
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.getStatusCode());
                System.out.println("AWS Error Code:   " + ase.getErrorCode());
                System.out.println("Error Type:       " + ase.getErrorType());
                System.out.println("Request ID:       " + ase.getRequestId());
            } catch (AmazonClientException ace) {
                System.out.println("Caught an AmazonClientException, which " +
                        "means the client encountered " +
                        "an internal error while trying to " +
                        "communicate with S3, " +
                        "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            }

            return ok(Json.toJson(newDoc));
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }


    public Result uploadAssignment(){

        DynamicForm input = formFactory.form().bindFromRequest();
        String title= input.get("title");
        String description = input.get("description");
        java.util.Date dte = new java.util.Date( input.get("date"));
        java.sql.Date date = new java.sql.Date(dte.getYear(), dte.getMonth(), dte.getDate());
        //date.setTime(input.get("time"));
        Long lid = Long.parseLong(input.get("lid"));
        Long id = null;

        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> doc = body.getFile("file");
        Assignment assignment = new Assignment(title,date,description,null, lid);

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "INSERT INTO assignments(atitle, adescription, lid, deadline)" +
                    "VALUES ( ?, ?, ?, ?)" +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,assignment.getTitle());
            preparedStatement.setString(2,assignment.getDescription());
            preparedStatement.setLong(3,assignment.getLid());
            preparedStatement.setDate(4,assignment.getDeadline());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("New Assignment Added!");
                id = rs.getLong("aid");


                assignment.setID(id);
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

        String bucketName     = "tutorme-io.herokuapp.com";
        String keyName        = "ass"+id+"_"+doc.getFilename();
        System.out.println(keyName);


        if (doc != null) {
            String fileName = doc.getFilename();
            String contentType = doc.getContentType();
            File file = doc.getFile();
            AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider(new ProfilesConfigFile(new File("credentials.txt")), "default"));
            try {
                System.out.println("Uploading a new object to S3 from a file\n");
                s3client.putObject(new PutObjectRequest(
                        bucketName, keyName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                String url =s3client.getUrl(bucketName,keyName).toString();
                try {
                    conn = db.getConnection();

                    String statement = "UPDATE assignments " +"SET apath = ?"
                            + " WHERE aid = "+id;
                    preparedStatement = conn.prepareStatement(statement);
                    preparedStatement.setString(1,url);
                    assignment.setPath(url);


                    preparedStatement.executeUpdate();

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

            } catch (AmazonServiceException ase) {
                System.out.println("Caught an AmazonServiceException, which " +
                        "means your request made it " +
                        "to Amazon S3, but was rejected with an error response" +
                        " for some reason.");
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.getStatusCode());
                System.out.println("AWS Error Code:   " + ase.getErrorCode());
                System.out.println("Error Type:       " + ase.getErrorType());
                System.out.println("Request ID:       " + ase.getRequestId());
            } catch (AmazonClientException ace) {
                System.out.println("Caught an AmazonClientException, which " +
                        "means the client encountered " +
                        "an internal error while trying to " +
                        "communicate with S3, " +
                        "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            }
            Logger.info("Path: "+file.getPath());

            return ok(Json.toJson(assignment));
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

    public Result uploadSubmission(){
        DynamicForm input = formFactory.form().bindFromRequest();
        Long stID = Long.parseLong(input.get("stid"));
        Long aID = Long.parseLong(input.get("aid"));
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> doc = body.getFile("file");
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        Submission submission = new Submission(stID,aID,null,null);
        Long id = null;
        try {
            conn = db.getConnection();

            String statement = "INSERT INTO submissions(stid,aid, grade)" +
                    "VALUES ( ?, ?, -1)" +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1,submission.getStID());
            preparedStatement.setLong(2,submission.getaID());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("New Submission Added!");
                id = rs.getLong("suid");


                submission.setID(id);
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
        String bucketName     = "tutorme-io.herokuapp.com";
        String keyName        = "sub"+id+"_"+doc.getFilename();
        if (doc != null) {
            String fileName = doc.getFilename();
            String contentType = doc.getContentType();
            File file = doc.getFile();
            AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider(new ProfilesConfigFile(new File("credentials.txt")), "default"));
            try {
                System.out.println("Uploading a new object to S3 from a file\n");
                s3client.putObject(new PutObjectRequest(
                        bucketName, keyName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                String url =s3client.getUrl(bucketName,keyName).toString();
                try {
                    conn = db.getConnection();

                    String statement = "UPDATE submissions " +"SET spath = ?"
                            + " WHERE suid = "+id;
                    preparedStatement = conn.prepareStatement(statement);
                    preparedStatement.setString(1,url);
                    submission.setPath(url);
                    System.out.println(url);

                    preparedStatement.executeUpdate();

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

            } catch (AmazonServiceException ase) {
                System.out.println("Caught an AmazonServiceException, which " +
                        "means your request made it " +
                        "to Amazon S3, but was rejected with an error response" +
                        " for some reason.");
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.getStatusCode());
                System.out.println("AWS Error Code:   " + ase.getErrorCode());
                System.out.println("Error Type:       " + ase.getErrorType());
                System.out.println("Request ID:       " + ase.getRequestId());
            } catch (AmazonClientException ace) {
                System.out.println("Caught an AmazonClientException, which " +
                        "means the client encountered " +
                        "an internal error while trying to " +
                        "communicate with S3, " +
                        "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            }

            return ok(Json.toJson(submission));


        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }


    @Transactional
    public Result deleteLesson(Long lid) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();

            String statement = "DELETE FROM documents WHERE lid = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, lid);
            int rs = preparedStatement.executeUpdate();

            statement = "SELECT DISTINCT * FROM assignments WHERE lid = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1,lid);
            ResultSet ars = preparedStatement.executeQuery();
            while(ars.next()) {
                statement = "DELETE FROM submissions WHERE aid = ?";
                preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setLong(1, ars.getLong("aid"));
                rs = preparedStatement.executeUpdate();
            }

            statement = "DELETE FROM assignments WHERE lid = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, lid);
            rs = preparedStatement.executeUpdate();
            statement = "SELECT DISTINCT * FROM videos WHERE lid = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1,lid);
            ResultSet vrs = preparedStatement.executeQuery();
            while(vrs.next()) {
                statement = "DELETE FROM comments WHERE videoId = ?";
                preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setLong(1, vrs.getLong("vid"));
                rs = preparedStatement.executeUpdate();
            }
            statement = "DELETE FROM videos WHERE lid = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, lid);
            rs = preparedStatement.executeUpdate();
            statement = "DELETE FROM lessons WHERE lid = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, lid);
            rs = preparedStatement.executeUpdate();
            return ok("Lesson deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
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
    public Result deleteDocument(Long did){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();

            String statement = "DELETE FROM documents WHERE did = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, did);
            int rs = preparedStatement.executeUpdate();
            return ok("Document deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
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
    public Result deleteAssignment(Long aid){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();

            String statement = "DELETE FROM submissions WHERE aid = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, aid);
            preparedStatement.executeUpdate();
            statement = "DELETE FROM assignments WHERE aid = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, aid);
            int rs = preparedStatement.executeUpdate();
            return ok("Assignment deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
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
    public Result deleteVideo(Long vid){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = db.getConnection();
            String statement = "DELETE FROM comments WHERE videoId = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, vid);
            int crs = preparedStatement.executeUpdate();
            statement = "DELETE FROM videos WHERE vid = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, vid);
            int rs = preparedStatement.executeUpdate();
            return ok("Video deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
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
    public Result submitGrade(int grade, Long subId) {

        Submission submission;


        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement = "UPDATE public.submissions SET grade=? WHERE submissions.suid=? RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,grade);
            preparedStatement.setLong(2,subId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Logger.info("Grade Submitted!");
                submission = new Submission(rs.getLong("stid"), rs.getLong("aid"), rs.getInt("grade"), rs.getString("spath"));
                submission.setID(subId);


                return ok(Json.toJson(submission));
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
    public Result makePayment() {
        JsonNode req = request().body().asJson();
        Long fromId = req.findPath("fromId").asLong();
        Long sectionId = req.findPath("sectionId").asLong();
        Double quantity = req.findPath("quantity").asDouble();
        String cardholder = req.findPath("cardholder").textValue();
        Long cardnumber = req.findPath("cardnumber").asLong();
        Integer expirationmonth = req.findPath("expirationmonth").asInt();
        Integer expirationyear = req.findPath("expirationyear").asInt();
        Integer cvv = req.findPath("cvv").asInt();
        Integer toId = null;

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement =  "SELECT tutor_id "+
                    "FROM sections " +
                    "WHERE id = ?";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, sectionId);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                Logger.info("Post posted!");
                toId = rs.getInt("tutor_id");
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

        Logger.info("From: " + fromId);
        Logger.info("To: " + toId);
        Logger.info("$: " + quantity);
        Logger.info("Cardholder: " + cardholder);
        Logger.info("Card Number: " + cardnumber);
        Logger.info("Exp Date: " + expirationmonth + "/" + expirationyear);
        Logger.info("CVV: " + cvv);


        conn = null;
        preparedStatement = null;

        try {
            conn = db.getConnection();

            String statement =  "INSERT INTO payments(fromId, toId, quantity, cardholder, cardnumber, expirationmonth, expirationyear, cvv) "+
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "RETURNING *";
            preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setLong(1, fromId);
            preparedStatement.setLong(2, toId);
            preparedStatement.setDouble(3, quantity);
            preparedStatement.setString(4, cardholder);
            preparedStatement.setLong(5, cardnumber);
            preparedStatement.setInt(6, expirationmonth);
            preparedStatement.setInt(7, expirationyear);
            preparedStatement.setString(8, cvv.toString());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Logger.info("Payment posted!");
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

}
