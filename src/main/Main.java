package main;

//import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

//import javax.swing.JFrame;

import graph.Graph;
import graph.GraphException;


class Main {

	public static void main(String[] args) {
		File input;
		
		if(args.length > 0) {
			input = new File(args[0]);
			System.out.println(System.getProperty("user.dir"));
			System.out.println(args[0]);
		}
		else {
			input = new File("../resources/48.txt");
		}
		
		if(input.exists()) {
				
			try {
				Graph g = new Graph(input);
				
				//Adjazenzmatrix
				System.out.println(g.getAdjazenzMatrix().toString());
				System.out.println();
				//Wegematrix
				System.out.println(g.getWegeMatrix().toString());
				System.out.println();
				//Distanzmatrix
				System.out.println(g.getDistanzMatrix().toString());
				System.out.println();
				//Zusammenhängend
				System.out.println("Ist Zusammenhaengend: " + g.getZusammenhaengend());
				System.out.println();
				//Komponenten
				System.out.println("Komponenten: ");
				g.printKomponenten();
				System.out.println();
				//Artikulationen
				System.out.println("Artikulationen: ");
				for (Integer a : g.getArticulations()) {
					System.out.print((a) + " ");
				}
				System.out.println();
				
				//Exzentrizitaeten
				int[] ex = g.getExzentrizitaeten();
				System.out.println("Exzentrizitaeten: ");
				for (int i = 0; i < ex.length; i++) {
					System.out.print(i + ": " + ex[i]);
					if (i < ex.length-1) {
						System.out.print(", ");
					}
				}
				System.out.println();
				System.out.println();
				
				//Radius, Durchmesser, Zentrum -- nur wenn zusammenhängend
				System.out.print("Radius: " + g.getRadius() + ", Durchmesser: " + g.getDiameter() + ", Zentrum: ");
				for(int element : g.getCenter()) {
					System.out.print(element + " ");
				}
				System.out.println();
				
				System.out.println("Bruecken: ");
				for (Integer[] element : g.getBridges()) {
					System.out.println(element[0]+ "-" +element[1]);
				}
				System.out.println();
				
				System.out.println("Bloecke: ");
				for (HashSet<Integer> element : g.getBlocks()) {
					System.out.print("{ ");
					for (Integer e : element) {
						System.out.print(e + " ");
					}
					System.out.println("}");
				}
				System.out.println();
			} catch (FileNotFoundException | GraphException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else {
			System.out.print("No such file!");
		}
	}	
}
