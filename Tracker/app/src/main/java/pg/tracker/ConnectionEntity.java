package pg.tracker;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ConnectionEntity {

    public String firstEmail;
    public String secondEmail;

    public String getFirstEmail() {
        return firstEmail;
    }

    public void setFirstEmail(String firstEmail) {
        this.firstEmail = firstEmail;
    }

    public String getSecondEmail() {
        return secondEmail;
    }

    public void setSecondEmail(String secondEmail) {
        this.secondEmail = secondEmail;
    }

    public ConnectionEntity() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ConnectionEntity(String firstEmail, String secondEmail) {
        this.firstEmail = firstEmail;
        this.secondEmail = secondEmail;
    }

}
