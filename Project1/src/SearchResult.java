
// Node for search result
public class SearchResult {
	private boolean found;
	private long nCompare;
	
	public SearchResult(boolean found, long nCompare) {
		this.found = found;
		this.nCompare = nCompare;
	}
	
	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public long getnCompare() {
		return nCompare;
	}

	public void setnCompare(long nCompare) {
		this.nCompare = nCompare;
	}
	
}
