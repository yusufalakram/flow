package flow.db;
import java.math.BigDecimal;

public class AddClubsInit {
    FlowDBBackend fb;
    private void run(){
        fb = new FlowDBBackend();
        fb.connect();

        int id = 1;//fb.newDBClub("Second Bridge", "a club");

        if(id>0){
            //fb.updateClubLocation(id, new BigDecimal(51.378959),new BigDecimal(-2.3593847));
            //fb.updateClubSafety(id, 65);
            //fb.updateClubBusyness(id, 45);
            //fb.updateClubUsersPresent(id, 350);
            //fb.updateClubQueueTime(id, 25);
        }
        fb.disconnect();
    }

    private void addClub(){

    }

    public static void main(String[] args){
        AddClubsInit app = new AddClubsInit();
        app.run();
    }
}
