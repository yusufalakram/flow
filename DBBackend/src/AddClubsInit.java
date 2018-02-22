
public class AddClubsInit {
    FlowBackend fb;
    private void run(){
        fb = new FlowBackend();
        fb.connect();
        fb.newDBUser("Michael", "Cobb", "test@test.com", "password", null);
        //System.out.println(fb.updateUserLocation(8, -34, -64));
        //System.out.println(fb.authenticateUser("test@test.com", "password"));
        //fb.updateUserPassword(8, "password");
        //System.out.println(fb.authenticateUser("test@test.com", "password"));
    }

    private void addClub(){

    }

    public static void main(String[] args){
        AddClubsInit app = new AddClubsInit();
        app.run();
    }
}
