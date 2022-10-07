package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

public class Graph {
	//private ArrayList<Vertex> vertices;
	//private ArrayList<Edge> edges;
	
	private AdjazenzMatrix adjazenz;
	private WegeMatrix wege;
	private DistanzMatrix distanz;
	
	private HashSet<Integer> articulations;
	private HashSet<Integer[]> bridges;
	private HashSet<HashSet<Integer>> blocks;
	
	public Graph(File inMatrix) throws FileNotFoundException, GraphException 
	{
		//vertices = new ArrayList<Vertex>();
		//edges = new ArrayList<Edge>();
		
		adjazenz = new AdjazenzMatrix(inMatrix);
		distanz = DistanzMatrix.instanzDistanzMatrix(adjazenz);
		wege = WegeMatrix.instanzWegeMatrix(adjazenz);
		fillMatrix(); // fill distanz and wege Matrix
		
		articulations = new HashSet<Integer>();
		bridges = new HashSet<Integer[]>();
		blocks = new HashSet<HashSet<Integer>>();
		calculateABB(); // Articulations-Bridges-Blocks
	}
	
	private void fillMatrix() throws GraphException 
	{
		int[][] resultMatrix = new int[adjazenz.getMatrix().length][adjazenz.getMatrix().length];
		Utilities.copy(adjazenz.getMatrix(), resultMatrix);
		
		for (int times = 2; times < adjazenz.getMatrix().length; times++) {
			if (wege.isFull()) {
				break;
			}
			
			if (resultMatrix.equals(adjazenz.multiply(resultMatrix))) {
				break;
			}
			
			resultMatrix = adjazenz.multiply(resultMatrix);
			
			wege.addPaths(resultMatrix);
			distanz.addDistances(resultMatrix, times);
		}
		wege.setZusammenhaengendAndKomponenten();
	}
	
	public void printKomponenten() 
	{
		for (ArrayList<Integer> komponente : getKomponenten()) {
			System.out.print("{ ");
			for (int vertex : komponente) {
				System.out.print(vertex + " ");
			}
			System.out.println("}");
		}
	}

	// distanzMatrix, wegeMatrix
	public int[] getExzentrizitaeten() 
	{
		if (!getZusammenhaengend()) {
			return new int[0];
		}
		
		int[] ex = new int[distanz.getMatrix().length];
		
		for (int from = 0; from < ex.length; from++) {
			for (int to = 0; to < ex.length; to++) {
				if (distanz.getMatrix()[from][to] > ex[from]) {
					ex[from] = distanz.getMatrix()[from][to];
				}
			}
		}
		return ex;
	}
	
	// distanzMatrix, wegeMatrix
	public int getRadius() 
	{
		if (!getZusammenhaengend()) {
			return -1;
		}
		
		int radius = distanz.getMatrix().length;
		int[] ex = getExzentrizitaeten();

		for (int e : ex) {
			if (e < radius) {
				radius = e;
			}
		}
		return radius;
	}
	
	// distanzMatrix, wegeMatrix
	public int getDiameter() 
	{
		if (!getZusammenhaengend()) {
			return -1;
		}
		
		int diameter = 0;
		int[] ex = getExzentrizitaeten();

		for (int e : ex) {
			if (e > diameter) {
				diameter = e;
			}
		}
		return diameter;	
	}

	public ArrayList<Integer> getCenter() 
	{
		if (!getZusammenhaengend()) {
			return new ArrayList<Integer>(0);
		}
		
		ArrayList<Integer> center = new ArrayList<Integer>();
		int radius = getRadius();
		int[] ex = getExzentrizitaeten();
		
		for (int i = 0; i < ex.length; i++) {
			if (ex[i] == radius) {
				center.add(i);
			}
		}
		return center;
	}

	// Articulations-Bridges-Blocks
	private void calculateABB()
	{
		int[] low = new int[adjazenz.getMatrix().length];
		int[] depth = new int[adjazenz.getMatrix().length];
		int[] parent = new int[adjazenz.getMatrix().length];
		Stack<Integer[]> stack = new Stack<Integer[]>();
		
		for (int i = 0; i < adjazenz.getMatrix().length; i++) {
			parent[i] = -1;;
			depth[i] = -1;
			low[i] = -1;
		}
		
		for (int i = 0; i < adjazenz.getMatrix().length; i++) {
			if (depth[i] == -1) {
				dfsABB(i, parent, depth, low, stack, 0);
			}
			
			if (!stack.isEmpty()) {
				//HashSet<Integer[]> nexHash = new HashSet<Integer[]>();
				HashSet<Integer> nextHash = new HashSet<Integer>();
				
				while (!stack.isEmpty()) {
					//Integer[] next = stack.pop();
    				//nexHash.add(next);
					nextHash.addAll(Arrays.asList(stack.pop()));
				}
				blocks.add(nextHash);
			}
		}
	}
	
