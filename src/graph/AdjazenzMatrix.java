package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AdjazenzMatrix extends Matrix{
	
	public AdjazenzMatrix(File inMatrix) throws FileNotFoundException, GraphException 
	{
		//super(inMatrix);
		
		Scanner myReader = new Scanner(inMatrix);
		String[] input = null;
		
		if(myReader.hasNextLine()) {
			
			input = myReader.nextLine().split(";");
			int vertexCount = input.length;
			
			if(vertexCount > 0) {
				setMatrix(new int[vertexCount][vertexCount]);
			}
		}
		
		int from = 0;
		if(input != null) {
			while(myReader.hasNext()) {
				
				if(input.length != matrix.length) {
					myReader.close();
	    		    throw new GraphException("Adjazenzmatrix nicht korrekt!");
				}	
				int to = 0;
				for(String element : input) {
					//System.out.println("Element: " + element);
					if(to >= from && to < input.length && element != null && element.equals("1")) {
						addEdge(from, to, 1);
					}
					to++;
				}
				from++;
				input = myReader.nextLine().split(";");
			}
			
			int to = 0;
			for(String element : input) {
				//System.out.println("Element: " + element);
				if(to >= from && to < input.length && element != null && element.equals("1")) {
					addEdge(from, to, 1);
				}
				to++;
			}
			from++;
		}
		
		myReader.close();
		setMinValue(1);
	}
	
	public int[] getVerticeWeigths() 
	{
		int[] weights = new int[matrix.length];
		for (int from = 0; from < matrix.length; from++) {
			for (int adj : matrix[from]) {
				weights[from] += adj;
			}
		}
		return weights;
	}
	
//	public int getPathsForLength(int from, int to, int length) throws GraphException
//	{
//		vertexInRange(from);
//		vertexInRange(to);
//		
//		int[][] result = new int[matrix.length][matrix.length];
//				
//		for (int i = 0; i < length; i++) {
//			result = multiply(result);
//		}
//		return result[from][to];
//	}
}