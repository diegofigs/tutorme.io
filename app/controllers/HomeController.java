package controllers;

import models.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import views.html.*;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    @Inject FormFactory formFactory;
    @Inject HttpExecutionContext httpExecutionContext;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("TutorMe.io", null));
    }

    public CompletionStage<Result> getRegister() {
        return CompletableFuture.supplyAsync(() -> {
            return ok(register.render("TutorMe.io - Register"));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> getLogin() {
        return CompletableFuture.supplyAsync(() -> {
            return ok(login.render("TutorMe.io - Login"));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> postRegister() {
        return CompletableFuture.supplyAsync(() -> {
            return User.create(formFactory.form(User.class));
        }, httpExecutionContext.current()).thenApply(success -> {
            if(!success){
                return redirect(routes.HomeController.getRegister());
            }
            return redirect(routes.HomeController.getHome());
        });
    }

    public CompletionStage<Result> postLogin() {
        return CompletableFuture.supplyAsync(() -> {
            DynamicForm loginForm = formFactory.form().bindFromRequest();
            String email = loginForm.get("email");
            String password = loginForm.get("password");
            return User.authenticate(email, password);
        }, httpExecutionContext.current()).thenApply(success -> {
            if(!success){
                return notFound();
            }
            else {
                return redirect(routes.HomeController.getHome());
            }
        });
    }

    public Result getHome() {
        User user = User.get(session().get("email"));
        return ok(home.render("TutorMe.io - Home", user));
    }


}
