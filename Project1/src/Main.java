
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] K = new int[] {1000, 10000, 30000, 50000, 70000, 100000};
		int N = 2^16;
		int M = 100;
		int bufferSize = 256;
		LinkedList ll = new LinkedList();
		Hashing hash = new Hashing(M, N);
		FileLinkedList fll = new FileLinkedList("data.txt", bufferSize);
		FileHashing fh = new FileHashing("dataH.txt", bufferSize, N, M);
		int[] point;
		int[][] searchArray = new int[100][2];
		SearchResult sr;
		for(int k = 0; k < K.length; k++) {
			int avgLL = 0;
			int avgHash = 0; 
			int avgFLL = 0;
			int avgFH = 0;
			int favgLL = 0;
			int favgHash = 0; 
			int favgFLL = 0;
			int favgFH = 0;
			
			int tLL = 0;
			int tHash = 0; 
			int tFLL = 0;
			int tFH = 0;
			
			ll = new LinkedList();
			hash = new Hashing(M, N);
			fll = new FileLinkedList("data" + K[k] + ".txt", bufferSize);
			fh = new FileHashing("dataH" + K[k] + ".txt", bufferSize, N, M);
			
			for(int i = 0; i < K[k]; i++) {
				point = new int[] {(int)(i*N/K[k]), (int)(i*0.75*N/K[k])};
				ll.insert(point);
				hash.insert(point);
				fll.insert(point);
				fh.insert(point);
				
			}
			
			for(int i = 0; i < 100; i++) {
				point = new int[] {(int)(Math.random()*N), (int)(Math.random()*N)};
				sr = ll.search(point);
				if(sr.isFound()) {
					avgLL += sr.getnCompare();
					tLL++;
				}
				else
					favgLL += sr.getnCompare();
				sr = hash.search(point);
				if(sr.isFound()) {
					avgHash += sr.getnCompare();
					tHash++;
				}
				else
					favgHash += sr.getnCompare();
				sr = fll.search(point);
				if(sr.isFound()) {
					avgFLL += sr.getnCompare();
					tFLL++;
				}
				else
					favgFLL += sr.getnCompare();
				sr = fh.search(point);
				if(sr.isFound())
				{
					avgFH += sr.getnCompare();
					tFH++;
				}
				else
					favgFH += sr.getnCompare();
			}
			avgLL /= tLL;
			avgHash /= tHash;
			avgFLL /= tFLL;
			avgFH /= tFH;
			favgLL /= 100-tLL;
			favgHash /= 100-tHash;
			favgFLL /= 100-tFLL;
			favgFH /= 100-tFH;
			System.out.println("\nFor K = " + K[k]);
			System.out.println("True Average of Linked List: " + avgLL);
			System.out.println("True Average of Hashing: " + avgHash);
			System.out.println("True Average of File Linked List: " + avgFLL);
			System.out.println("True Average of File Hashing: " + avgFH);
			
			System.out.println("False Average of Linked List: " + favgLL);
			System.out.println("False Average of Hashing: " + favgHash);
			System.out.println("False Average of File Linked List: " + favgFLL);
			System.out.println("False Average of File Hashing: " + favgFH);
			
		}
	}

}
