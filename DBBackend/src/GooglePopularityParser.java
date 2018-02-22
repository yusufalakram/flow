
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class GooglePopularityParser {

    private String APIKey = "AIzaSyA4KkumsMikRcAOJBeFyVqqV7CZ07ZPLM4"; // or "AIzaSyB6HncKpAXHKvTb3ydvwpHwhVItnlK3DIQ"

	public enum logType {MESSAGE, WARNING, ERROR, JAVA_EXCEPTION}

	public void logMessage(logType type, String message){
        String time = new SimpleDateFormat("[HH:mm:ss ").format(Calendar.getInstance().getTime());
        switch (type){
            case MESSAGE:
                System.out.println(time + "!!] " + message);
                break;
            case WARNING:
                System.out.println(time + "WW] " + message);
                break;
            case ERROR:
                System.out.println(time + "EE] " + message);
                break;
            case JAVA_EXCEPTION:
                System.out.println("===================================");
                System.out.println(time + "EE] JAVA EXCEPTION\n" + message);
                break;
		}

	}

	private String buildCommand (String[] types, double[] location1, double[] location2) throws Exception{
        StringBuilder command = new StringBuilder();
        command.append("python getPopularity.py ");
        command.append(APIKey);
        command.append(" ");
        for (int i = 0; i < types.length; i++){
            command.append(types[i]);
            if(i < types.length-1){
                command.append(",");
            }

        }
        command.append(" ");
        if(location1.length==2 && location2.length == 2){
            command.append(location1[0]);
            command.append(",");
            command.append(location1[1]);
            command.append(" ");
            command.append(location2[0]);
            command.append(",");
            command.append(location2[1]);

        } else {
            throw new Exception("Locations specified must be lists of 2 doubles! (lat, lon)");
        }
        return command.toString();
    }

	public Collection<GoogleClub> runPythonClubGetter(String[] types, double[] location1, double[] location2) throws Exception{
	    //build the command required to pass to the python program
        String cmd = buildCommand(types, location1, location2);

        //create JSON parser to handle returned data from Google
        GsonBuilder b = new GsonBuilder();
        Gson gson = b.create();
        Type Gcoll = new TypeToken<Collection<GoogleClub>>(){}.getType();

        Process p = Runtime.getRuntime().exec( cmd );
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        //read the JSON into a collection of 'GoogleClub'
        Collection<GoogleClub> cc = gson.fromJson(br, Gcoll);

        //wait for the python script to finish
        p.waitFor();
        //close the BufferedReader
        br.close();
        p.destroy();
        if(cc == null){
            throw new Exception("Something went wrong parsing python script! Command was: " +cmd);
        }
        return cc;
	}

	private void run(){
        try {
            logMessage(logType.MESSAGE, "Python script started...");

            Collection<GoogleClub> result = runPythonClubGetter(new String[] {"night_club"}, new double[] {51.411663, -2.422927}, new double[] {51.347387, -2.303463});

            logMessage(logType.MESSAGE, "Python script completed successfully!");

            System.out.println(result);
        } catch (Exception e){
            logMessage(logType.JAVA_EXCEPTION, "Exception occurred while running python script!");
            e.printStackTrace();
        }
    }
	
	public static void main(String args[]){
        GooglePopularityParser App = new GooglePopularityParser();
        App.run();
	}
}