	//Articulations, Bridges and Blocks
	private void dfsABB(int vertex, int[] parent, int[] depth, int[] low, Stack<Integer[]> stack, int time)
	{
		//discover vertex
		depth[vertex] = low[vertex] = ++time;
		//System.out.println("Vertex:" + (vertex) +" -> Tiefe: " + depth[vertex] +" LOW: " + low[vertex]);
		int children = 0;
		
		 for (int i = 0; i < adjazenz.getMatrix().length; i++) {
			 if (adjazenz.getMatrix()[vertex][i] == 1) {
				 int nextVertex = i;
			 
				 //Recur if not visited
				 if (depth[nextVertex] == -1) {
					 children++;
					 parent[nextVertex] = vertex;
					 
					 //new edge in stack
					 stack.add(new Integer[] {vertex, nextVertex});
					 //edges.add(new Edge(vertex, nextVertex));
					 
					 //Recurse
					 dfsABB(i, parent, depth, low, stack, time);
					 
					 low[vertex] = Math.min(low[nextVertex], low[vertex]);
					 
					 //bridge
					 if (low[nextVertex] > depth[vertex]) {
						 //System.out.println("Bridge: "+ vertex +"-" + nextVertex);
						 bridges.add(new Integer[] {vertex,nextVertex});
					 }
					 
					 //articulation
					 if ((depth[vertex] == 1 && children > 1) || (depth[vertex] > 1 && low[nextVertex] >= depth[vertex])) {
						 articulations.add(vertex);
						 //System.out.println("Artikulation: " + (vertex));
						 //HashSet<Integer[]> next = new HashSet<Integer[]>();
						 HashSet<Integer> next = new HashSet<Integer>();
						 
						 while(!stack.isEmpty() && (!Utilities.contains(stack.peek(), nextVertex) || !Utilities.contains(stack.peek(), vertex))) {
							 //next.add(stack.pop());
							 next.addAll(Arrays.asList(stack.pop()));
						 }
						 if (!stack.isEmpty()) {
							 //next.add(stack.pop());
							 next.addAll(Arrays.asList(stack.pop()));
						 }
						 blocks.add(next);
					 }
					 
				 }
				 else if (nextVertex != parent[vertex] && depth[nextVertex] < depth[vertex]) {
					 low[vertex] = Math.min(low[vertex], depth[nextVertex]);
					 //System.out.println("ELSE: Knoten:" + (vertex) +" -> Tiefe: " + depth[vertex] + " LOW: " + low[vertex] + " NaechsterKnoten:" + (nextVertex));
					 stack.push(new Integer[] {vertex,nextVertex});
					 //edges.add(new Edge(vertex, nextVertex));
				 }				 
			 }
		 }
		 //Islandvertex
		 if (parent[vertex] == -1 && children == 0) {
			 stack.push(new Integer[] {vertex,vertex});
		 }
		 //vertices.add(new Vertex(Integer.toString(vertex), (int)((vertex*1000)%547 + 30), (int)((1987*(vertex+1)))%547 + 30));
	}
	
	/*
	public ArrayList<Vertex> getVertices()
	{
		return vertices;
	}
	
	public ArrayList<Edge> getEdges()
	{
		return edges;
	}
	*/
	
	public boolean getZusammenhaengend() 
	{
		return wege.getZusammenhaengend();
	}

	public ArrayList<ArrayList<Integer>> getKomponenten() 
	{
		return wege.getKomponenten();
	}
	
	public HashSet<HashSet<Integer>> getBlocks()
	{
		return blocks;
	}
	
	public HashSet<Integer[]> getBridges()
	{
		return bridges;
	}
	
	public HashSet<Integer> getArticulations() 
	{
		return articulations;
	}
	
	public AdjazenzMatrix getAdjazenzMatrix() 
	{
		return adjazenz;
	}
	
	public WegeMatrix getWegeMatrix() 
	{
		return wege;
	}
	
	public DistanzMatrix getDistanzMatrix() 
	{
		return distanz;
	}
}
