package eu.telecomnancy.test.base;

public class ServiceRow extends AnnonceRow{

	private int Id;
	private String Localization;
	private String ProfileName;
	private String Type;
	private String info;
	private boolean isPonctual;
	
	public int getId() {
		return Id;
	}
	public void setId(int Id) {
		this.Id = Id;
	}
	public String getLocalization() {
		return Localization;
	}
	public void setProfileName(String name) {
		this.ProfileName = name;
	}
	public String getProfileName() {
		return ProfileName;
	}
	public void setLocalization(String localization) {
		Localization = localization;
	}
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}

	public String getType() {
		return Type;
	}
	
	public void setType(String type) {
		Type = type;
	}

	public boolean isMaterial(){
		return false;
	}
	public boolean isPonctual() {
		return isPonctual;
	}

	
	public ServiceRow( int Id, String localization, String profileName, String type, String info,boolean isPonctual ) {
		this.Id = Id;
		this.Localization = localization;
		this.ProfileName = profileName;
		this.Type = type;
		this.info = info;
		this.isPonctual=isPonctual;
	}
}


