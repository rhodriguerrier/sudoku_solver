package sudoku_solver;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graph<T> {
	private Map<T, T> squareVals = new LinkedHashMap<>();
	private Map<T, List<T>> sudokuConnections = new HashMap<>();
	
	// Adds a new vertex to graph
	void addVertex(T label) {
		sudokuConnections.put(label, new LinkedList<T>());
	}
	
	// Adds a bidirectional edge between two vertices
	void addEdge(T source, T destination) {
		if (source == destination)
			return;
		
		if (!sudokuConnections.containsKey(source))
			addVertex(source);
		
		if (!sudokuConnections.containsKey(destination))
			addVertex(destination);
		
		if (!sudokuConnections.get(source).contains(destination))
			sudokuConnections.get(source).add(destination);
		
		if (!sudokuConnections.get(destination).contains(source))
			sudokuConnections.get(destination).add(source);
	}
	
	// Assign a value to a given vertex
	void assignVertexColour(T vertexName, T vertexColour) {
		squareVals.put(vertexName, vertexColour);
	}
	
	// Connect everything in the input list to one another
	void connectAll(List<T> squares) {
		for (int i = 0; i < squares.size(); i++) {
			for (int j = i+1; j < squares.size(); j++) {
				addEdge(squares.get(i), squares.get(j));
			}
		}
	}
	
	// Get the current value of each vertex
	Map<T, T> getSudokuValues() {
		return squareVals;
	}
	
	// Get the vertices that are connected to a given vertex
	List<T> getNeighbours(T square) {
		return sudokuConnections.get(square);
	}
	
	// Get the value of a given vertex
	T getSquareVal(T square) {
		return squareVals.get(square);
	}
	
	// Get the number of vertices in the graph
	int getSize() {
		return squareVals.size();
	}
	
	// Setter to change the value of a given vertex
	void setSquareVal(T square, T value) {
		squareVals.put(square, value);
	}
	
}
