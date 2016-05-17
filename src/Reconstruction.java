import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class Reconstruction {
	double[][] inputfile ;
	int line = 303602;
	int row = 3;
	double[][] densitymap;  //density map (double matrix)
	int xinterval = 10;
	int yinterval = 10;
	double xmin;
	double ymin;
	int xnumber;    // the max location of x-axis
	int ynumber;    // the max location of y-axis
	ArrayList<Node> saddlelist;   //all saddle points locations
	ArrayList<ArrayList<Node>> allpathlist;  //all paths
	ArrayList<Node> maxlist;    // all local maximal points locations
	ArrayList<Node> ridge; //all points which has 3 around lower density points.
	ArrayList<ArrayList<Node>> showpathlist;   // all paths finally are shown on the graph.
	ArrayList<ArrayList<Node>> allconnectedlines;
	
	Reconstruction(){
		inputfile = new double[line][row];
		
		this.saddlelist = new ArrayList<Node>();
		this.allpathlist = new ArrayList<ArrayList<Node>>();
		this.maxlist = new ArrayList<Node>();
		this.ridge = new ArrayList<Node>();
		this.showpathlist = new ArrayList<ArrayList<Node>>();
		this.allconnectedlines = new ArrayList<ArrayList<Node>>();
		
		//read file into inputfile array.
		try {
			this.ReadFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ReadFile() throws FileNotFoundException {
		File inputFile = new File("ptsclr.txt");
		Scanner scanner = new Scanner(inputFile);
		for(int i = 0 ; i < line ; i++){
			for(int j = 0; j < row ; j++){
				inputfile[i][j] = scanner.nextDouble();
			}
		}		
		scanner.close();
	}
	
	public void findBoundary(){
		double xmin = 0;
		double xmax = 0;
		double ymin = 0;
		double ymax = 0;
		//how many points in x-axis and y-axis;
		int xnumber = 0;
		int ynumber = 0;
		//find max and min x;
		xmin = this.inputfile[0][0];
		xmax = this.inputfile[0][0];
		
		//find max and min y
		ymin = this.inputfile[0][1];
		ymax = this.inputfile[0][1];
		
		for(int i = 0 ; i< this.line ; i++){
			if(xmin > this.inputfile[i][0])
				xmin = this.inputfile[i][0];
			if(xmax < this.inputfile[i][0])
				xmax = this.inputfile[i][0];
			if(ymin > this.inputfile[i][1])
				ymin = this.inputfile[i][1];
			if(ymax < this.inputfile[i][1])
				ymax = this.inputfile[i][1];				
		}
		System.out.println(xmin+"   "+xmax+"   "+ymin+"   "+ymax);
		xnumber = (int)(xmax-xmin)/xinterval + 1;
		ynumber = (int)(ymax-ymin)/yinterval + 1;
		System.out.println(xnumber +"    " +ynumber);
		this.xmin = xmin;
		this.ymin = ymin;
		this.xnumber = xnumber;
		this.ynumber = ynumber;
		this.densitymap = new double[xnumber][ynumber];			
	}
	
	
	public void formDensityMap(){
		double xcoordinate ;
		double ycoordinate ;
		double density = 0.0;
		//location in the densitymap;
		int x = 0;
		int y = 0;
		
		for(int i = 0 ; i < line ;i++){
			xcoordinate = this.inputfile[i][0];
			ycoordinate = this.inputfile[i][1];
			density = this.inputfile[i][2];
			
			x = (int)(xcoordinate - this.xmin)/this.xinterval;
			y = (int)(ycoordinate - this.ymin)/this.yinterval;
			this.densitymap[x][y] = density;			
		}		
	}
	
	public void locateSaddlePoint(){
		int saddlenumber = 0 ;
		double center , top , bot , left , right;
		for(int i = 1; i< this.xnumber - 1; i ++){
			for(int j = 1 ; j< this.ynumber - 1; j++){
				center = this.densitymap[i][j];
				top = this.densitymap[i-1][j];
				bot = this.densitymap[i+1][j];
				left = this.densitymap[i][j-1];
				right = this.densitymap[i][j+1];

/*				if(center == 0.0 || top == 0.0 || bot == 0.0 || left == 0.0 || bot == 0.0){
					break;
				}
*/				
				if(top > center && bot > center && left < center && right < center){
					saddlelist.add(new Node(i,j));
					saddlenumber++;
				}
				if(top < center && bot < center && left > center && right > center){
					saddlelist.add(new Node(i,j));
					saddlenumber++;
				}
			}
		}
		System.out.println("number of saddle points:   " + saddlenumber);
		
		//all saddle point locations
//		for(int i = 0 ; i <saddlelist.size();i++){
//			System.out.println(saddlelist.get(i).getX()+"    "+ saddlelist.get(i).getY());
//		}
		System.out.println(this.densitymap[102][450]+"    ");
	}
	
	public void locateMaxPoint(){
		int maximalnumber = 0 ;
		double center , top , bot , left , right;
		for(int i = 1; i< this.xnumber - 1; i ++){
			for(int j = 1 ; j< this.ynumber - 1; j++){
				center = this.densitymap[i][j];
				top = this.densitymap[i-1][j];
				bot = this.densitymap[i+1][j];
				left = this.densitymap[i][j-1];
				right = this.densitymap[i][j+1];
/*				if(center == 0.0 || top == 0.0 || bot == 0.0 || left == 0.0 || bot == 0.0){
					break;
				}
*/				
				if(top < center && bot < center && left < center && right < center){
					this.maxlist.add(new Node(i,j));
					maximalnumber++;
				}
			}
		}
		System.out.println("number of maximal points:   " + maximalnumber);		
		
	}
	
	//find path from the saddle points following the largest density point.
	public void formPath(){
		Node node;
		ArrayList<Node> pathlist1 ;
		ArrayList<Node> pathlist2 ;
		
		for(int i = 0 ; i< this.saddlelist.size();i++){
			pathlist1 = new ArrayList<Node>();
			pathlist2 = new ArrayList<Node>();
			node = saddlelist.get(i);

			pathlist1.add(node);
			pathlist2.add(node);
			int result = 0;
			if(this.densitymap[node.getX()][node.getY()] < this.densitymap[node.getX() + 1][node.getY()] &&
					this.densitymap[node.getX()][node.getY()] < this.densitymap[node.getX() - 1][node.getY()] ){

				node = saddlelist.get(i);
				pathlist1.add(new Node(node.getX() -1, node.getY()));
				node = this.largestPoint(node.getX() -1, node.getY());

			
				while(node != null){
					pathlist1.add(node);
					node = this.largestPoint(node.getX(), node.getY());
				}
				
				this.allpathlist.add(pathlist1);
				
				node = saddlelist.get(i);
				pathlist2.add(new Node(node.getX() + 1, node.getY()));
				node = this.largestPoint(node.getX() + 1, node.getY());
				
				while(node != null){
					pathlist2.add(node);
					node = this.largestPoint(node.getX(), node.getY());
				}
				
				this.allpathlist.add(pathlist2);	
	
			}
			
			node = saddlelist.get(i);
			if(this.densitymap[node.getX()][node.getY()] < this.densitymap[node.getX()][node.getY() + 1] &&
					this.densitymap[node.getX()][node.getY()] < this.densitymap[node.getX()][node.getY() - 1] ){
				
				node = saddlelist.get(i);
				pathlist1.add(new Node(node.getX(), node.getY() - 1));
				node = this.largestPoint(node.getX(), node.getY() - 1);

			
				while(node != null){
					pathlist1.add(node);
					node = this.largestPoint(node.getX(), node.getY());
				}
				
				this.allpathlist.add(pathlist1);
				
				node = saddlelist.get(i);
				pathlist2.add(new Node(node.getX(), node.getY() + 1));
				node = this.largestPoint(node.getX(), node.getY() + 1);
				
				while(node != null){
					pathlist2.add(node);
					node = this.largestPoint(node.getX(), node.getY());
				}
				
				this.allpathlist.add(pathlist2);

			}
		}
	
	}
	
	//form the path from all saddle points following all higher density points.
	public void newFormPath(){
		Node node;
		ArrayList<Node> list;
		ArrayList<Node> result;
		for(int i = 0; i< this.saddlelist.size(); i++){
			node = saddlelist.get(i);
			System.out.println(node.getX() +"  " +node.getY());
			list = new ArrayList<Node>();
			result = new ArrayList<Node>();
			list.add(node);
			result.add(node);
			while(list.size() > 0){
				node = list.get(0);
				list.remove(0);
				list.addAll(this.getAllBiger(node));
				list = this.deleteRepeat(list);
				result.add(node);			
			}
			result = this.deleteRepeat(result);
			this.allpathlist.add(result);
		}
	}
	
	//form the paht from all max points following the closest density neighbor.
	public void formPathMax(){
		Node node;
		ArrayList<Node> templist = new ArrayList<Node>();
		ArrayList<Node> pathlist = new ArrayList<Node>();
		
		
		for(int i = 0 ; i < maxlist.size() ; i++){
			node = this.maxlist.get(i);
		
			templist = new ArrayList<Node>();
			pathlist = new ArrayList<Node>();
			pathlist.add(node);
			templist.add(node);
//			templist.add(new Node(node.getX() + 1,node.getY()));
//			templist.add(new Node(node.getX() - 1,node.getY()));
//			templist.add(new Node(node.getX(),node.getY() + 1));
//			templist.add(new Node(node.getX(),node.getY() - 1));
		
			pathlist.addAll(templist);
		
			while(templist.size() != 0){
				node = templist.get(0);
				templist.remove(0);
				node = this.findNearestDensityPoint(node);
				if(node != null){
					templist.add(node);
					pathlist.add(node);
					templist = this.deleteRepeat(templist);
				}
				pathlist = this.deleteRepeat(pathlist);
//				System.out.println(templist.size()+"    templist");
//				System.out.println(pathlist.size() +"ni da ye");
			}
			this.allpathlist.add(pathlist);
//			System.out.println(this.allpathlist.size()+" "+pathlist.size()+"      ....");
		}
	}
	
// find the two closed max points.
	public void findNearestMax(){
		Node start = new Node();
		Node end = new Node();
		Node nearestone = new Node();
		Node nearesttwo = new Node();
		int distance;
		int mindistance;
		int seconddistance;
		ArrayList<Node> list;
		for(int i = 0 ; i < this.maxlist.size(); i++){
			start = maxlist.get(i);
			mindistance = 1000;
			for(int j = 0 ; j < this.maxlist.size(); j++){
				end = maxlist.get(j);
				distance = this.calculateDistance(start, end);
				if(distance > 0 && distance < mindistance){
					mindistance = distance;
					nearestone = new Node(end.getX(), end.getY());
				}				
			}
			seconddistance = 1000;
			for(int j = 0 ; j < this.maxlist.size(); j++){
				end = maxlist.get(j);
				distance = this.calculateDistance(start, end);
				if(distance > mindistance && distance < seconddistance){
					seconddistance = distance;
					nearesttwo = new Node(end.getX(), end.getY());
				}				
			}
			
			list = new ArrayList<Node>();
			list.add(start);
			list.add(nearestone);
			list.add(nearesttwo);
			this.allpathlist.add(list);
		}		
	}
	
	public int calculateDistance(Node start, Node end){
		int result = 0;
		result = Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
		return result;
	}
	
	public Node findNearestDensityPoint(Node node){
		Node resultnode ;
		if(node.getX() < 1 ||node.getX() > this.xnumber - 2 || node.getY() < 1||node.getY() > this.ynumber -2 )
			return null;
		
		double center = this.densitymap[node.getX()][node.getY()];
		double top = this.densitymap[node.getX()][node.getY() - 1];
		double bot = this.densitymap[node.getX()][node.getY() + 1];
		double left = this.densitymap[node.getX() - 1][node.getY()];
		double right = this.densitymap[node.getX() + 1][node.getY()];
		
		double resulttop = center - top;
		double resultbot = center - bot;
		double resultleft = center - left;
		double resultright = center - right;
//		System.out.println(resulttop+"   "+resultbot+"  "+resultleft+"   "+resultright);
		
		//find minimum number but which is bigger than zero.		
		double result = this.findMinPositive(resulttop, resultbot, resultleft, resultright);
//		System.out.println(result+"result");
		if(result <= 0){
			return null;
		}
		if(result == resulttop){
			resultnode = new Node(node.getX(), node.getY() -1);
			return resultnode ;
		}
		if(result == resultbot){
			resultnode = new Node(node.getX(), node.getY() + 1);
			return resultnode ;
		}
		if(result == resultleft){
			resultnode = new Node(node.getX() - 1, node.getY());
			return resultnode ;
		}
			
		if(result ==resultright){
			resultnode = new Node(node.getX() + 1, node.getY());
			return resultnode;	
		}
		return null;
	}
	
	
	public double findMinPositive(double d1, double d2, double d3, double d4){
		Double[] array = new Double[4];
		array[0] = d1;
		array[1] = d2;
		array[2] = d3;
		array[3] = d4;
		Double result = 0.0;
		int index = 0;
		for(int i = 0 ; i < array.length; i++){
			for(int j = i ; j <array.length; j++){
				if(array[j] < array[i]){
					result = array[j];
					array[j] = array[i];
					array[i] = result;
				}
			}
		}
		for(int i = 0; i< array.length; i++){
			if(array[i] > 0)
				return array[i];
		}
		
		return array[3];
	}
	
	public ArrayList<Node> deleteRepeat(ArrayList<Node> list){
		ArrayList<Node> result = new ArrayList<Node>();
		HashSet<Node> set = new HashSet<Node>();
		for(int i = 0 ; i < list.size(); i++){
			if(set.add(list.get(i))){
				result.add(list.get(i));
			}
		}
		return result;
	}
	
	public void computeRidge(){
		double center;
		double top;
		double bot;
		double left;
		double right;
		int count = 0;
		
		for(int i = 1; i < this.xnumber - 1; i++){
			for(int j =1 ;j <this.ynumber - 1; j++){
				count = 0;
				center = this.densitymap[i][j];
				top = this.densitymap[i][j - 1];
				bot =  this.densitymap[i][j + 1];
				left =  this.densitymap[i - 1][j];
				right =  this.densitymap[i + 1][j];
				if(top < center)
					count++;
				if(bot < center)
					count++;
				if(left < center)
					count++;
				if(right < center)
					count++;
				if(count == 3){
					this.ridge.add(new Node(i,j));
				}
				
			}
		}
	}
	
	public ArrayList<Node> getAllBiger(Node node){
		ArrayList<Node> result = new ArrayList<Node>();
		double center = this.densitymap[node.getX()][node.getY()];
		double top = this.densitymap[node.getX()][node.getY() - 1];
		double bot = this.densitymap[node.getX()][node.getY() + 1];
		double left = this.densitymap[node.getX() - 1][node.getY()];
		double right = this.densitymap[node.getX() + 1][node.getY()];
		if(top > center)
			result.add(new Node(node.getX(), node.getY() - 1));
		if(bot > center)
			result.add(new Node(node.getX(), node.getY() + 1));
		if(left > center)
			result.add(new Node(node.getX() - 1, node.getY()));
		if(right > center)
			result.add(new Node(node.getX() + 1, node.getY()));		
		return result;
	}
	
	public Node largestPoint(int x, int y){
		if(x < 1 || y < 1)
			return null;
		Node node;
		//densities of all points around the input point
		double center = this.densitymap[x][y];
		double top = this.densitymap[x][y-1];
		double bot = this.densitymap[x][y+1];
		double left = this.densitymap[x-1][y];
		double right = this.densitymap[x+1][y];

		if(top > center && top >= bot  && top >= left && top >= right){
			node = new Node(x, y-1);
			return node;
		}
		if(bot > center && bot >= top  && bot >= left && bot >= right){

			node = new Node(x, y+1);
			return node;
		}
		if(left > center && left >= bot  && left >= top && left >= right){
			node = new Node(x-1, y);
			return node;
		}
		if(right > center && right >= bot  && right >= left && right >= top){
			node = new Node(x+1, y);
			return node;
		}
		
		return null;		
	}
	
	public double largest(double x1, double x2, double x3, double x4, double x5){
		double result = x1;
		if(x2 > result){
			result = x2;
		}
		if(x3 > result){
			result = x3;
		}
		if(x4 > result){
			result = x4;
		}
		if(x5 > result){
			result = x5;
		}
		
		return result;
		
	}
	
	// compute all connected path based on max and ridge.
	public void findConnected(){
		ArrayList<Node> combinedlist = new ArrayList<Node>();
		Node center,left, right, top, bot, topleft, topright, botleft, botright;
		ArrayList<Node> templist = new ArrayList<Node>();
		int index = 0;
		HashSet<Node> set = new HashSet<Node>();
		//combine max and ridge lists.
		combinedlist.addAll(this.maxlist);
		combinedlist.addAll(this.ridge);
//		System.out.println(this.maxlist.size()+"     " + this.ridge.size()+"     "+combinedlist.size());
		while(combinedlist.size() != 0){
			templist = new ArrayList<Node>();
			templist.add(combinedlist.get(0));
			combinedlist.remove(0);
			index = 0;
			while(index != templist.size()){
				center = templist.get(index);
				top = new Node(center.getX(), center.getY() - 1);
				bot = new Node(center.getX(), center.getY() + 1);
				left = new Node(center.getX() - 1, center.getY());
				right = new Node(center.getX() + 1, center.getY());
				topleft =  new Node(center.getX() - 1, center.getY() - 1); 
				topright =  new Node(center.getX() + 1, center.getY() - 1);
				botleft =  new Node(center.getX() - 1, center.getY() + 1);
				botright =  new Node(center.getX() + 1, center.getY() + 1);
				if(this.checkpoint(top, combinedlist)){
					templist.add(top);
					this.minusNode(combinedlist, top);
				}
				if(this.checkpoint(bot, combinedlist)){
					templist.add(bot);
					this.minusNode(combinedlist, bot);
				}
				if(this.checkpoint(left, combinedlist)){
					templist.add(left);
					this.minusNode(combinedlist, left);
				}
				if(this.checkpoint(right, combinedlist)){
					templist.add(right);
					this.minusNode(combinedlist, right);
				}
				if(this.checkpoint(topleft, combinedlist)){
					templist.add(topleft);
					this.minusNode(combinedlist, topleft);
				}
				if(this.checkpoint(topright, combinedlist)){
					templist.add(topright);
					this.minusNode(combinedlist, topright);
				}
				if(this.checkpoint(botleft, combinedlist)){
					templist.add(botleft);
					this.minusNode(combinedlist, botleft);
				}
				if(this.checkpoint(botright, combinedlist)){
					templist.add(botright);
					this.minusNode(combinedlist, botright);
				}
				index ++;
			}
			this.allpathlist.add(templist);
		}
		System.out.println(this.allpathlist.size());
	}
	
	//check whether a node is in the list;
	public boolean checkpoint(Node node, ArrayList<Node> list){
		HashSet<Node> set = new HashSet<Node>(); 
		set.addAll(list);
		if(set.add(node))
			return false; // doesn't contain
		return true; // contain
	}
	
	//remove a node from the list.
	public void minusNode(ArrayList<Node> list, Node node){
		for(int i = 0 ; i < list.size() ; i++){
			if(list.get(i).getX() == node.getX() && list.get(i).getY() == node.getY()){
				list.remove(i);
				return;
			}
		}		
	}
	
	//filter the paths which don't satisfy the length requirement.
	public void computeShowBasedThree(){
		int length = 30;  // the minimal value for the length of the path.
		
		//filtering the paths.
		for(int i = 0 ; i < this.allpathlist.size() ; i++){
			if(allpathlist.get(i).size() > length){
				this.showpathlist.add(allpathlist.get(i));
			}
		}
		
		//connect three closed paths
		int min = 500 ,mid = 500 ,max = 500;
		int distance = 0;
		ArrayList<Node> minpath;
		ArrayList<Node> midpath;
		ArrayList<Node> maxpath;
		ArrayList<Node> temp = new ArrayList<Node>();
		
		for(int i = 0; i < showpathlist.size(); i++){
			System.out.println("based three     " + i);
			min = 500;
			mid = 500;
			max = 500;
			minpath = new ArrayList<Node>();
			midpath = new ArrayList<Node>();
			maxpath = new ArrayList<Node>();
			for(int j = 0; j < showpathlist.size() ; j++){
				if(i == j){
					continue;
				}
				
				temp = this.twoClosedNodesofTwoPath(showpathlist.get(i), showpathlist.get(j));
				distance = this.calculateDistance(temp.get(0), temp.get(1));
//				System.out.println(distance);
				if(distance < min){
					maxpath = new ArrayList<Node>();
					maxpath.addAll(midpath);
					midpath = new ArrayList<Node>();
					midpath.addAll(minpath);
					minpath = new ArrayList<Node>();
					minpath.add(temp.get(0));
					minpath.add(temp.get(1));
					max = mid;
					mid = min;
					min = distance;
				}
				else{
					if(distance < mid){
						maxpath = new ArrayList<Node>();
						maxpath.addAll(midpath);
						midpath = new ArrayList<Node>();
						midpath.add(temp.get(0));
						midpath.add(temp.get(1));
						max = mid;
						mid = distance;
						
					}
					else{
						if(distance < max){
							maxpath = new ArrayList<Node>();
							maxpath.add(temp.get(0));
							maxpath.add(temp.get(1));
							max = distance;
						}
					}
				}
			}
			this.allconnectedlines.add(minpath);
			this.allconnectedlines.add(midpath);
			this.allconnectedlines.add(maxpath);
		}
	}
	
	//filter the length of path.
	//connect two closed paths and finally form a connected component.
	public void computeShow(){
		int length = 30;  // the minimal value for the length of the path.
		
		//filtering the paths.
		for(int i = 0 ; i < this.allpathlist.size() ; i++){
			if(allpathlist.get(i).size() > length){
				this.showpathlist.add(allpathlist.get(i));
			}
		}
		
		System.out.println("computeShow");
		//connect the rest of the path.
		
		ArrayList<Node> list ;
		ArrayList<Node> finallist = new ArrayList<Node>();
		int distance = 0;
		int mindistance = 500;
		int pathnumberone = 0;
		int pathnumbertwo = 0;
		while(this.showpathlist.size() > 1){
			mindistance = 500;
			for(int i = 0; i < this.showpathlist.size() ; i++){
				for(int j = i+1 ; j < this.showpathlist.size(); j++){
					list = this.twoClosedNodesofTwoPath(showpathlist.get(i), showpathlist.get(j));
//					System.out.println(list.get(0).getX()+"    "+list.get(0).getY()+"    "+list.get(1).getX()+"    "+list.get(1).getY()+"    sssss");
					distance = this.calculateDistance(list.get(0), list.get(1));
					if(distance < mindistance){
						mindistance = distance;
						finallist = new ArrayList<Node>();
						finallist.add(list.get(0));
						finallist.add(list.get(1));
						pathnumberone = i;
						pathnumbertwo = j;
					}
					
				}
			}
			
			// combine
			showpathlist.get(pathnumberone).addAll(showpathlist.get(pathnumbertwo));
			showpathlist.remove(pathnumbertwo);
			this.allconnectedlines.add(finallist);
			System.out.println(showpathlist.size()+"   showpathlist ");
		}
		
	}
	
	public ArrayList<Node> twoClosedNodesofTwoPath(ArrayList<Node> first, ArrayList<Node> second){
		int min = 500;
		ArrayList<Node> list = new ArrayList<Node>();
		int nodenumberone = 0;
		int nodenumbertwo = 0;
		int length = 0;
		for(int i = 0 ; i < first.size() ; i++){
			for(int j = 0; j < second.size() ; j++){
				length = this.calculateDistance(first.get(i), second.get(j));
				if(length < min){
					min = length;
					nodenumberone = i;
					nodenumbertwo = j;
				}
			}
		}
		list.add(first.get(nodenumberone));
		list.add(second.get(nodenumbertwo));
		return list;
		
	}
	
	public ArrayList<ArrayList<Node>> GetAllpathlist(){
		return this.allpathlist;
	}
	
	public double[][] getDensityMap(){
		return this.densitymap;
	}
	
	
	public ArrayList<Node> getMaxlist() {
		return this.maxlist ;
	}
	
	public ArrayList<Node> getSaddlelist() {
		return this.saddlelist ;
	}
	
	public ArrayList<Node> getRidge(){
		return this.ridge;
	}

	public ArrayList<ArrayList<Node>> getAllconnectedlines() {
		return allconnectedlines;
	}
	

	public ArrayList<ArrayList<Node>> getShowpathlist() {
		return showpathlist;
	}
	
	public void writeMethod() throws IOException{
		ArrayList<ArrayList<Node>> list = this.showpathlist;
		String filename = "output.txt";
		FileWriter writer = new FileWriter(filename);
		for(int i = 0 ; i< list.size(); i++){
			for(int j = 0 ; j < list.get(i).size(); j++){
				writer.write(list.get(i).get(j).getX() +" " + list.get(i).get(j).getY()+ "\n");
			}
			writer.write("\n");
		}
		writer.close();		
		
	}

	public static void main(String args[]){
		Reconstruction re = new Reconstruction();
		re.findBoundary();
		re.formDensityMap();
		re.locateSaddlePoint();
		re.locateMaxPoint();


		re.computeRidge();
		re.findConnected();
		
		re.computeShowBasedThree();
		try {
			re.writeMethod();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*		
		ArrayList<ArrayList<Node>> list = re.getAllconnectedlines();
		System.out.println("size     " + list.size());
		for(int i = 0 ; i < list.size(); i++){
			for(int j = 0 ; j < list.get(i).size(); j++){
				System.out.println(list.get(i).get(j)+"      ");
			}
			System.out.println();
		}
*/
/*		System.out.println();
		for(int i = 0; i< list.size();i++){
			for(int j = 0; j< list.get(i).size();j++){
				System.out.println(list.get(i).get(j).getX()+"    " +list.get(i).get(j).getY());
			}
			System.out.println();
		}
*/
	
	}

}
