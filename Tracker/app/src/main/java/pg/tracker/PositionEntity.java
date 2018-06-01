package pg.tracker;

/**
 * Created by Osaka on 01.06.2018.
 */
public class PositionEntity {
    public Double latitude;
    public Double longitude;

    public PositionEntity() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getPositionEntity() {
        return latitude.toString() + " " + longitude.toString();
    }

    public void setUser(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PositionEntity(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}


