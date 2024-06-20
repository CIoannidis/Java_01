
public class Hashing {
	private int M;
	private int N;
	private LinkedList[] hashTable;
	
	public Hashing(int M, int N) {
		this.M = M;
		this.N = N;
		hashTable = new LinkedList[M];
		for(int i = 0; i < M; i++) {
			hashTable[i] = new LinkedList();
		}
	}
	
	public void insert(int[] point) {
		int arrayPointer = (int)(point[0]*N + point[1]) % M; // Points to where the point is going to be inserted
		hashTable[arrayPointer].insert(point);	//Insert (x,y) in the list of arrayPointer 
		}
	
	public SearchResult search(int[] point) {
		int arrayPointer = (point[0]*N + point[1]) % M; //calculkation of pointer, hash
		return hashTable[arrayPointer].search(point);
	}
	
	
}
