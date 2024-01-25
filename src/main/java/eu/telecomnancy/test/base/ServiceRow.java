package eu.telecomnancy.test.base;

public class ServiceRow extends AnnonceRow{

	private int serviceId;
	private String Localization;
	private String ProfileName;
	private String Type;
	private String info;
	private boolean isPonctual;
	
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
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

	
	public ServiceRow( int sId, String localization, String profileName, String type, String info,boolean isPonctual ) {
		this.serviceId = sId;
		this.Localization = localization;
		this.ProfileName = profileName;
		this.Type = type;
		this.info = info;
		this.isPonctual=isPonctual;
	}
}


