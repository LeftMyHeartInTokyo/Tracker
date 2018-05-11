package pg.tracker;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserEntity {

    public String user;

    public UserEntity() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public UserEntity(String user) {
        this.user = user;
    }

}
