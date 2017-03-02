package model;

/**
 * @author francoise.perrin
 * Inspiration Jacques SARAYDARYAN, Adrien GUENARD *
 */
public interface Pieces {

	/**
	 * @return indice de la colonne o� est positionn�e la piece
	 */
	public int getX();
	
	/**
	 * @return indice de la ligne o� est positionn�e la piece
	 */
	public int getY();
	
	/**
	 * @return couleur de la piece
	 */
	public Couleur getCouleur();
	
	/**
	 * @return lenom de la piece
	 */
	public String getName() ;
	
	/**
	 * @param xFinal
	 * @param yFinal
	 * @return true si d�placement l�gal
	 */
	public  boolean isMoveOk(int xFinal, int yFinal) ;
	
	/**
	 * @param xFinal
	 * @param yFinal
	 * @return true si d�placement effectu�
	 */
	public boolean move(int xFinal, int yFinal);
	
	/** 
	 * @return true si piece effectivement captur�e
	 * Positionne x et y à -1
	 */
	public boolean capture();
};

