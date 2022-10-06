package graph;

import java.util.Arrays;

public class Utilities {
	
	public static void copy(int[][] srcMatrix, int[][] destMatrix) 
	{		
		for(int from = 0; from < srcMatrix.length; from++) {
			for(int to = 0; to < srcMatrix[0].length; to++) {
				destMatrix[from][to] = srcMatrix[from][to];
			}
		}
	}

	
	public static boolean contains(Integer[] arr, final Object komponente) 
	{
		if (arr == null) {
			return false;
		}
		return Arrays.stream(arr).anyMatch(i -> komponente.equals(i));
	}
	
}
