package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.media.jfxmedia.logging.Logger;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    @Inject FormFactory formFactory;
    @Inject HttpExecutionContext httpExecutionContext;

    private JPAApi jpaApi;

    @Singleton
    private static final Key key = MacProvider.generateKey();
    private static final long ttl = 1296000000;

    @Inject
    public HomeController(JPAApi api) {
        this.jpaApi = api;
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

        Query query = jpaApi.em().createNativeQuery("INSERT INTO users(firstname, lastname, email, password) " +
                "VALUES ( ?1, ?2, ?3, ?4)" +
                "RETURNING *", User.class)
                .setParameter(1, user.getFirstname())
                .setParameter(2, user.getLastname())
                .setParameter(3, user.getEmail())
                .setParameter(4, user.getPassword());
        user = (User) query.getSingleResult();
        if(user != null){
            play.Logger.info(Json.toJson(user).toString());
            return ok(Json.toJson(user));
        }
        else{
            return badRequest();
        }

    }

    @Transactional
    public Result postLogin() {
        Form<User> userForm = formFactory.form(User.class);
        User user = userForm.bindFromRequest().get();
        Query query = jpaApi.em().createNativeQuery("SELECT * FROM users WHERE email = ?1", User.class)
                .setParameter(1, user.getEmail());
        user = (User)query.getSingleResult();
        if(user != null){
            play.Logger.info(Json.toJson(user).toString());
            return ok(Json.toJson(user));
        }
        else {
            return notFound();
        }
    }

}
