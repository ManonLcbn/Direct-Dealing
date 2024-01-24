package eu.telecomnancy.test.base;

public class ServiceRow {

	private int serviceId;
	private String Localization;
	private String ProfileName;
	private String Type;
	private String info;
	
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

	
	public ServiceRow( int sId, String localization, String profileName, String type, String info ) {
		this.serviceId = sId;
		this.Localization = localization;
		this.ProfileName = profileName;
		this.Type = type;
		this.info = info;
	}
}


