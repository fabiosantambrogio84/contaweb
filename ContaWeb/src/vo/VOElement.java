package vo;

import java.io.Serializable;

public class VOElement implements Serializable {
	
	private static final long serialVersionUID = -1709739756251524832L;
	
	static public final int NORMAL = 0;
	static public final int WARNING = 1;
	static public final int ERROR = 2;
	static public final int STORED = 3;
	static public final int NOT_ACTIVE = 4;
	
	public VOElement() {
	}
	
	/*public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}*/
	
	public Integer getStato() {
		return NORMAL;
	}

	protected Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
