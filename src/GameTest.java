import java.util.Random;
import org.junit.Test;

public class GameTest {
	
	//Solver p1 = new AI(Player.RED, 6);
    //Solver p2 = new AI(Player.YELLOW, 6);

	private Solver p1 = new Human(Player.RED);
	private Solver p2 = new Dummy(Player.YELLOW);
           
    private Board board = new Board();
    
    Random rand = new Random();
    
    /**
     * The idea behind the minimax algorithm is that there is a two player game: 
     * One player is attempting to maximize the score and another player is trying to minimize it.
     */
    @Test
	public void testMinimax()
	{
    	int depth =1;
    	
    	///add moves
    	board.makeMove(new Move(Player.YELLOW, 1));
    	board.makeMove(new Move(Player.YELLOW,2));
    	board.makeMove(new Move(Player.YELLOW, 4));	
    	board.makeMove(new Move(Player.RED, 5));
    	board.makeMove(new Move(Player.RED, 3));
    	board.makeMove(new Move(Player.YELLOW, 6));	
    	board.makeMove(new Move(Player.RED, 0));
    	board.makeMove(new Move(Player.YELLOW, 4));	
    	//board.makeMove(new Move(Player.RED, 5));
    	//board.makeMove(new Move(Player.YELLOW, 6));	
   		//board.makeMove(new Move(Player.RED, 0));
    			
   	    ////new state
   	    State s =  new State(Player.YELLOW, board, new Move(Player.RED, 6));
    	    
   	   //create AI instance3
        AI i = new AI(Player.RED, depth);
   	    
        ///create a game tree
        AI.createGameTree(s,depth);
    		    
        ///run the minimax algorithm
        i.minimax(s);
    		    
        ///write to file
   	    s.writeToFile();
    		    
   	    ///the output should be the lowest value; the state with the lowest value should be the best move. 
   	    System.out.println(s.getValue());    		    
    	
	}
	
    /**
     * Return an array of possible moves
     */
	@Test
	public void testGetPossibleMoves() {		
		
		board.makeMove(new Move(Player.RED, 1));
		board.makeMove(new Move(Player.RED, 1));
		board.makeMove(new Move(Player.RED, 1));
	    
	    Game game= new Game(p1, p2, board, false);
	    
	    State s =  new State(Player.RED, board, new Move(Player.RED, 1));
		
	    System.out.println("Player can move piece to the following columns:");
	    for(int i = 0; i < board.getPossibleMoves(Player.RED).length;i++)
        {
        	System.out.println(board.getPossibleMoves(Player.RED)[i].getColumn());
        }
	    
	    GameTest.fillColumn(board, 2, Player.YELLOW);
		
		System.out.println("\nPlayer can move piece to the following columns:");
	    for(int i = 0; i < board.getPossibleMoves(Player.RED).length;i++)
        {
        	System.out.println(board.getPossibleMoves(Player.RED)[i].getColumn());
        }
	    
	    s.initializeChildren();
	    
	    for (State ss : s.getChildren())
	    {
	    	System.out.println("LOOOK"+ss);
	    }
	    
	    runGame(game);
	}
	
	/**
     * A binary tree was created, but I'm not sure whether it can be implemented into this minimax situation...
     */
	@Test
	public void testBinaryTree()
	{		
		int[] arr = new int[4];
		
		int root = 25;
		
		System.out.format("Binary tree root: %d\n", root);
		BinarySearchTree t = new BinarySearchTree(root);
		
		for(int i = 0 ; i< arr.length; i++)
		{
			arr[i]=rand.nextInt(50);
			System.out.format("Value inserted into binary tree: %d\n", arr[i]);
			t.Insert(arr[i]);
		}
		
		System.out.println("\n--------PREORDER---------");
		t.Display_Preorder();
		
		System.out.print("Does " + arr[3]+ " exist in the binary search tree: ");	
		System.out.println(t.BinarySearch(arr[3]));
		
		System.out.print("Does 15 exist in the binary search tree: ");	
		System.out.println(t.BinarySearch(15));
	}
	
	/**
	 * Tests all the possible moves a player can make across the gameboard. 
	 */
	@Test
	public void testInitializeChildren()
	{
		///add moves
		board.makeMove(new Move(Player.RED, 1));
		board.makeMove(new Move(Player.YELLOW, 6));    
	    
	    ////new state
	    State s =  new State(Player.RED, board, new Move(Player.RED, 1));
	    
	    ///run the initialise children method
	    s.initializeChildren();
	    
	    ///now test to see what happens...hopefully it works
	    State[] children = s.getChildren();
	    
	    for(int i =0; i < children.length;i++)
	    {
	    	System.out.println(children[i]);
	    }
	    
	    System.out.println("Number of states: " + children.length);
	}
	
	/**
	 * Creates a game tree
	 */
	@Test
	public void testGameTree()
	{
		///add moves
		board.makeMove(new Move(Player.YELLOW, 1));
		board.makeMove(new Move(Player.YELLOW,2));
		board.makeMove(new Move(Player.YELLOW, 4));	
		board.makeMove(new Move(Player.RED, 5));
		board.makeMove(new Move(Player.YELLOW, 6));	
		board.makeMove(new Move(Player.RED, 0));
		board.makeMove(new Move(Player.YELLOW, 4));	
		board.makeMove(new Move(Player.RED, 5));
		board.makeMove(new Move(Player.YELLOW, 6));	
		board.makeMove(new Move(Player.RED, 0));
		
	    ////new state
	    State s =  new State(Player.YELLOW, board, null);
	    
	    ///run the initialise children method
	    s.initializeChildren();
	    
	    AI ai = new AI(Player.RED, 1);	    
	    ai.createGameTree(s, 1);	        
	    
	    AI.createGameTree(s,6);
	    
	    s.writeToFile();
	    
	}

	private void runGame(Game game) {
		game.setGUI(new GUI(game));
	    game.runGame();
	}
	
	/**
    *Fills a column with alternating coloured pieces
    */
	public static void fillColumn(Board board, int column, Player player)
	{
		for(int i =0; i < board.NUM_COLS; i++)
		{
			player = (player==Player.RED) ? Player.YELLOW: Player.RED;
			
			board.makeMove(new Move(player, column));
		}
	}

}
