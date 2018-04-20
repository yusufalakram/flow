package flow.db;

public class GoogleClubPopTimes {
	//day of the week
	protected String name;
	//popularity for each hour of that day  max=100??
	//data[0]=midnight, data[23]= 11pm (i.e. index is 24hour time)
	protected int[] data;
}
