package flow.backend;

public class Settings {
	private static final int SETTINGS_LENGTH = 3;
	private int UserID;
	private int prefRadius = 5;
	private boolean autoCheckIn;
	private String visibility;
	
	public Settings(int UserID){
		this.UserID = UserID;
		getSettings();
	}
	public int getPrefRadius(){
		return prefRadius;
	}
	public void setPrefRadius(int prefRadius){
		this.prefRadius = prefRadius;
	}
	
	public boolean getAutoCheckIn(){
		return autoCheckIn;
	}
	
	public void setAutoCheckIn(boolean autoCheckIn){
		this.autoCheckIn = autoCheckIn;
	}
	
	public void getSettings(){
		String[] Settings = Backend.db.getUserSettings(UserID);
		for(int i = 0; i < SETTINGS_LENGTH; i++){
			switch(i){
				case 0:
					this.prefRadius = Integer.parseInt(Settings[i]);
				case 1:
					this.autoCheckIn = Boolean.parseBoolean(Settings[i]);
				case 2:
					this.visibility = Settings[i];
			}
		}
	}
	
	public void saveSettings(){
		String prefRadius = String.valueOf(this.prefRadius);
		String autoCheckIn = String.valueOf(this.autoCheckIn);
		String visibility = this.visibility;
		String[] retVal = {prefRadius, autoCheckIn, visibility};
		Backend.db.saveUserSettings(UserID, retVal);
		
	}
}
