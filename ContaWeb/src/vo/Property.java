package vo;

public class Property {

	private static final long serialVersionUID = -1709739756251524832L;
	
	static public final int NORMAL = 0;
	static public final int WARNING = 1;
	static public final int ERROR = 2;
	static public final int STORED = 3;
	static public final int NOT_ACTIVE = 4;

	private String key;
	private String value;

	public Property() {
	}
	
	/*public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}*/
	
	public Integer getStato() {
		return NORMAL;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}
