package sudoku_solver;

import java.util.*;
import java.io.*;

public class Main {
	
	private static ArrayList<Integer> givenColoursIndices = new ArrayList<>();
	
	public static void main(String[] args) {
		// Load text file into an array to use as start of sudoku
		List<String> fileInfo = readFile("src/sudoku_solver/example_9by9.txt");
		int sudokuLength = (int) Math.sqrt(fileInfo.size());
		
		ArrayList<Integer> sudokuLabels = createSudokuLabels(fileInfo.size());
		
		// Split sudoku into rows, columns and boxes to create graph connections
		Graph<Integer> myGraph = new Graph<>();
		sendRows(sudokuLabels, sudokuLength, myGraph);
		sendColumns(sudokuLabels, sudokuLength, myGraph);
		sendBoxes(sudokuLabels, sudokuLength, myGraph);
		
		// Input given values into final map
		saveInitialValues(fileInfo, myGraph);
		
		// Print state of board before solving
		System.out.println("Before:");
		Map<Integer, Integer> startSquares = myGraph.getSudokuValues();
		printSudokuState(startSquares, sudokuLength);
		
		// Send info to solver class to fill sudoku
		Solver solveSudoku = new Solver();
		if (solveSudoku.graphColouring(0, sudokuLength, givenColoursIndices, myGraph)) {
			Map<Integer, Integer> finalSquares = myGraph.getSudokuValues();
			System.out.println("After:");
			printSudokuState(finalSquares, sudokuLength);
		} else {
			System.out.println("Error computing solution");
		}
		
	}
	
	// Function to print sudoku board
	static void printSudokuState(Map<Integer, Integer> squareStates, int sudokuLength) {
		int boxCount = 1;
		int squareCount = 1;
		int rowCount = 1;
		int boxLength = (int) Math.sqrt(sudokuLength);
		System.out.println("------------");
		for (int square : squareStates.values()) {
			if (boxCount == boxLength && squareCount == sudokuLength && rowCount != boxLength) {
				System.out.print(square);
				System.out.println("|");
				boxCount = 1;
				squareCount = 1;
				rowCount++;
			} else if (boxCount == boxLength && squareCount == sudokuLength && rowCount == boxLength) {
				System.out.print(square);
				System.out.println("|");
				System.out.println("------------");
				boxCount = 1;
				squareCount = 1;
				rowCount = 1;
			} else if (boxCount == boxLength) {
				boxCount = 1;
				System.out.print(square);
				System.out.print("|");
				squareCount++;
			} else {
				System.out.print(square);
				boxCount++;
				squareCount++;
			}
		}
		
	}
	
	// Function to read sudoku values from text file into list
	static List<String> readFile(String fileName) {
		List<String> sudokuSquares = new ArrayList<>();
		try {
			Scanner s = new Scanner(new File(fileName));
			while(s.hasNextLine()) {
				String test = s.next();
				sudokuSquares.addAll(Arrays.asList(test.split("")));
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		
		return sudokuSquares;
	}
	
	// Create vertex labels to map numbers against 
	static ArrayList<Integer> createSudokuLabels(int sudokuSquares) {
		ArrayList<Integer> labels = new ArrayList<>();
		for (int i = 0; i < sudokuSquares; i++) {
			labels.add(i);
		}
		return labels;
	}
	
	// Split labels into each row, feed each one into graph to be connected
	static void sendRows(ArrayList<Integer> sudokuLabels, int sudokuLength, Graph<Integer> graph) {
		for (int i = 0; i < sudokuLength; i++) {
			int startIndex = i * sudokuLength;
			graph.connectAll(sudokuLabels.subList(startIndex, startIndex + sudokuLength));
		}
	}
	
	// Split labels into each column, feed each one into graph to be connected
	static void sendColumns(ArrayList<Integer> sudokuLabels, int sudokuLength, Graph<Integer> graph) {
		for (int i = 0; i < sudokuLength; i++) {
			List<Integer> tempCol = new ArrayList<>();
			for (int j = 0; j < sudokuLength; j++) {
				tempCol.add(sudokuLabels.get((j * sudokuLength) + i));
			}
			graph.connectAll(tempCol);
		}
	}
	
	// Split labels into each n x n box, feed each one into graph to be connected
	static void sendBoxes(ArrayList<Integer> sudokuLabels, int sudokuLength, Graph<Integer> graph) {
		int boxLength = (int) Math.sqrt(sudokuLength);
		for (int i = 0; i < sudokuLength; i += boxLength) {
			for (int j = 0; j < sudokuLength; j += boxLength) {
				List<Integer> boxSquares = new ArrayList<>();
				for (int k = 0; k < sudokuLength; k += boxLength) {
					List<Integer> tempBoxRows = sudokuLabels.subList(
							(k*boxLength) + (j + (i*sudokuLength)),
							(k*boxLength) + (j + (i*sudokuLength)) + boxLength
					);
					boxSquares.addAll(tempBoxRows);
				}
				graph.connectAll(boxSquares);
			}
		}
	}
	
	// Enter initial clues into vertex colour map
	static void saveInitialValues(List<String> initialValues, Graph<Integer> graph) {
		for (int i = 0; i < initialValues.size(); i++) {
			if (initialValues.get(i).equals("-")) {
				graph.assignVertexColour(i, 0);
			} else {
				graph.assignVertexColour(i, Integer.parseInt(initialValues.get(i)));
				givenColoursIndices.add(i);
			}
		}
	}
}
