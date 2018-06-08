package pg.tracker;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CheckPointEntity {

    public String name;
    public String user;
    public String color;
    public String alarm;
    public PositionEntity position;
    public CheckPointEntity() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CheckPointEntity(String name, String color, String alarm, Double latitude, Double longitude, String user) {
        this.name = name;
        this.user = user;
        this.color = color;
        this.alarm = alarm;
        this.position = new PositionEntity(latitude,longitude);
    }

}
