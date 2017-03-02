package model.observable;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is the facade of our chessBoard, it can be manipulated through the
 * ChessGame's methods. It extends observable and is being observed by the GUI.
 */
public class ChessGame extends Observable {

    private Echiquier m_echiquier;

    public ChessGame(){
        m_echiquier = new Echiquier();
    }

    /**
     * This method calls the 'move' method from the class 'Echiquier'.
     * If the move is successful, switch players et notify the observers with the
     * new positions on the chessboard.
     *
     * @param xInit Initial x-axis of the piece to move.
     * @param yInit Initial y-axis of the piece to move.
     * @param xFinal Destination's x-axis of the piece to move.
     * @param yFinal Destination's y-axis of the piece to move.
     *
     * @return true if the move is OK, false if it is not.
     */
    public boolean move (int xInit, int yInit, int xFinal, int yFinal){
        boolean ret = false;

        if (m_echiquier.isMoveOk(xInit, yInit ,xFinal ,yFinal)){

            if( m_echiquier.move(xInit, yInit ,xFinal ,yFinal)){
                m_echiquier.switchJoueur();
                setChanged();
                super.notifyObservers(m_echiquier.getPiecesIHM());

                ret = true;
            }
        }

        return ret;
    }

    public boolean isMoveOK (int xInit, int yInit, int xFinal, int yFinal){
        return m_echiquier.isMoveOk(xInit,yInit,xFinal,yFinal);
    }

    public boolean isEnd(){
        return m_echiquier.isEnd();
    }

    public String getMessage(){
        return m_echiquier.getMessage();
    }

    public Couleur getColorCurrentPlayer(){
        return m_echiquier.getColorCurrentPlayer();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);

        setChanged();
        notifyObservers(m_echiquier.getPiecesIHM());
    }

    /**
     * Check through the board which square is an OK move and store it in a list
     * @param initCoord Initial coordinate of the piece to move
     * @return The list of index of the components that can be highlighted.
     */
    public List<Integer> highlightMoves(Coord initCoord){
        List<Integer> highlightedSquares = new ArrayList<Integer>();

        for (int x_final = 0; x_final < 8; x_final++) {
            for (int y_final = 0; y_final < 8 ; y_final++) {

                if(isMoveOK(initCoord.x, initCoord.y, x_final, y_final)){
                    //System.out.println("Coordinate x=" + x_final + " & y=" + y_final + " can be destination.");
                    highlightedSquares.add(y_final*8 + x_final);
                }
            }
        }

        return highlightedSquares;
    }
}
