package controllers;

import models.User;
import play.data.FormFactory;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    @Inject FormFactory formFactory;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render());
    }

    public Result getRegister() {
        return ok(register.render());
    }

    public Result postRegister() {
        Form<User> userForm = formFactory.form(User.class);
        User user = userForm.bindFromRequest().get();
        return ok("Hello " + user.getFirstName() + " " + user.getLastName());
    }

}
