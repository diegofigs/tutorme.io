package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.JwtBuilder;
import models.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    @Inject FormFactory formFactory;
    @Inject HttpExecutionContext httpExecutionContext;

    @Singleton
    private static final Key key = MacProvider.generateKey();
    private static final long ttl = 1296000000;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(angular.render());
    }


    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> postRegister() {
        return CompletableFuture.supplyAsync(() -> {
            return User.create(formFactory.form(User.class));
        }, httpExecutionContext.current()).thenApply(user -> {
            if(user == null){
                return notFound();
            }
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DATE, 30);
            Date d = c.getTime();

            // Create token and sign
            String jwt = Jwts.builder()
                    .setSubject(user.getEmail())
                    .setIssuer("TutorMe.io")
                    .setExpiration(d)
                    .signWith(SignatureAlgorithm.HS512, key)
                    .compact();

            ObjectNode res = Json.newObject();
            res.set("user", Json.toJson(user));
            return ok(res);
        });
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> postLogin() {
        return CompletableFuture.supplyAsync(() -> {
            DynamicForm loginForm = formFactory.form().bindFromRequest();
            String email = loginForm.get("email");
            String password = loginForm.get("password");
            return User.authenticate(email, password);
        }, httpExecutionContext.current()).thenApply(user -> {
            if(user == null){
                return notFound();
            }
            else {
                Calendar c = new GregorianCalendar();
                c.add(Calendar.DATE, 30);
                Date d = c.getTime();

                // Create token and sign
                String jwt = Jwts.builder()
                        .setSubject(user.getEmail())
                        .setIssuer("TutorMe.io")
                        .setExpiration(d)
                        .signWith(SignatureAlgorithm.HS512, key)
                        .compact();

                ObjectNode res = Json.newObject();
                res.set("user", Json.toJson(user));
                return ok(res);
            }
        });
    }

}
