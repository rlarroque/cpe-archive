package model;



/**
 * @author francoise.perrin
 * Inspiration Jacques SARAYDARYAN, Adrien GUENARD *
 */
public class Fou extends AbstractPiece {
	

	/**
	 * @param name
	 * @param couleur_de_piece
	 * @param coord
	 */
	public Fou(String name,Couleur couleur_de_piece, Coord coord) {
		super(name,couleur_de_piece, coord);
	}

	/* (non-Javadoc)
	 * @see model.AbstractPiece#isMoveOk(int, int)
	 */
	@Override
	public boolean isMoveOk(int xFinal, int yFinal) {
		
		boolean ret = false;
		
		if (Math.abs(yFinal - this.getY()) == Math.abs(xFinal - this.getX())) {
			ret  = true;
		}		
		
		return ret;
	}

	
}
