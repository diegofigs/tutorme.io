package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Singleton;
import javax.validation.Constraint;
import java.util.*;

import static play.mvc.Http.Context.Implicit.session;
import static play.mvc.Results.ok;

/**
 * Created by diegofigs on 10/4/16.
 */
public class User {

    @Singleton
    private static Map<String, User> userMap = new HashMap<>();

    private String firstName;
    private String lastName;

    @Constraints.Required
    private String email;
    private String password;

    public static boolean create(Form<User> userForm) {
        User user = userForm.bindFromRequest().get();
        for(Map.Entry<String, User> u: userMap.entrySet()){
            if(u.getValue().getEmail().equals(user.getEmail())){
                return false;
            }
        }
        userMap.put(user.getEmail(), user);
        session().put("email", user.getEmail());
        session().put("name", user.getFirstName());
        return true;
    }

    public static boolean authenticate(String email, String password) {
        for(Map.Entry<String, User> u: userMap.entrySet()){
            if(u.getValue().getEmail().equals(email) && u.getValue().getPassword().equals(password))
                session().put("email", u.getValue().getEmail());
                session().put("name", u.getValue().getFirstName());
                return true;
        }
        return false;
    }

    public static User get(String email) {
        return userMap.get(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
