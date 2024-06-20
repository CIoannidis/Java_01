

//Declaration of node
public class Node {
	private int[] point;
	private Node next;
	
	public Node(int[] point, Node next) {
		this.point = point;
		this.next = next;
	}
	
	public int[] getPoint() {
		return point;
	}
	
	public Node getNext() {
		return next;
	}
	
	public void setPoint(int[] point) {
		this.point = point;
	}
	
	public void setNext(Node next) {
		this.next = next;
	}
}
