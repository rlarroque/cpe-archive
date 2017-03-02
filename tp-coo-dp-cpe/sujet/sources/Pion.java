package model;




/**
 * @author francoise.perrin
  * Inspiration Jacques SARAYDARYAN, Adrien GUENARD*
 */
public class Pion extends AbstractPiece implements Pions {
	private boolean premierCoup;

	/**
	 * @param name
	 * @param couleur_de_piece
	 * @param coord
	 */
	public Pion(String name,Couleur couleur_de_piece, Coord coord) {
		super(name,couleur_de_piece, coord);
		this.premierCoup = true;
	}

	/* (non-Javadoc)
	 * @see model.AbstractPiece#isMoveOk(int, int)
	 */
	@Override
	public boolean isMoveOk(int xFinal, int yFinal) {

		boolean ret = false;

		if ((xFinal == this.getX())
				&& (Math.abs(yFinal - this.getY()) <= 1 || 
				(Math.abs(yFinal - this.getY()) <= 2 && this.premierCoup==true))) {

			if ((Couleur.NOIR.equals(this.getCouleur()) && (yFinal - this.getY() > 0))
					|| (Couleur.BLANC.equals(this.getCouleur()) 
							&& (yFinal - this.getY() < 0))) {
				ret = true;
			}
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see model.Pions#isMoveDiagOk(int, int)
	 */
	@Override
	public boolean isMoveDiagOk(int xFinal, int yFinal) {
		
		boolean ret = false;
		
		if (Couleur.NOIR.equals(this.getCouleur())) {
			if ((yFinal == this.getY()+1 && xFinal == this.getX()+1) 
					|| (yFinal == this.getY()+1 && xFinal == this.getX()-1)) {
				ret = true;
			}
		}
					
		if (Couleur.BLANC.equals(this.getCouleur())) {
			if ((yFinal == this.getY()-1 && xFinal == this.getX()+1) 
					|| (yFinal == this.getY()-1 && xFinal == this.getX()-1)) {
				ret = true;
			}
		}	
			
		return ret;
	}

	/* (non-Javadoc)
	 * @see model.AbstractPiece#move(int, int)
	 */
	@Override
	public boolean move(int x, int y){
		
		boolean ret = false;
		
		if(Coord.coordonnees_valides(x,y)){
			this.premierCoup = false;
			ret = super.move(x, y);
		}
		return ret;
	}

	

}
