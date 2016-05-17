import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;




public class Tran {
	int[][] matrix;
	int size;
	public Tran() throws FileNotFoundException {
		File inputFile = new File("YeastL.txt");
		Scanner sc = new Scanner(inputFile);
		int count = 0;
		int number = 0;
		while(sc.hasNextLine()){
			sc.nextLine();
			count ++ ;
		}
		System.out.println(count); // how many lines in .txt file.
		
		sc = new Scanner(inputFile);
		
//		this.formMatrix(outarray);
		//form outarray based on map and oriarray
		
	}
	
	
	public void formMatrix(int[][] array){
		int x = 0;
		int y = 0;
		this.matrix = new int[this.size][this.size];
		for(int i = 0; i<array.length ; i++){
			x = array[i][0];
			y = array[i][1];
			matrix[x][y] = 1;
			matrix[y][x] = 1;
		}
	}
	public boolean checkSymmetric(){
		boolean value = true;
		for(int i = 0; i < this.size; i++){
			for(int j = i ; j< this.size ;j ++){
				if(this.matrix[i][j] != this.matrix[j][i])
					value =false;
			}
		}
		return value;
	}
	
	public void writerMethod() throws IOException {
		String filename = "CAmatrix.txt";
		FileWriter writer = new FileWriter(filename);
		int size = matrix.length;
		for(int i = 0 ; i<this.size ; i++){
			for(int j = 0; j < size -1; j++){
				writer.write(matrix[i][j]+" ");
			}
			writer.write(matrix[i][size -1]+"\n");
		}
		writer.close();
	}
	
	public static void main(String args[]){
		try {
			Tran t = new Tran();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
