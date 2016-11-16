package models;

import javax.persistence.*;

/**
 * Created by diegofigs on 11/2/16.
 */
@Entity
@Table(name = ("sections"))
public class Section extends Course{

    public Section() {

    }

    public Section(String title, String description) {
        super(title, description);
    }
}
