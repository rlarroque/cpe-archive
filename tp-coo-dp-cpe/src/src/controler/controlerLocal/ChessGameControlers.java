package controler.controlerLocal;

import model.Coord;
import model.Couleur;

import java.util.List;

/**
 * Created by Thuranos on 11/13/2015.
 */
public interface ChessGameControlers {

    public boolean move(Coord initCoord, Coord finalCoord);
    public List<Integer> highlightMoves(Coord initCoord);
    public String getMessage();
    public boolean isEnd();
    public Couleur getColorCurrentPlayer();

}
