import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Rfile {

	double[][] array;
	double threadhold = 0;

	
	//303602
	public Rfile() throws FileNotFoundException {
		File inputFile = new File("pts.txt");
		Scanner scone = new Scanner(inputFile);
		inputFile = new File("clr.txt");
		Scanner sctwo = new Scanner(inputFile);
		array = new double[303602][3];
		for(int i = 0; i<303602;i++){
			array[i][0] = scone.nextDouble();
			array[i][1] = scone.nextDouble();
			array[i][2] = sctwo.nextDouble();
		}
		
		scone.close();
		sctwo.close();
	}
	
	public void writeMethod() throws IOException{
		String filename = "ptsclr.txt";
		FileWriter writer = new FileWriter(filename);
		for(int i = 0 ; i < 303602; i++){
			if(array[i][2] >= threadhold){
				writer.write(array[i][0]+" ");
				writer.write(array[i][1]+" ");
				writer.write(array[i][2]+"\n");
			}
		}
		writer.close();
		
		
	}
	
	public static void main(String args[]){
		Rfile r;
		try {
			 r = new Rfile();
			 r.writeMethod();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
