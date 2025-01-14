import java.awt.Color;
import javax.swing.*;

/*
 *  A GUI component
 *
 *  A simple extension of JPanel which records its
 *  coordinates in xcoord and ycoord, NOT in 'x' and 'y'.
 *  Why not? Because 'x' and 'y' are already attributes of
 *  the panel (super) class which say where to draw it in the window.
 *
 *  The game grid and allows the background colour to be set with ease.
 *
 *  @author mhatcher
 */
public class GridSquare extends JPanel
{
    private int xcoord, ycoord;  // location in the grid
	
	// constructor takes the x and y coordinates of this square
	public GridSquare(int xcoord, int ycoord)
	{
		super();
		this.setSize(50,50);
		this.xcoord = xcoord;
		this.ycoord = ycoord;
	}
	
	// if the decider is even, it chooses black, otherwise white (for 'column+row' will allow a chequerboard effect)
    public void setColor( int decider)
    {
        Color colour = (int) (decider) == (0) ? Color.green : new Color(139, 69, 19);
        this.setBackground( colour);
    }
    
    // if the square is brown it becomes gray
    public void switchColor()
    {
        if (this.getBackground().equals(new Color(139, 69, 19))) {
            this.setBackground(Color.gray);
        }
    }
    
    // simple setters and getters
    public void setXcoord(int value)    { xcoord = value; }
    public void setYcoord(int value)    { ycoord = value; }
    public int getXcoord()              { return xcoord; }
    public int getYcoord()              { return ycoord; }
}
