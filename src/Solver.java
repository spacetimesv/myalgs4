
public class Solver {

	private int moves = 0;
	
	private MinPQ<Board> pq = new MinPQ<Board>();
	private MinPQ<Board> solution = new MinPQ<Board>();
	private Board previous = null;
	
	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial)  {
		
		// is initial goal
		if ( ! initial.isGoal() ) {
			pq.insert( initial );
		}
		
		// add all neighboring boards of search node
		// add all of the neighboring boards to priority exception the previous board
		// enqueue lowest priority board from priority queue
		// Repeat until we reach the goal board
		run8Puzzle();
		
	}
	
	private void run8Puzzle() {
	
		// continue running until all boards are processed
		while( ! pq.isEmpty() ) {
			// enqueue min priority board
			Board searchB = pq.delMin();
			
			// update previous
			previous = searchB;
			
			// add board to solution
			solution.insert( searchB );
			
			if( searchB.isGoal() ){
				break;
			}
			
			// get all neighbors of search board
			// insert them to priority queue if it is not equal to previous board
			System.out.println("Finding neighbors for board " + searchB );
			for(Board b: searchB.neighbors() ) {
				if(!b.equals(previous)) {
					System.out.print( "Inserting to min priority queue - " + b );
					pq.insert(b);
				}
			}
			
		}
		
	}
	

	// is the initial board solvable?
	public boolean isSolvable()   {
		return (moves() < 0) ? false: true;
	}
	
	// min number of moves to solve initial board; -1 if no solution
    public int moves()  {
    	int N = solution.size();
    	if( N > 0 ) return solution.size();
    	return -1;
    }
    
	// sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution()  {
    	return solution;
    }
    
	// solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        
    }

}
