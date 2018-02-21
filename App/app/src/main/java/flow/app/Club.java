package flow.app;

/**
 * Created by Ben Amor on 21/02/2018.
 */
public class Club {

    private String name;
    private double[] location;

    public Club(String name, double[] location) {
        this.name = name;
        this.location = location;
    }

    public Club(String name, double lat, double lon) {
        this.name = name;
        this.location = new double[] {lat, lon};
    }

    public String getName() {
        return name;
    }

    public double[] getLocation() {
        return location;
    }

}
