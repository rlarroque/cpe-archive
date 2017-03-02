package launcher.launcherLocal;


import controler.controlerLocal.ChessGameControler;
import model.observable.ChessGame;
import vue.ChessGameGUI;

import javax.swing.*;

/**
 * Entry point of our application
 */
public class LauncherGUI{

    /**
     * Instantiate models, controllers and views.
     * Specify chessGame's observer, which is the GUI
     */
    public LauncherGUI(){
        ChessGame chessGame = new ChessGame();
        ChessGameControler chessGameControler = new ChessGameControler(chessGame);
        ChessGameGUI gui = new ChessGameGUI(chessGameControler);

        chessGame.addObserver(gui);

        gui.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        gui.pack();
        gui.setResizable(true);
        gui.setLocationRelativeTo( null );
        gui.setVisible(true);

        System.out.println("Number of observers: " + chessGame.countObservers());
    }

    public static void main(String[] args) {
       new LauncherGUI();
    }
}
