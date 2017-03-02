package vue;

import controler.controlerLocal.ChessGameControler;
import model.Coord;

import java.util.Observable;
import java.util.Observer;


/**
 * @author francoise.perrin
 * Inspiration Jacques SARAYDARYAN, Adrien GUENARD *
 * 
 */
public class ChessGameCmdLine implements Observer{
	
	public ChessGameCmdLine(ChessGameControler chessGameControler) {
		
		System.out.println(chessGameControler + "\n");
		
		chessGameControler.move(new Coord(3,6), new Coord(3, 4));	// true
		System.out.print("\n Déplacement de 3,6 vers 3,4 : ");
		System.out.println(chessGameControler.getMessage() + "\n");	
		System.out.println(chessGameControler + "\n");
		
		chessGameControler.move(new Coord(3,4), new Coord(3, 6));	// false
		System.out.print("\n Déplacement de 3,4 vers 3,6 : ");
		System.out.println(chessGameControler.getMessage() + "\n");	
		System.out.println(chessGameControler + "\n");
		
		chessGameControler.move(new Coord(4, 1), new Coord(4, 3));	// true
		System.out.print("\n Déplacement de 4,1 vers 4,3 : ");
		System.out.println(chessGameControler.getMessage() + "\n");	
		System.out.println(chessGameControler + "\n");
		
		chessGameControler.move(new Coord(3, 4), new Coord(3, 4));	// false
		System.out.print("\n Déplacement de 3,4 vers 3,4 : ");
		System.out.println(chessGameControler.getMessage() + "\n");	
		System.out.println(chessGameControler + "\n");
		
		chessGameControler.move(new Coord(3, 4), new Coord(4, 3));	// true
		System.out.print("\n Déplacement de 3,4 vers 4,3 : ");
		System.out.println(chessGameControler.getMessage() + "\n");	
		System.out.println(chessGameControler + "\n");
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("CmdLine Updated successfully");
	}
}
