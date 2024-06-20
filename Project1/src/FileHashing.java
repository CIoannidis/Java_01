//Declaration of File Hashing
//Hashtable was implemented in a file
public class FileHashing {
	private FileManager fm;   //FileManager
	private FileManager fmHT; //FileManagerHashTable
	private int pageSize;
	int N, M;
	LinkedList page;
	
	public FileHashing(String fileName, int pageSize, int N, int M) {
		fm = new FileManager(fileName, pageSize);
		fmHT = new FileManager("HashTable.txt", 0);	//Creation of file of hashTable 
		this.pageSize = pageSize;
		for(int i = 0; i < 2*M; i++) {	//Number of loops is 2*M because (M,M) = (heads, tails) are ran linearly  
			fmHT.writeLong(i*Long.BYTES, -1);	//Initialization with -1 of both heads and tails
		}
		this.N = N;
		this.M = M;
	}
	
	//Insertion
	public void insert(int[] point) {
		boolean isNewPage = false;
		int arrayPointer = (int)((point[0]*N + point[1]) % M);	//Hashing
		
		if(fmHT.readLong(arrayPointer*2*Long.BYTES) == -1) { //Check if head exists at this position
			
			isNewPage = true;
			page = new LinkedList();
			fmHT.writeLong(arrayPointer*2*Long.BYTES, fm.getLength());	//writing of head for the 1st time
			fmHT.writeLong((arrayPointer*2+1)*Long.BYTES, fm.getLength());	//writing of tail, is identical to head for this 1st time
		}
		else {
			page = fm.readPage(fmHT.readLong((arrayPointer*2+1)*Long.BYTES));	//Reading current page to check for availability for the insertion of the point (x,y)
			if(page.getLength()+1 == pageSize/8) {	//if it's full
				isNewPage = true;	//creating new page
				page.insert(new int[] {(int)(fm.getLength() >> 32), (int)fm.getLength()});	//Putting in the old page the position of overflow page which, everytime, is the EOF
				//The position of overflow of page is broken in 2 pieces (2 ints) so we can use the list node
				fm.writePage(fmHT.readLong((arrayPointer*2+1)*Long.BYTES), page); //overwrite the old page because it now has the position of overflow page
				page = new LinkedList();
				fmHT.writeLong((arrayPointer*2+1)*Long.BYTES, fm.getLength()); // writing the tail and defining it as EOF 
			}
		}
		page.insert(point);
		
		page = fillPage(page);
		if(isNewPage)
			fm.writePage(fm.getLength(), page);	//writing the new page at the EOF
		else
			fm.writePage(fmHT.readLong((arrayPointer*2+1)*Long.BYTES), page);	//overwriting on a preexisting page  
	}
	
	private LinkedList fillPage(LinkedList page) {
		while(page.getLength() < pageSize/8) {
			page.insert(new int[] {-1, -1});
		}
		return page;
	}
	
	public SearchResult search(int[] point) {
		SearchResult sr;
		int nReads = 0;
		long position;
		if(fm.getLength() == 0)	//if hashtable doesn't have a head at all, meaning the file is completely empty
			return new SearchResult(false, nReads);
		int arrayPointer = (int)(point[0]*N + point[1]) % M;	//calculating the exact position on the hashtable
		if(fmHT.readLong(arrayPointer*2*Long.BYTES) == -1) return new SearchResult(false, nReads);	//checking if there is a head at this specific position in the hashtable
		page = fm.readPage(fmHT.readLong(arrayPointer*2*Long.BYTES)); //reading the head of a specific position of the hashtable
		nReads++;
		sr = page.search(point);
		
		//Last 8 bytes are reserved for the pointer for the next overflow page, 
		//so this check is made to calculate for the chance that those 8 bytes are the same as the point we're looking for. (Explained before)
		if(sr.isFound() && sr.getnCompare() != pageSize/8) {
			sr.setnCompare(nReads);
			return sr;
		}
		
		//If this check is true, only one page exists and there's no need to continue
		if(fmHT.readLong(arrayPointer*2*Long.BYTES) == fmHT.readLong((arrayPointer*2+1)*Long.BYTES)) return new SearchResult(false, nReads);
		do {
			position = (long)((page.getTail().getPoint()[0] << 32) | (page.getTail().getPoint()[1] & 0xFFFFFFFFL));	//recovering the position that was split in 2 ints from a long
			page = fm.readPage(position);
			nReads++;
			sr = page.search(point);
			
			if(sr.isFound() && sr.getnCompare() != pageSize/8) {
				sr.setnCompare(nReads);
				return sr;
			}
		}while(position != fmHT.readLong((arrayPointer*2+1)*Long.BYTES)); //while the list still has pages in the file
		return new SearchResult(false, nReads);
	}
}
