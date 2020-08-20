import processing.core.PApplet;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author grace
 * 
 * Completed August 16, 2020
 *
 */

public class MazeGenerator extends PApplet {
	
	int rows = 10;
	int columns = 10;
	int totalCells = rows * columns;
	MazeCell cells[] = new MazeCell[totalCells]; 
	int currentCell;
	int nextCell;
	LinkedList<Integer> mazeOrder = new LinkedList<Integer>();
	
	public static void main(String[] args) {
		PApplet.main("MazeGenerator");

	}
	
	public void settings() {
		setSize(1000, 1000);
	}
	
	public void setup() {
		background(0, 0, 0);
		frameRate(12);
		
		//Initialize cells
		int i = 0;
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < columns; x++) {
				cells[i] = new MazeCell();
				cells[i].visited = false;
				cells[i].x = x;
				cells[i].y = y;
				cells[i].bottomWall = true;
				cells[i].rightWall = true;
				i++;
			}
		}
		
		//Start at a random cell
		int randomCell = (int)(Math.random() * totalCells);
		currentCell = randomCell;
		cells[currentCell].visited = true;
		mazeOrder.addFirst(currentCell);
	}
	
	public void draw() {
		background(0, 0, 0);
		strokeWeight(2);
		
		drawMaze();
		if(mazeOrder.size() == totalCells) {
			
		} else {
			generate();
		}
	}
	
	public void drawMaze() {
		//Total boundary
		rect(100, 100, 50 * columns, 50 * rows);
		//draw cells
		for(int i = 0; i < cells.length; i++) {
			if(cells[i].visited && i != currentCell) {
				fill(50);
			} else if(i == currentCell) {
				fill(80);
			} else {
				fill(0);
			}
			noStroke();
			rect(100 + (cells[i].x * 50), 100 + (cells[i].y * 50), 50, 50);
			//Draw walls
			if(cells[i].bottomWall) {
				stroke(0, 255, 0);
				line(100 + (cells[i].x * 50), 150 + (cells[i].y * 50), 150 + (cells[i].x * 50), 150 + (cells[i].y * 50));
			}
			if(cells[i].rightWall) {
				stroke(0, 255, 0);
				line(150 + (cells[i].x * 50), 100 + (cells[i].y * 50), 150 + (cells[i].x * 50), 150 + (cells[i].y * 50));
			}
		}
	}
	
	public void generate() {
		nextCell = getUnvisitedNeighbor(currentCell);
		if(nextCell != -1) {
			System.out.println("Cell " + currentCell);
			System.out.println("NextCell " + nextCell);
			removeWall(currentCell, nextCell); 
			currentCell = nextCell;
			mazeOrder.addFirst(currentCell);
			cells[currentCell].visited = true;
		} else if(nextCell == -1) {
			for(int i = 0; i < mazeOrder.size() - 1; i++) {
				int previousCell = mazeOrder.get(i);
				System.out.println("Previous: " + previousCell);
				int possibleNextCell = getUnvisitedNeighbor(previousCell);
				if(possibleNextCell != -1) {
					System.out.println("Possible: " + possibleNextCell);
					//currentCell = possibleNextCell;
					currentCell = previousCell;
					break;
				}
			}
		}
	}
	

	
	public int getUnvisitedNeighbor(int currentCell) {
		//Current cell location
		int x = cells[currentCell].x;
		int y = cells[currentCell].y;
		
		//Possible cells to visit
		int[] possibleNeighbors = new int[4];
		//Cells found
		int found = 0;
		
		//left 
		if(x > 0 && !cells[currentCell - 1].visited) {
			possibleNeighbors[found] = currentCell - 1;
			found++;
			System.out.println("left");
		}
		
		//right
		if(x < columns - 1 && !cells[currentCell + 1].visited) {
			possibleNeighbors[found] = currentCell + 1;
			found++;
			System.out.println("right");
		}
		
		//up
		if(y > 0 && !cells[currentCell - columns].visited) {
			possibleNeighbors[found] = currentCell - columns; 
			found++;
			System.out.println("up");
		}
		
		//down
		if(y < rows - 1 && !cells[currentCell + columns].visited) {
			possibleNeighbors[found] = currentCell + columns; 
			found++;
			System.out.println("down");
		}
		
		if(found == 0) {
			return -1;
		}
		System.out.println("---");
		//choose a random candidate
		int choice = (int)(Math.random() * found);
		return possibleNeighbors[choice];
	}
	
	public void removeWall(int currentCell, int nextCell) {
		//Next cell is to left of current cell, current Cell is not on left edge
		if(cells[currentCell].x - cells[nextCell].x > 0 && cells[currentCell].x > 0) {
			//Remove next cell, right wall
			cells[nextCell].rightWall = false;
			System.out.println("Remove next cell, right wall");
		}
		//Next cell is to right of current wall, current cell is not on right edge
		if(cells[currentCell].x - cells[nextCell].x < 0 && cells[currentCell].x < columns - 1) {
			//Remove current cell, right wall
			cells[currentCell].rightWall = false;
			System.out.println("Remove current cell, right wall");
		}
		//Next cell is above current cell, current cell is not on top row
		if(cells[currentCell].y - cells[nextCell].y > 0 && cells[currentCell].y > 0) {
			//Remove next cell, bottom wall
			cells[nextCell].bottomWall = false;
			System.out.println("Remove next cell, bottom wall");
		}
		//Next cell is below current cell, current cell is not on bottom row
		if(cells[currentCell].y - cells[nextCell].y < 0 && cells[currentCell].y < rows - 1) {
			//Remove current cell, bottom wall
			cells[currentCell].bottomWall = false;
			System.out.println("Remove current cell, bottom wall");
		}
	}
}
