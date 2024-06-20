import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

//File Manager is used for creation of files, reading and writing, serializing and deserializing of data
public class FileManager {
	private RandomAccessFile file;
	private int bufferSize;
	
	//Creating new file
	public FileManager(String fileName, int bufferSize) {
		try {
			file = new RandomAccessFile(fileName, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.bufferSize = bufferSize;
	}
	
	//Reads the page in the position indicated 
	public LinkedList readPage(long position) {
		byte[] buffer = new byte[bufferSize];	//Creating a static buffer, byte array
		try {
			if(position != -1) //flag used for reading at the desired position
				file.seek(position);
			file.read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return deserializePoints(buffer);
	}
	
	//Writing of page at the position indicated
	public void writePage(long position, LinkedList list) {
		try {
			file.seek(position);
			file.write(serializePoints(list));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long getLength() {
		try {
			return file.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public long getPosition() {
		try {
			return file.getFilePointer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	
	//Serialization
	//Creation of different byte buffer for use of its methods 
	private byte[] serializePoints(LinkedList list) {
	    java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(bufferSize);
	    Node n = list.getHead();
	    for(int i = 0; i < list.getLength(); i++) {
	    	bb.putInt(n.getPoint()[0]);
	    	bb.putInt(n.getPoint()[1]);
	    	n = n.getNext();
	    }
	    return bb.array();
	}
	
	//Deserialization
	//Begins the deserialization process of the buffer
	private LinkedList deserializePoints(byte[] buffer) {
		LinkedList list = new LinkedList();
		java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(bufferSize);
		bb.put(buffer);
		bb.position(0);
		int[] point = null;
		while(bb.hasRemaining()) {
			
			point = new int[2];
	    	point[0] = bb.getInt();
	    	point[1] = bb.getInt();
	    	if(point[0] == -1) break;	//in case the 'x' portion of point (x,y) is -1, it stops
	    	list.insert(point);
	    	
		}
	    return list;
	}
	
	//For the purpose of writing the contents of the hashtable in the file
	public void writeLong( long position, long value) {
		try {
			file.seek(position);
			file.write(longToBytes(value));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//For the purpose of reading the contents of the hashtable in the file
	public long readLong(long position) {
		byte[] buffer = new byte[Long.BYTES];
		try {
			file.seek(position);
			file.read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -2;	//-1 is already used as initialized value to be rewritten
		}
		return bytesToLong(buffer);
	}
	
	//Serialization of long
	private byte[] longToBytes(long value) {
		java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(Long.BYTES);
	    bb.putLong(value);
	    return bb.array();
	}
	
	//Deserialization of long
	private long bytesToLong(byte[] buffer) {
		java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(Long.BYTES);
		bb.put(buffer);
		bb.position(0);
	    return bb.getLong();
	}
}
