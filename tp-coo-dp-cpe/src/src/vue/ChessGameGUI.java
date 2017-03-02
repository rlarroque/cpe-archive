package vue;

import controler.controlerLocal.ChessGameControler;
import model.Coord;
import model.PieceIHM;
import tools.ChessImageProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This is the view oof our project. The visual representation of the chessboard is
 * created here.
 * This class is observing the ChessGame's class. When the GUI receives a notify, the position
 * of all the available pieces are passed as an argument and redrawing the board is possible.
 */
public class ChessGameGUI extends JFrame implements MouseListener, MouseMotionListener, Observer{

    private JLayeredPane m_layeredPane;
    private JPanel m_chessBoard;
    private JPanel m_chessInfo;
    private JLabel m_chessPiece;
    private int xAdjustment;
    private int yAdjustment;

    private Coord initCoord;
    private Coord finaleCoord;
    private ChessGameControler m_chessGameControler;

    private List<PieceIHM> currentListOfPieces;
    private List<Integer> highlightedSquares;

    public ChessGameGUI(ChessGameControler chessGameControler){
        m_chessGameControler = chessGameControler;

        Dimension totalSize = new Dimension(600, 600);
        Dimension boardSize = new Dimension(600, 600);
        Dimension infoSize = new Dimension(600, 100);

        // Use a Layered Pane for this this application
        m_layeredPane = new JLayeredPane();
        getContentPane().add(m_layeredPane);
        m_layeredPane.setPreferredSize(totalSize);
        m_layeredPane.addMouseListener(this);
        m_layeredPane.addMouseMotionListener(this);

        // Add a chess board to the LayeredPane
        m_chessBoard = new JPanel();
        m_layeredPane.add(m_chessBoard, JLayeredPane.DEFAULT_LAYER);
        m_chessBoard.setLayout( new GridLayout(8, 8) );
        m_chessBoard.setPreferredSize( boardSize );
        m_chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        // Create all the squares, which are JPanels. They will contains the pieces (JLabels)
        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel( new BorderLayout() );
            m_chessBoard.add( square );

            int row = (i / 8) % 2;
            if (row == 0)
                square.setBackground( i % 2 == 0 ? Color.black : Color.white );
            else
                square.setBackground( i % 2 == 0 ? Color.white : Color.black );
        }

        m_chessInfo = new JPanel();
        m_layeredPane.add(m_chessInfo, JLayeredPane.DEFAULT_LAYER);
        m_chessInfo.setPreferredSize( infoSize );
        m_chessInfo.setBounds(0, 600, infoSize.width, infoSize.height);

        highlightedSquares = new ArrayList<Integer>();
    }

    /**
     * On mouse pressed, if a piece is available at the current position, the coords
     * are stored for a future use and the piece is moved to the layeredPane.
     * The piece can now be dragged.
     * @param e MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent e) {
        m_chessPiece = null;

        // Used to find the deepest child that contains the specified position
        Component c =  m_chessBoard.findComponentAt(e.getX(), e.getY());

        // If it is a JPanel, there is no piece available to be dragged here
        if (c instanceof JLabel) {
            Point parentLocation = c.getParent().getLocation();
            initCoord = new Coord(parentLocation.x / 75, parentLocation.y / 75);

            xAdjustment = parentLocation.x - e.getX();
            yAdjustment = parentLocation.y - e.getY();

            m_chessPiece = (JLabel) c;
            m_chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
            m_chessPiece.setSize(m_chessPiece.getWidth(), m_chessPiece.getHeight());
            m_layeredPane.add(m_chessPiece, JLayeredPane.DRAG_LAYER);

            // Highlight moves available according to the piece pressed and the board.
            highlightSquares(initCoord);
        }
    }

    /**
     * On mouse released, if the move is OK then move the piece to the destination.
     * If the move isn't, just put back the current piece in the initial location.
     * @param e MouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        // If no pieces are being currently dragged, exit.
        if(m_chessPiece == null) return;

        m_chessPiece.setVisible(false);
        Component c =  m_chessBoard.getComponentAt(e.getX(), e.getY());
        finaleCoord = new Coord((e.getX() - e.getX()%75)/75, (e.getY() - e.getY()%75)/75);

        if(m_chessGameControler.move(initCoord, finaleCoord)){
            Container parent = (Container)c;
            parent.add(m_chessPiece);
            m_chessPiece.setVisible(true);
        }
        else{
            JPanel panel = (JPanel) m_chessBoard.getComponent(initCoord.y*8 + initCoord.x);
            panel.add(m_chessPiece);
            m_chessPiece.setVisible(true);
        }

        if(!highlightedSquares.isEmpty()){
            unhighlightSquares();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (m_chessPiece == null) return;
        m_chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
    }

    @Override
    public void update(Observable o, Object arg) {
        currentListOfPieces = (List<PieceIHM>) arg;

        drawPieces(currentListOfPieces);
        System.out.println(m_chessGameControler.getMessage());
    }

    /**
     * Draw the pieces one by one by iterating the list of pieces.
     * For each 'PieceIHM' loop on the possibly multiples coords. (There can be a maximum of
     * 2 pieces per type per color, 8 for the 'Pions')
     * @param listOfPieces List of pieces from the current chessboard.
     */
    private void drawPieces(List<PieceIHM> listOfPieces){
        JLabel piece;
        JPanel panel;

        for (PieceIHM pIHM: listOfPieces) {

            for (Coord c: pIHM.getList()) {
                piece = new JLabel(new ImageIcon(ChessImageProvider.getImageFile(pIHM.getTypePiece(),pIHM.getCouleur())));
                panel = (JPanel) m_chessBoard.getComponent(c.y*8 + c.x);

                panel.removeAll();
                panel.add(piece);
            }
        }
    }

    /**
     * Highlight squares available for a move.
     */
    private void highlightSquares(Coord initCoord){
        highlightedSquares = m_chessGameControler.highlightMoves(initCoord);

        for(Integer i: highlightedSquares){
            m_chessBoard.getComponent(i).setBackground(Color.RED);
        }
    }

    /**
     * Turn back squares to their initial colors.
     */
    private void unhighlightSquares(){
        for(Integer i: highlightedSquares){
            int row = (i / 8) % 2;
            if (row == 0)
                m_chessBoard.getComponent(i).setBackground( i % 2 == 0 ? Color.black : Color.white );
            else
                m_chessBoard.getComponent(i).setBackground( i % 2 == 0 ? Color.white : Color.black );
        }
    }
}
