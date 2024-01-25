package eu.telecomnancy.test.base;

public class AdRow extends AnnonceRow{

	private int Id;
	private boolean isMaterial;
	private String Localization;
	private String ProfileName;
	private String Type;
	private String info;
	
	public int getId() {
		return Id;
	}
	public void setId(int Id) {
		this.Id = Id;
	}
	public boolean isMaterial() {
		return isMaterial;
	}
	public void setMaterial(boolean isMaterial) {
		this.isMaterial = isMaterial;
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

	
	public AdRow( int Id, boolean isMaterial, String localization, String profileName, String type, String info ) {
		this.Id = Id;
		this.isMaterial = isMaterial;
		this.Localization = localization;
		this.ProfileName = profileName;
		this.Type = type;
		this.info = info;
	}
}
