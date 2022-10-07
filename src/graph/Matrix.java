package graph;

//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Scanner;


public abstract class Matrix 
{
	protected int[][] matrix;
	protected int minValue;
	
	public Matrix() {}
	
//	public Matrix(File inMatrix) throws FileNotFoundException 
//	{
//			Scanner myReader = new Scanner(inMatrix);
//			
//			if(myReader.hasNextLine()) {
//				int vertexCount = myReader.nextLine().split(";").length;
//				
//				if(vertexCount > 0) {
//					setMatrix(new int[vertexCount][vertexCount]);
//				}
//				
//			}
//			myReader.close();
//	}
	
//	protected Matrix(int[][] inMatrix)
//	{
//		this.matrix = inMatrix;
//	}

	// Graph.java fillMatrix
	public boolean isFull()
	{
		for(int from = 0; from < matrix.length; from++) {
			for(int to = 0; to < matrix[0].length; to++) {
				if(matrix[from][to] < minValue) {
					return false;
				}
			}
		}
		return true;
	}
	
	protected int[][] multiply (int[][] multiplicant) throws GraphException
	{
		//Error
		if(matrix[0].length != multiplicant.length) {
			throw new GraphException("Error! Kann die Matrizen nicht multiplizieren!");
		}
		
		int[][] result = new int[matrix.length][multiplicant.length];
		
		
		for(int from = 0; from < matrix.length; from++) {
			for(int to = 0; to < multiplicant.length; to++) {
				result[from][to] = 0;
				for(int count = 0; count < matrix.length; count++) {
					result[from][to] += matrix[from][count] * multiplicant[count][to];
				}
			}
		}
		return result;
	}
	
	protected void vertexInRange(int vertice) throws GraphException {
		if (vertice < 0 || vertice >= matrix.length) {
			throw new GraphException("Error! Vertex out of bounds!");
		}
	}
	
	public void addEdge(int from, int to, int value) throws GraphException 
	{
		
		vertexInRange(from);
		vertexInRange(to);
			
		setEdge(from, to, value);

	}
	
	private void setEdge(int from, int to, int value) 
	{
		matrix[from][to] = value;
		matrix[to][from] = value;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder s = new StringBuilder((matrix.length * matrix[0].length));
	 		      
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[0].length; j++) {
				s.append(matrix[i][j]);
				if(j < matrix[0].length-1) {
						s.append("  ");
				}
			}
			s.append(System.getProperty("line.separator"));
		}
		return s.toString();
	}
	
	// Distanz und Wege
    protected void compareMatrices(int[][] matrix) throws GraphException {
    	if ((this.matrix.length != matrix.length) || (this.matrix[0].length != matrix[0].length)) {
			throw new GraphException("Error! -- Matrizen nicht gleich gross!");
		}
    }
	
	public int[][] getMatrix() 
	{
		return matrix;
	}
	public void setMatrix(int[][] matrix) 
	{
		this.matrix = matrix;
	}
	
	protected void setMinValue(int value)
	{
		minValue = value;
	}
	
	protected int getMinValue()
	{
		return minValue;
	}
}
