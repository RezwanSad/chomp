import javax.swing.JOptionPane;
/*
 *  Another simple GUI demonstration using swing.
 *  Here the GridSquare class extends JPanel and
 *  holds its coordinates in the grid as attributes.
 *  @author mhatcher
 */
public class Chomp
{
	public static void main(String[] args)
	{
		String[] sizes = {"10x12", "7x10", "5x8"};

		int answer = JOptionPane.showOptionDialog(
				null, 
				"Please select a chocolate bar size for the game", 
				"The Game of Chomp", 
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, 
				null, sizes, 0);

		if (answer == 0){
			WindowDemo demo = new WindowDemo(10, 12);
		} else if (answer == 1) {
			WindowDemo demo = new WindowDemo(7, 10);
		} else if (answer == 2) {
			WindowDemo demo = new WindowDemo(5, 8);
		} else  {
			System.exit(0);
		}
		// create a new GUI window
		
	}
}
