import java.util.List;
/**
Reference
https://en.wikipedia.org/wiki/Minimax#Pseudocode
http://stackoverflow.com/questions/21519112/minimax-algorithm-explanation
http://www.aihorizon.com/essays/basiccs/trees/minimax.htm
Still struggling with the basic idea behind the algorithm...
*/

/**
 * An instance represents a Solver that intelligently determines
 * Moves using the Minimax algorithm.
 */
public class AI implements Solver {

    private Player player; // the current player

    /**
     * The depth of the search in the game space when evaluating moves.
     */
    private int depth;
    
    /**
     * The depth of the search in the game space when evaluating moves.
     */
    public int getDepth(){return this.depth;}

    /**
     * Constructor: an instance with player p who searches to depth d
     * when searching the game space for moves.
     */
    public AI(Player p, int d) {
        player = p;
        depth = d;
    }

    /**
     * See Solver.getMoves for the specification.
     */
    @Override
    public Move[] getMoves(Board b) {
          	
    	///I won't test this. I know it works
    	
    	// Set up current state
    	/// is needed prior to setting up any gametree or minimax algorithm
    	State stateRightNow = new State(this.player, b, null);    	
    	
    	// minimax before tree creation doesn't work
    	///this.minimax(stateRightNow);
    	
    	// Construct the game tree
    	AI.createGameTree(stateRightNow, this.getDepth());
    	
    	///minimax
    	this.minimax(stateRightNow);    	
    	
    	///need to replace this magic number...placeholder for now
    	///create an array
    	Move[] getPriorityMoves = new Move[100];  	
    	    	
    	int counter=0;
    	for(int i = 0; i < stateRightNow.getChildren().length; i++)
    	{
    		///testing...it just doesn't seem to be working right...
    		//System.out.printf("State value: %d;\n",stateRightNow.getValue()); 
    		//System.out.printf("Child state value: %d;\n", stateRightNow.getChildren()[i].getValue());
    		//System.out.println("--------");    		
    		//stateRightNow.writeToFile();
    		
    		///if the child has state has the same value as the current state, then 
    		///we add that state to the moves array
    		///it should be the priority move
    		if (stateRightNow.getValue() == stateRightNow.getChildren()[i].getValue())
    		{    			
    			getPriorityMoves[counter] = stateRightNow.getChildren()[i].getLastMove();
    			counter++;
    		}
    	}
    	
    	///where we shorten the array
    	Move[] bestMoves = new Move[counter];

    	for(int i = 0; i < counter; i++)
    	{
    		bestMoves[i] = getPriorityMoves[i];
    	}
    	
        return bestMoves;
        
    }

    /**
     * Generate the game tree with root s of depth d.
     * The game tree's nodes are State objects that represent the state of a game
     * and whose children are all possible States that can result from the next move.
     * <p/>
     * NOTE: this method runs in exponential time with respect to d.
     * With d around 5 or 6, it is extremely slow and will start to take a very
     * long time to run.
     * <p/>
     * Note: If s has a winner (four in a row), it should be a leaf.
     */
    public static void createGameTree(State s, int d) {
        // Note: This method must be recursive, recurse on d,
        // which should get smaller with each recursive call
    	
    	///cease operations at depth zero
    	///any lower and everything is messed up
    	 if (d > 0) {
    		 
    		 s.initializeChildren();
    		 ///this recursion was wrong initially...
    		 //we need trees for all the states within the game
    		 ///this can be done via a for loop    		 
    		 for (int i =0; i <s.getChildren().length; i++)
    		 {
    			 ///slow...how to optimise
    			 createGameTree(s.getChildren()[i], d - 1);
    		 }
    	 }
    	 
    }

    /**
     * Call minimax in ai with state s.
     */
    public void minimax(AI ai, State s)
    {
        ai.minimax(s);
    }

    /**
     * State s is a node of a game tree (i.e. the current State of the game).
     * Use the Minimax algorithm to assign a numerical value to each State of the
     * tree rooted at s, indicating how desirable that java.State is to this player.
     */
    public void minimax(State s)
    {
    	///wikipedia...
    	///...i mean, I think this should work as pseudocode was converted
    	///a value should be associated with each state    	
    	int max = -Integer.MAX_VALUE , min = Integer.MAX_VALUE, depthBreakPoint = 0;
    	
    	//Call this method if we have no children
    	//initially the depth was not part of this either or condition - 
    	//evaluation should take place once a depth has been reached 
    	if (s.getChildren() == State.length0||depth == depthBreakPoint)
    	{
    		///should be returning a winner...checking for winner does the same thing, I I think
    		s.setValue(this.evaluateBoard(s.getBoard()));
    	}
    	///try and obtain the maximum value
    	///this is for first player
    	else if (s.getPlayer() == player) {
    		
    		for (int i =0; i <s.getChildren().length; i++) 
    		{
    			///iterate through array and run the algorithm recursively again and again for
    			//each state
    			///how do you reduce depth??? the number of turns???
    			this.minimax(s.getChildren()[i]);
    			
    			if (s.getChildren()[i].getValue() > max)
    			{
    				max = s.getChildren()[i].getValue();
    				//depth = depth -1;
    			}
    		}
    		
    		s.setValue(max);

    		///testing
    		//System.out.printf("AI opts for cell with value: %d;\n",max);
    	}
    	///try and obtain the minimum value;
    	///this is for the other player
    	else 
    	{
    		///same as above, we just run it for minimum value
    		for (int i =0; i <s.getChildren().length; i++) 
    		{
    			this.minimax(s.getChildren()[i]);
    			
    			if (s.getChildren()[i].getValue() < min)
    			{
    				min = s.getChildren()[i].getValue();
    				//depth = depth -1;
    			}
    		}
    		
    		s.setValue(min);
    	}
    }

    /**
     * Evaluate the desirability of Board b for this player
     * Precondition: b is a leaf node of the game tree (because that is most
     * effective when looking several moves into the future).
     */
    public int evaluateBoard(Board b) {
        Player winner = b.hasConnectFour();
        int value = 0;
        if (winner == null) {
            // Store in sum the value of board b. 
            List<Player[]> locs = b.winLocations();
            for (Player[] loc : locs) {
                for (Player p : loc) {
                    value += (p == player ? 1 : p != null ? -1 : 0);
                }
            }
        } else {
            // There is a winner
            int numEmpty = 0;
            for (int r = 0; r < Board.NUM_ROWS; r = r + 1) {
                for (int c = 0; c < Board.NUM_COLS; c = c + 1) {
                    if (b.getTile(r, c) == null) numEmpty += 1;
                }
            }
            value = (winner == player ? 1 : -1) * 10000 * numEmpty;
        }
        return value;
    }
}
