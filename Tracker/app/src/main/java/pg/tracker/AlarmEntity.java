package pg.tracker;

/**
 * Created by Osaka on 07.06.2018.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AlarmEntity {

    public String alarm;
    public int id;
    public AlarmEntity() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm, int id) {
        this.alarm = alarm;
        this.id = id;
    }

    public AlarmEntity(String alarm, int id) {
        this.alarm = alarm;
        this.id = id;
    }

}


