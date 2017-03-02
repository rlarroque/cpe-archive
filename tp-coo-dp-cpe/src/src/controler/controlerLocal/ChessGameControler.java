package controler.controlerLocal;

import model.Coord;
import model.Couleur;
import model.observable.ChessGame;

import java.util.List;

/**
 * This controller is a link between the view and the model
 * The chessgame's move method is callable through this class.
 */
public class ChessGameControler implements ChessGameControlers{

    private ChessGame m_chessGame;

    public ChessGameControler(ChessGame chessGame){
        m_chessGame = chessGame;
    }

    @Override
    public boolean move(Coord initCoord, Coord finalCoord) {
        return m_chessGame.move(initCoord.x, initCoord.y, finalCoord.x, finalCoord.y);
    }

    @Override
    public List<Integer> highlightMoves(Coord initCoord) {
        return m_chessGame.highlightMoves(initCoord);
    }

    @Override
    public String getMessage() {
        return m_chessGame.getMessage();
    }

    @Override
    public boolean isEnd() {
        return m_chessGame.isEnd();
    }

    @Override
    public Couleur getColorCurrentPlayer() {
        return m_chessGame.getColorCurrentPlayer();
    }
}
