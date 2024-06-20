//Declaration of Linked List
public class FileLinkedList {
	private FileManager fm;
	private int pageSize;
	LinkedList page;
	
	public FileLinkedList(String fileName, int pageSize) {
		fm = new FileManager(fileName, pageSize);
		this.pageSize = pageSize;
	}
	
	//Inserting new point 
	public void insert(int[] point) {
		boolean isNewPage = false;

		//Creating new page for the insertion of the first point
		if(fm.getLength() == 0) {
			isNewPage = true;
			page = new LinkedList();
		}
		else {
			//Reading current page to check for availability for the insertion of the point (x,y)
			page = fm.readPage(fm.getLength() - pageSize);
			if(page.getLength() == pageSize/8) { //if page is full, create new page
				isNewPage = true;
				page = new LinkedList();
			}
		}
		//insertion of point
		page.insert(point);
		//filling the page with (-1, -1) for size of the page to be equal to the buffer size
		page = fillPage(page);
		if(isNewPage)
			fm.writePage(fm.getLength(), page); //when page is full or no pages exist
		else
			fm.writePage(fm.getLength() - pageSize, page); //when page isn't full 
	}
	
	//Searching
	public SearchResult search(int[] point) {
		SearchResult sr;
		//nReads is the number of "reads" of pages 
		int nReads = 0;
		if(fm.getLength() == 0) //if file is empty
			return new SearchResult(false, nReads);
		page = fm.readPage(0);
		nReads++;
		sr = page.search(point);
		sr.setnCompare(nReads); //putting the value of nReads instead of nCompare (= number of comparisons )
		//The search result needed is the num of reads of the file
		
		//if search is successful
		if(sr.isFound())
			return sr;
		
		//If the search isn't successful in the 1st page
		while(fm.getPosition() != fm.getLength()) {			
			page = fm.readPage(-1);	//Argument '-1' is flag for "read the next page"
			nReads++;
			sr = page.search(point);
			sr.setnCompare(nReads);
			if(sr.isFound())
				return sr;
		}
		return new SearchResult(false, nReads);
	}
	
	//Fill page with (-1, -1)
	private LinkedList fillPage(LinkedList page) {
		while(page.getLength() < pageSize/8) {
			page.insert(new int[] {-1, -1});
		}
		return page;
	}
}
