package graph;

import java.util.ArrayList;

public class WegeMatrix extends Matrix{
	
	boolean zusammenhaengend;
	ArrayList<ArrayList<Integer>> komponenten;
	
	
	private WegeMatrix(int[][] matrix)
	{
		this.matrix = matrix;
		setMinValue(1);
	}
	
	public static WegeMatrix instanzWegeMatrix(AdjazenzMatrix adjMatrix) throws GraphException
	{
		int [][] newMatrix = new int[adjMatrix.getMatrix().length][adjMatrix.getMatrix()[0].length];
		
		WegeMatrix wegeMatrix = new WegeMatrix(newMatrix);
		Utilities.copy(adjMatrix.getMatrix(), newMatrix);
		
		for (int from = 0; from < wegeMatrix.getMatrix().length; from++) {
			wegeMatrix.addEdge(from, from, 1);
		}
		return wegeMatrix;
	}
	
	public static WegeMatrix instanzWegeMatrix(int[][] wegeMatrix) {
		WegeMatrix wege = new WegeMatrix(wegeMatrix);
		
		return wege;
	}
	
	
	public void addPaths(int[][] newPaths) throws GraphException
	{
		compareMatrices(newPaths);
		
		for (int i = 0; i < matrix.length; i++) {
			if(matrix[i].length != newPaths[i].length) {
				return;
			}
		}
		
		for (int from = 0; from < matrix.length; from++) {
			for (int to = 0; to < matrix[0].length; to++) {
				if(matrix[from][to] == 0 && newPaths[from][to] != 0) {
					addEdge(from, to, 1);
				}
			}
		}
	}
	
	
	private void setZusammenhaengend()
	{
		for (int[] wege : matrix) {
			for (int weg : wege) {
				if (weg != 1) {
					zusammenhaengend = false;
					return;
				}
			}
		}
		zusammenhaengend = true;
	}

	private void setKomponenten()
	{
		komponenten = new ArrayList<ArrayList<Integer>>();
		
		for (int vertex = 0; vertex < matrix.length; vertex++) {
			ArrayList<Integer> komponente = new ArrayList<Integer>();
			for (int toVertex = 0; toVertex < matrix[0].length; toVertex++) {
				if(matrix[vertex][toVertex] == 1) {
					komponente.add(toVertex);
				}
			}
			
			boolean old = false;
			
			if(komponente.isEmpty()) {
				komponente.add(vertex);
			}
			
			for(ArrayList<Integer> komp : komponenten) {
				if(komp.equals(komponente)) {
					old = true;
				}
			}
			
			if(!old) {
				komponenten.add(komponente);
			}
		}
	}
	
	public void setZusammenhaengendAndKomponenten()
	{
		setZusammenhaengend();
		setKomponenten();
	}
	
	public ArrayList<ArrayList<Integer>> getKomponenten()
	{
		return komponenten;
	}
	
	public boolean getZusammenhaengend()
	{
		return zusammenhaengend;
	}
}
