package tools;

import java.util.LinkedList;
import java.util.List;

import model.Coord;
import model.Couleur;
import model.Pieces;

/**
 * @author francoise.perrin
 * Inspiration Jacques SARAYDARYAN, Adrien GUENARD
 * 
 * Classe qui fabrique une pièce de jeu d'echec
 * de la couleur, du type et aux coordonnées
 * passées en paramètre
 *
 */
public class ChessSinglePieceFactory {

	/**
	 * private pour ne pas instancier d'objets
	 */
	private ChessSinglePieceFactory() {

	}

	/**
	 * @param pieceCouleur
	 * @return liste de pi�ces de jeu d'�chec
	 */
	public static Pieces newPiece(Couleur pieceCouleur, String type, int x, int y){

		Pieces piece = null;
		String initCouleur = (Couleur.BLANC == pieceCouleur ? "B_" : "N_" );

		String className = "model." + type;	// attention au chemin
		String pieceName = initCouleur + type.substring(0, 2);
		Coord pieceCoord = new Coord(x, y);

		piece = (Pieces) Introspection.newInstance (className,
				new Object[] {pieceName, pieceCouleur, pieceCoord});

		return piece;
	}

	/**
	 * Tests unitaires
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(ChessSinglePieceFactory.newPiece(Couleur.BLANC, "Tour", 0, 6));
	}
}
