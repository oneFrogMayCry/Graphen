package graph;

public class DistanzMatrix extends Matrix{


	private DistanzMatrix(int[][] matrix)
	{
		this.matrix = matrix;
		setMinValue(0);
	}
	
	public static DistanzMatrix instanzDistanzMatrix(AdjazenzMatrix adjMatrix) throws GraphException
	{
		int[][] newMatrix = new int[adjMatrix.getMatrix().length][adjMatrix.getMatrix()[0].length];
		Utilities.copy(adjMatrix.getMatrix(), newMatrix);
		DistanzMatrix distanzMatrix = new DistanzMatrix(newMatrix);
		
		for (int from = 0; from < newMatrix.length; from++) {
			for (int to = 0; to < newMatrix[0].length; to++) {
				if (newMatrix[from][to] == 0) {
					distanzMatrix.addEdge(from, to, -1);
				}
				if (from == to) {
					distanzMatrix.addEdge(from, to, 0);
				}
			}
		}
		
		return distanzMatrix;
	}
	
	
	public void addDistances(int[][] newDistances, int distance) throws GraphException {
		
		compareMatrices(newDistances);
		
		for (int i = 0; i < matrix.length; i++) {
			if(matrix[i].length != newDistances[i].length) {
				return;
			}
		}
		
		for (int from = 0; from < matrix.length; from++) {
			for (int to = 0; to < matrix[0].length; to++) {
				if(matrix[from][to] == -1 && newDistances[from][to] != 0) {
					addEdge(from, to, distance);
				}
			}
		}
	}
	
	@Override
	public String toString() 
	{
		StringBuilder s = new StringBuilder((matrix.length * matrix[0].length));
	      
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[0].length; j++) {
				s.append(matrix[i][j]);
				if(j < matrix[0].length-1) {
					if(matrix[i][j] < 0) {
						s.append(" ");
					}
					else {
						s.append("  ");
					}	
				}
			}
			s.append(System.getProperty("line.separator"));
		}
		return s.toString();
	}
	
}
