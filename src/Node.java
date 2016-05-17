import java.util.HashSet;


public class Node {
	private int x;
	private int y;
	
	
	Node(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	Node(){
		
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String toString(){
		return this.x+"   "+this.y;
	}
	
	public boolean equals(Object obj){
		if (obj instanceof Node){
			Node node = (Node) obj;
			return(x ==node.x && y == node.y);
		}
		return super.equals(obj);
	}
	 public int hashCode(){
		 return x*y;
	 }



}
