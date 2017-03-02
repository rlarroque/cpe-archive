package model;



/**
 * @author francoise.perrin
 * Inspiration Jacques SARAYDARYAN, Adrien GUENARD *
 */
public class Cavalier extends AbstractPiece  {
	

	/**
	 * @param name
	 * @param couleur_de_piece
	 * @param coord
	 */
	public Cavalier(String name,Couleur couleur_de_piece, Coord coord) {
		super(name,couleur_de_piece, coord);
	}

	/* (non-Javadoc)
	 * @see model.AbstractPiece#isMoveOk(int, int)
	 */
	@Override
	public boolean isMoveOk(int xFinal, int yFinal) {
		
		boolean ret = false;
		
		if ((Math.abs(xFinal - this.getX()) + Math.abs(yFinal - this.getY())) == 3) {
			
			if ((Math.abs(xFinal - this.getX())<3) && (Math.abs(yFinal - this.getY())<3)) {
				ret  = true;
			}		
		}	
		
		return ret;
	}

}
