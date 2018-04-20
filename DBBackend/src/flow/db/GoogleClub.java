package flow.db;

public class GoogleClub {

	//name of the club
	private String name;
	//address of the  club
	private String address;
	//type of the club, i.e. 'bar' 'night_club'
	private String[] types;
	//latitude of the club
	private double lat;
	//longitude of the club
	private float lon;

	//google rating
	private float rating;

	//number of ratings on google
	private int rating_n;
	
	private GoogleClubPopTimes[] populartimes;
	
	
	public String toString(){
		return rating + " .. " + rating_n;
	}
}
