package model;



/**
 * @author francoise.perrin
 * Inspiration Jacques SARAYDARYAN, Adrien GUENARD
 * G�re le comportement commun � toutes les pi�ces
 * Chaque classe d�riv�e (Pion, etc.) sera capable de dire 
 * si le d�placement est OK.
 */
public abstract class AbstractPiece implements Pieces {

	private int x, y;
	private Couleur couleur;
	private String name; // Surtout utile pour affichage en mode console



	/**
	 * @param name
	 * @param couleur
	 * @param coord
	 */
	public AbstractPiece(String name, Couleur couleur, Coord coord){
		this.name = name;
		this.x = coord.x;
		this.y = coord.y;
		this.couleur=couleur;
	}

	/* (non-Javadoc)
	 * @see model.Pieces#getX()
	 */
	public int getX(){
		return this.x;
	}

	/* (non-Javadoc)
	 * @see model.Pieces#getY()
	 */
	public int getY(){
		return this.y;
	}

	/* (non-Javadoc)
	 * @see model.Pieces#getCouleur()
	 */
	public Couleur getCouleur(){
		return this.couleur;
	}

	/* (non-Javadoc)
	 * @see model.Pieces#move(int, int)
	 * 
	 * D�place une pi�ce
	 */
	public boolean move(int x, int y){
		boolean ret = false;
		if(Coord.coordonnees_valides(x,y)){
			this.x=x;
			this.y=y;
			ret = true;
		}
		return ret;

	}
	
	/* (non-Javadoc)
	 * @see model.Pieces#capture()
	 * 
	 * Capture une piece : 
	 * passer ses coordonn�es � -1
	 */
	public boolean capture(){
		this.x=-1;
		this.y=-1;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String S=getName();
		return S;
	}


	/* (non-Javadoc)
	 * @see model.Pieces#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see model.Pieces#isMoveOk(int, int)
	 * 
	 * En fonction du type de pi�ce (Pion, etc.)
	 * est capable de dire si le d�placement est OK
	 */
	public abstract boolean isMoveOk(int xFinal, int yFinal) ;

};

