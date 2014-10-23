

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board implements Comparable {

	private int[][] board;
	private int mValue = -1;
	
	// goal board
	int[][] goal = {
					{1, 2, 3},
					{4, 5, 6},
					{7, 8, 0}
					};
	
	// data to represent a point
	private class Point {
		int x;
		int y;
		Point(int i, int j){
			this.x = i;
			this.y = j;
		}
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append("(" + x + ", " + y + ")");
			return sb.toString();
		}
	}
	
	// construct a board from an N-by-N array of blocks
	public Board(int[][] blocks)   {
		this.board = blocks;
		
		// update manhattan distance
		mValue = manhattan();
	}
    
	// (where blocks[i][j] = block in row i, column j) board dimension N
	public int dimension()     {
		return board.length;
	}

	
	// number of blocks out of place
	public int hamming()   {
		return -1;
	}

	// calculate the manhattan distance from point a to point b
	private int manhattanDistance(int x1, int y1, int x2, int y2) {
		int dist = 0;
		
		// x2 - x1
		// y2 - y1
		int x = x2 - x1;
		if(x < 0) x = -1 * x;
		int y = y2 - y1;
		if(y < 0) y = -1 * y;
		
		dist = x + y;
		return dist;
	}
	
	// search for position of a number from a given board
	private Point search(int[][] board, int number) {
		Point point = new Point(-1, -1);
		int N = board.length;
		boolean found = false;
		
		for(int i=0; !found && i<N; i++){
			for(int j=0; j<N; j++){
				if(board[i][j] == number) {
					point = new Point(i, j);
					found = true;
					break;
				}
			}
		}
		
		return point;
	}
	
	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		if( mValue != -1 ) return mValue;

		int m = 0;

		for(int i=0; i<board.length; i++) {
			for(int j=0; j < board[i].length; j++){
				// get position of num from goal board
				Point p = search(goal, board[i][j]);
				m += manhattanDistance(i, j, p.x, p.y);
			}
		}

		// update mValue
		mValue = m;
		return m;
	}
	
	// is this board the goal board?
	public boolean isGoal() {    
		return board.equals(goal);
	}
	
	// a board obtained by exchanging two adjacent blocks in the same row
	public int[][] twin() {
		int[][] twinb = this.board;
		//Random r = new Random();
		//int row = r.nextInt(dimension());
		//int col = r.nextInt(dimension());

		// search for position of 0
		// swap adjacent blocks on a row where there is no 0
		//Point p = search(twinb, 0);
		
		// swap 0,0 and 0,1
		int tmp = twinb[0][0];
		twinb[0][0] = twinb[0][1];
		twinb[0][1] = tmp;
		
		return twinb;
	}
	
	// does this board equal y?
	public boolean equals(Object y) {
		if( y == null ) return false;
		if( !(y instanceof Board) ) return false;

		Board yB = (Board) y;
		int N = yB.dimension();
		int[][] yboard = yB.board;
		
		for(int i=0; i < N; i++) {
			for(int j=0; j < N; j++){
				if(board[i][j] != yboard[i][j]) return false;
			}
		}

		return true;
		
	}
	
	// all neighboring boards
	public Iterable<Board> neighbors() {
		// create all neighbor boards
		List<Board> boards = new ArrayList<Board>();
		
		// get the o's position
		// get all possible next moves by swapping blocks left, right, top and bottom
		Point zero = search(this.board, 0);
		//System.out.println("Zero position: " + zero );
		int N = dimension();
		
		//top
		if( zero.x != 0 ) {
			// int[][] top = new int[N][N];
			//System.arraycopy(this.board,  0,  top,  0,  N); 
			// top = Arrays.copyOf( board,  N );
			//System.out.println("Board: ");
			//System.out.println( this );
			Board topB = this.clone();
			
			swapBlocks(topB.board, zero.x, zero.y, zero.x - 1, zero.y);
			boards.add( topB );
			//System.out.println("Top Neighbor: ");
			//System.out.println( topB );
		}
		
		//bottom
		if( zero.x != N-1 ) {
			// int[][] bottom = new int[N][N];
			// System.arraycopy(this.board,  0,  bottom,  0,  N); 
			// bottom = Arrays.copyOf( board,  N );
			//System.out.println("Board: ");
			//System.out.println( this );
			Board bottomB = this.clone();
			
			swapBlocks(bottomB.board, zero.x, zero.y, zero.x + 1, zero.y);
			boards.add( bottomB );
			//System.out.println("Bottom Neighbor: ");
			//System.out.println( bottomB );
		}
		
		//left
		if( zero.y != 0 ){
			// int[][] left = new int[N][N];
			// System.arraycopy(this.board,  0,  left,  0,  N); 
			// left = Arrays.copyOf( board,  N );
			//System.out.println("Board: ");
			//System.out.println( this );
			Board leftB = this.clone();
			swapBlocks(leftB.board, zero.x, zero.y, zero.x, zero.y-1);
			boards.add( leftB );
			//System.out.println("Left Neighbor: ");
			//System.out.println( leftB );
		}
		
		//right
		if( zero.y != N-1 ){
			// int[][] right = new int[N][N];
			// System.arraycopy(board,  0,  right,  0,  N); 
			// right = Arrays.copyOf( board,  N );
			////System.out.println("Board: ");
			//System.out.println( this );
			Board rightB = this.clone();
			
			swapBlocks(rightB.board, zero.x, zero.y, zero.x, zero.y+1);
			// Board rightB = new Board( right );
			boards.add( rightB );
			//System.out.println("Right Neighbor: ");
			//System.out.println( rightB );
		}
		
		return boards;
	}
	
	// swap point a and point b in a board
	private void swapBlocks(int[][] x, int x1, int y1, int x2, int y2){
		int tmp = x[x1][y1];
		x[x1][y1] = x[x2][y2];
		x[x2][y2] = tmp;
	}
	
	// string representation of the board (in the output format specified below)
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		int N = dimension();
		for(int i=0; i < N; i++){
			for(int j =0; j < N; j++){
				sb.append( board[i][j] + " ");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public int compareTo(Object other){
		int cmp = 0;
		if (other instanceof Board) {
			Board b = (Board) other;
			System.out.println("Comparing this " + this + " manhattan " + manhattan() + " with " + b + " manhattan " + b.manhattan());
			if( this.manhattan() < b.manhattan() ) {
				cmp = -1;
			} else if ( this.manhattan() > b.manhattan() ) {
				cmp = 1;
			} else {
				cmp = 0;
			}
		}
		
		return cmp;
	}
	
	public Board clone(){
		int N = this.dimension();
		int[][] b = new int[N][N];
		for(int i=0; i < N; i++){
			for(int j=0; j < N; j++){
				b[i][j] = this.board[i][j];
			}
		}
		Board x = new Board( b );
		return x;
	}
	
	public static void main(String[] args){
		int[][] p = {
					{0, 1, 3},
					{4, 2, 5},
					{7, 8, 6}
					};
		Board b = new Board( p );
		System.out.println("Input board:");
		System.out.println( b );
		System.out.println("Neighbors of input board:");
		for(Board x: b.neighbors()) {
			System.out.print("Neighbor: ");
			System.out.println( x );
		}
	}
}
