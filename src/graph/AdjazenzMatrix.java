package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AdjazenzMatrix extends Matrix{
	
	public AdjazenzMatrix(File inMatrix) throws FileNotFoundException, GraphException 
	{
		//super(inMatrix);
		
		Scanner myReader = new Scanner(inMatrix);
		
		if(myReader.hasNextLine()) {
			int vertexCount = myReader.nextLine().split(";").length;
			
			if(vertexCount > 0) {
				setMatrix(new int[vertexCount][vertexCount]);
			}
		}
		
		int from = 0;
		while(myReader.hasNext()) {
			String[] input = myReader.next().split(";");
			
			if(input.length != matrix.length) {
				myReader.close();
    		    throw new GraphException("Adjazenzmatrix nicht korrekt!");
			}	
			
			int to = 0;
			for(String element : input) {
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
	
	public int getPathsForLength(int from, int to, int length) throws GraphException
	{
		vertexInRange(from);
		vertexInRange(to);
		
		int[][] result = new int[matrix.length][matrix.length];
				
		for (int i = 0; i < length; i++) {
			result = multiply(result);
		}
		return result[from][to];
	}
}