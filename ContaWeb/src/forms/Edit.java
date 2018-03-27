package forms;

// import javax.servlet.ServletContext;

import web.GenericAction;

abstract public class Edit extends GenericAction {

	private static final long serialVersionUID = 1L;

	private String action = null;
	protected Integer id = null;
	
	protected final static String ERROR_DELETE = "error_delete";

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}

	abstract protected String store();
	abstract protected String delete();

	public String execute() throws Exception {
		
		//ELEMENT E' STATO VALIDATO. A SECONDA DELLA CHIAMATA (var. action)
		//DECIDO QUALE OPERAZIONE EFFETUARE.
		
		if (action.equalsIgnoreCase("insert") || action.equalsIgnoreCase("edit"))
			return store();
			
		if (action.equalsIgnoreCase("delete"))
			return delete();
		
		return INPUT;
   }
}