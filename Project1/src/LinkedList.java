
//Declaration of linked list
public class LinkedList {
	private Node head;
	private Node tail;
	private long length;
	
	public LinkedList() {
		head = null;
		tail = null;
		length = 0;
	}
	
	public void insert(int[] point) {
		Node newNode = new Node(point, null);
		if(tail == null) { 	// Insert first node in empty list
			head = newNode;
			tail = newNode;
			length++;
			return;
		}
		tail.setNext(newNode);	
		tail = newNode;
		length++;
	}
	
	public SearchResult search(int[] point) {
		long counter = 0;
		Node cursor = head;
		while(cursor != null) {	//Search the list as long as the list isn't over
			if(checkNode(cursor, point))
				return new SearchResult(true, ++counter);	//if true
			counter++;
			cursor = cursor.getNext();	//if false, go to the next
		}
		return new SearchResult(false, counter); // not found
	}
	
	public void printList() {	//printing of list
		Node n = head;
		int[] point;
		while(n != null) {
			point = n.getPoint();
			n = n.getNext();
			System.out.println("(" + point[0] + ", " + point[1] + ")");
		}
	}
	//Comparing the value of node with the requested point (x,y), returns boolean
	private boolean checkNode(Node node, int[] point) {	
		int[] temp = node.getPoint();
		if(temp[0] == point[0] && temp[1] == point[1]) {
			return true;
		}
		return false;
	}
	
	//Declaration of getters and setters
	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}
	
}