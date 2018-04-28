package flow.backend;

import flow.app.Club;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class QueueTime {
	private int queueTime;
	private static final double DEFAULT_RADIUS = 0.005;
	private Club club;
	private Map<Integer, Date> userList = new HashMap<Integer, Date>();
	public QueueTime(Club club){
		this.club = club;
		update();
	}
	
	public int getQueueTime(){
		return this.queueTime;
	}
	
	public void update(){
		updateUsersInQueue();
		
	}
	
	public void updateUsersInQueue(){
		int[] usersNear = Backend.db.getUsersWithinRadius(this.club.getLocation(), DEFAULT_RADIUS);
		long[] newQueueTimes = new long[userList.size()];
		int queueIndex = 0;
		for(int i = 0; i < usersNear.length; i++){
			userList.put(new Integer(usersNear[i]), new Date());
		}
		for(Integer key: userList.keySet()){
			if(!IntStream.of(usersNear).anyMatch(x -> x == key)){
				Date date = new Date();
				Date initialDate = userList.get(key);
				long queueTime = date.getTime() - initialDate.getTime();
				newQueueTimes[queueIndex] = queueTime;
				queueIndex++;
			}
		}
		if(newQueueTimes.length > 0){
			int acc = 0;
			for(int i = 0; i < newQueueTimes.length; i++){
				acc += newQueueTimes[i];
			}
			this.queueTime = acc / newQueueTimes.length;
		}
	}

}
