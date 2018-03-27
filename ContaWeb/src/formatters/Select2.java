package formatters;

import java.util.Vector;

public class Select2 {
	private Vector<Select2Result> results;
	private Select2Pagination pagination;
	public Select2Pagination getPagination() {
		return pagination;
	}
	public void setPagination(Select2Pagination pagination) {
		this.pagination = pagination;
	}
	public Vector<Select2Result> getResults() {
		return results;
	}
	public void setResults(Vector<Select2Result> results) {
		this.results = results;
	}
}
