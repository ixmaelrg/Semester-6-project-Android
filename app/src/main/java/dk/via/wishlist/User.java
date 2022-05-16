package dk.via.wishlist;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String password;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.password = email;
    }

}
