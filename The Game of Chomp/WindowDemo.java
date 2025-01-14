import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

/*
 *  The main window of the gui.
 *  Notice that it extends JFrame - so we can add our own components.
 *  Notice that it implements ActionListener - so we can handle user input.
 *  This version also implements MouseListener to show equivalent functionality (compare with the other demo).
 *  @author mhatcher
 */
public class WindowDemo extends JFrame implements ActionListener, MouseListener
{
	// randomly determines whose turn it is
	Random random = new Random();

	// gui components that are contained in this frame:
	private JPanel topPanel, bottomPanel;	// top and bottom panels in the main window
	private JLabel instructionLabel;		// a text label to tell the user what to do
	private JLabel infoLabel;            // a text label to show the coordinate of the selected square
    private JButton topButton;				// a 'reset' button to appear in the top panel
	private GridSquare [][] gridSquares;	// squares to appear in grid formation in the bottom panel
	private int rows,columns;				// the size of the grid

	private String name;					// player name
	boolean player1_turn;					// checks which players turn it is
	
	/*
	 *  constructor method takes as input how many rows and columns of gridsquares to create
	 *  it then creates the panels, their subcomponents and puts them all together in the main frame
	 *  it makes sure that action listeners are added to selectable items
	 *  it makes sure that the gui will be visible
	 */
	public WindowDemo(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		this.setSize(500,500);
		
		// first create the panels
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(rows, columns, 1, 1));
		bottomPanel.setSize(500,500);
		
		// then create the components for each panel and add them to it
		
		// for the top panel:
		instructionLabel = new JLabel("Click the Squares!");
        infoLabel = new JLabel("No square clicked yet.");
		topButton = new JButton("New Game");
		topButton.addActionListener(this);			// IMPORTANT! Without this, clicking the square does nothing.
		
		topPanel.add(instructionLabel);
		topPanel.add (topButton);
        topPanel.add(infoLabel);
		
	
		// for the bottom panel:	
		// create the squares and add them to the grid
		gridSquares = new GridSquare[rows][columns];
		for ( int x = 0; x < rows; x ++)
		{
			for ( int y = 0; y < columns; y ++)
			{
				gridSquares[x][y] = new GridSquare(x, y);
				gridSquares[x][y].setSize(20, 20);
				gridSquares[x][y].setColor(x + y);
				
				gridSquares[x][y].addMouseListener(this);		// AGAIN, don't forget this line!
				
				bottomPanel.add(gridSquares[x][y]);
			}
		}
		
		// now add the top and bottom panels to the main frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(bottomPanel, BorderLayout.CENTER);		// needs to be center or will draw too small
		
		// housekeeping : behaviour
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		name = JOptionPane.showInputDialog("Enter player name: ");
		// determining which player goes first
		firstTurn();
	}
	
	public void firstTurn() {
		if (random.nextInt(2) == 0) {
			player1_turn = true;
			instructionLabel.setText(name + "'s turn");
			instructionLabel.setForeground(Color.BLUE);
		} else {
			player1_turn = false;
			instructionLabel.setText("CPU turn");
			instructionLabel.setForeground(Color.RED);
			Timer timer = new Timer(1500, e -> cpuTurn()); // Adding a slight delay for the CPU's turn
			timer.setRepeats(false);
			timer.start();
		}
	}

	/*
	 *  handles actions performed in the gui
	 *  this method must be present to correctly implement the ActionListener interface
	 */
	public void actionPerformed(ActionEvent aevt)
	{
		// get the object that was selected in the gui
		Object selected = aevt.getSource();
				
		// if resetting the squares' colours is requested then do so
		if ( selected.equals(topButton) )
		{
			instructionLabel.setText("Click the Squares!");
			instructionLabel.setForeground(Color.BLACK);
			infoLabel.setText("No square clicked yet.");
			infoLabel.setForeground(Color.BLACK);

			for ( int x = 0; x < rows; x ++)
			{
				for ( int y = 0; y < columns; y ++)
				{
					gridSquares [x][y].setColor(x + y);
				}
			}

			Timer timer = new Timer(1500, e -> firstTurn()); // Adding a slight delay for the CPU's turn
			timer.setRepeats(false);
			timer.start();
		}
	}


	// Mouse Listener events
	public void mouseClicked(MouseEvent mevt)
	{
		if (!player1_turn) return;

		// get the object that was selected in the gui
		Object selected = mevt.getSource();
		
		/*
		 * I'm using instanceof here so that I can easily cover the selection of any of the gridsquares
		 * with just one piece of code.
		 * In a real system you'll probably have one piece of action code per selectable item.
		 * Later in the course we'll see that the Command Holder pattern is a much smarter way to handle actions.
		 */

		// if a gridsquare is selected then switch its color
		// check if a gray square is clicked and ignore the move
		if (selected instanceof GridSquare && ((GridSquare) selected).getBackground().equals(Color.gray)) {
			infoLabel.setText("You can't click on previously chomped square!");
			return;
		}

		if (selected instanceof GridSquare && ((GridSquare) selected).getBackground().equals(new Color(139, 69, 19))) {
			GridSquare square = (GridSquare) selected;
			player1Turn(square);
		} else if (selected instanceof GridSquare && ((GridSquare) selected).getBackground().equals(Color.green)) {
			GridSquare square = (GridSquare) selected;
			player1Turn(square);
			infoLabel.setText("Yuk! " + name + " ate the soap.");
			infoLabel.setForeground(Color.MAGENTA);
			instructionLabel.setText("Game Over!");
			return; // exit without switching turns
		}

		player1_turn = false;
		instructionLabel.setText("CPU turn");
		instructionLabel.setForeground(Color.RED);

		Timer timer = new Timer(1500, e -> cpuTurn()); // Adding a slight delay before CPU turn
		timer.setRepeats(false);
		timer.start();

	}


	private void player1Turn(GridSquare square) {
		int selectedX = square.getXcoord();
		int selectedY = square.getYcoord();
		infoLabel.setText("(" + selectedX + "," + selectedY + ") last selected.");

		square.setBackground(Color.red);

		Timer timer = new Timer(500, e -> square.setBackground(Color.gray)); // Adding a slight delay before CPU turn
		timer.setRepeats(false);
		timer.start();

		for (int x = selectedX; x < rows; x++) {
			for (int y = selectedY; y < columns; y++) {
				GridSquare gridSquare = gridSquares[x][y];
				gridSquare.switchColor();
			}
		}
	}


	private void cpuTurn() {

		// array to hold all the squares valid for choosing
		ArrayList<GridSquare> validSquares = new ArrayList<>();

		// collect all brown or green squares
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				Color squareColor = gridSquares[x][y].getBackground();
				if (squareColor.equals(new Color(139, 69, 19)) || squareColor.equals(Color.green)) {
					validSquares.add(gridSquares[x][y]);
				}
			}
		}

		if (!validSquares.isEmpty()) {
			// randomly select a valid square
			GridSquare selectedSquare = validSquares.get(random.nextInt(validSquares.size()));

			// If the CPU selects a green square, end the game
			if (selectedSquare.getBackground().equals(Color.green)) {
				infoLabel.setText("Yuk! CPU ate the soap. Game Over!");
				infoLabel.setForeground(Color.MAGENTA);
				instructionLabel.setText("Game Over!");
				player1_turn = false;
				return;  // exit without switching turns
			}

			player1Turn(selectedSquare);

			player1_turn = true;
			instructionLabel.setText(name + "'s turn");
			instructionLabel.setForeground(Color.BLUE);
		}
	}
	
	// not used but must be present to fulfil MouseListener contract
	public void mouseEntered(MouseEvent arg0){}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}
