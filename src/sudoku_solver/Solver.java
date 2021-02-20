package sudoku_solver;

import java.util.ArrayList;
import java.util.List;

public class Solver {
	
	// Recursive function applies backtracking graph colouring
	static boolean graphColouring(int squareIndex, int possibleColours,
			ArrayList<Integer> givenColours, Graph<Integer> graph) {
		
		if (squareIndex == graph.getSize()) {
			return true;
		}
		for (int i = 1; i <= possibleColours; i++) {
			if (isSafeColour(squareIndex, i, givenColours, graph)) {
				graph.setSquareVal(squareIndex, i);
				if (graphColouring(squareIndex+1, possibleColours, givenColours, graph)) {
					return true;
				}
			}
			
			if (!givenColours.contains(squareIndex)) {
				graph.setSquareVal(squareIndex, 0);
			}
		}
		
		return false;
	}
	
	// Function checks whether a given colour can be used on a specified vertex
	static boolean isSafeColour(int squareIndex, int colourCode,
			ArrayList<Integer> givenColours, Graph<Integer> graph) {
		if (givenColours.contains(squareIndex) && graph.getSquareVal(squareIndex) == colourCode)
			return true;
		
		if (givenColours.contains(squareIndex))
			return false;
		
		List<Integer> neighbours = graph.getNeighbours(squareIndex);
		for (int neighbour : neighbours) {
			if (graph.getSquareVal(neighbour) == colourCode) {
				return false;
			}
		}
		
		return true;
	}
	
}
