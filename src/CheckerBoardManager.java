import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Cassidy Tarng on 5/8/2018.
 */
public class CheckerBoardManager extends JPanel {

    CheckerBoard board = new CheckerBoard();
    Color nextTurn = Color.RED;
    Color currentTurn = Color.RED;
    boolean initialTurn = true;
    ArrayList<String> nextChain = null;
    ArrayList<Integer> commandWithKingDeleted = new ArrayList<>();
    ArrayList<Integer> commandWithKingCreated = new ArrayList<>();
    String selectedSpot;

    public CheckerBoardManager(CheckerBoard board){
        this.board = board;
    }

    /**
     * Draw the board
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // Paint CheckerBoard
        int y = 10;
        for(int i = 0; i < 8; i++){
            int x = 10;
            for(int k = 0; k < 8; k++){
                if ((k + i) % 2 == 0) g.setColor(Color.LIGHT_GRAY);
                else g.setColor(Color.GRAY);
                g.fillRect(x, y, 50, 50);

                x += 50;
            }
            y += 50;
        }

        // Paint CheckerPiece
        for(int i = 0; i < board.getBoard().length; i++){
            for(int k = 0; k < board.getBoard()[i].length; k++){
                if (board.getBoard()[i][k] != null){
                    paintPiece(g, k, i, board.getBoard()[i][k].getColor(), board.getBoard()[i][k].isKing());
                }
            }
        }

        // Paint piece in a chain sequence
        if (nextChain != null){
            System.out.println(nextChain.toString());
            String[] spot = nextChain.get(0).split("-");
            g.setColor(Color.CYAN);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g.drawRect(10 + (Integer.parseInt(spot[1]) * 50),10 + (Integer.parseInt(spot[0]) * 50),50,50);
        }
        // Paint Pieces that can be moved
        else{
            ArrayList<String> moveablePieces = checkMoveablePieces();
            for (String piece : moveablePieces){
                String[] spot = piece.split("-");
                g.setColor(Color.CYAN);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                g.drawRect(10 + (Integer.parseInt(spot[1]) * 50),10 + (Integer.parseInt(spot[0]) * 50),50,50);
            }
        }

        // Clicked spot
        if (selectedSpot != null){
            g.setColor(Color.WHITE);
            String[] spot = selectedSpot.split("-");
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g.drawRect(10 + (Integer.parseInt(spot[1]) * 50),10 + (Integer.parseInt(spot[0]) * 50),50,50);
        }
    }

    /**
     * Paints the CheckerPiece given an (x, y) coordinate and color
     * @param g Graphics
     * @param x coordinate
     * @param y coordinate
     * @param color to paint
     */
    public void paintPiece(Graphics g, int x, int y, Color color, boolean king){
        g.setColor(color);
        g.fillOval(10 + (x * 50), 10 + (y * 50), 50, 50);

        // If piece is king, draw a K
        if (king){
            g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
            g.setColor(Color.WHITE);
            g.drawString("K", 27 + (x * 50),  45 + (y * 50));
        }
    }

    //TODO Mandatory jump can be jumped to a non-mandatory jump
    //TODO Issue with Black King only being able to move
    //TODO King can jump after becoming a king
    //TODO Check for winner
    //TODO Moveable spots
    public ArrayList<String> checkMoveablePieces(){
        ArrayList<String> moveablePieces = new ArrayList<>();
        ArrayList<String> jumpablePieces = new ArrayList<>();
        for(int i = 0; i < board.getBoard().length; i++){
            for(int k = 0; k < board.getBoard()[i].length; k++){
                // If piece is king
                if (board.getBoard()[i][k] != null && board.getBoard()[i][k].getColor() == currentTurn && board.getBoard()[i][k].isKing()){
                    // Get 1 space in all directions
                    if ((i - 1 >= 0 && k - 1 >= 0 && board.getBoard()[i-1][k-1] == null)
                            || (i - 1 >= 0 && k + 1 < board.getBoard().length && board.getBoard()[i-1][k+1] == null)
                            || (i + 1 < board.getBoard().length && k - 1 >= 0 && board.getBoard()[i+1][k-1] == null)
                            || (i + 1 < board.getBoard().length && k + 1 < board.getBoard().length && board.getBoard()[i+1][k+1] == null)){
                        if (!moveablePieces.contains(i + "-" + k))  moveablePieces.add(i + "-" + k);
                    }
                    // Get 2 space in all directions
                    if ((i - 2 >= 0 && k - 2 >= 0 && board.getBoard()[i-2][k-2] == null && board.getBoard()[i-1][k-1] != null && board.getBoard()[i-1][k-1].getColor() != board.getBoard()[i][k].getColor())
                            || (i - 2 >= 0 && k + 2 < board.getBoard().length && board.getBoard()[i-2][k+2] == null && board.getBoard()[i-1][k+1] != null && board.getBoard()[i-1][k+1].getColor() == board.getBoard()[i][k].getColor())
                            || (i + 2 < board.getBoard().length && k - 2 >= 0 && board.getBoard()[i+2][k-2] == null && board.getBoard()[i+1][k-1] != null && board.getBoard()[i+1][k-1].getColor() == board.getBoard()[i][k].getColor())
                            || (i + 2 < board.getBoard().length && k + 2 < board.getBoard().length && board.getBoard()[i+2][k+2] == null && board.getBoard()[i+1][k+1] != null && board.getBoard()[i+1][k+1].getColor() == board.getBoard()[i][k].getColor())){
                        if (!jumpablePieces.contains(i + "-" + k))  jumpablePieces.add(i + "-" + k);
                    }
                }
                // Red's turn
                if (currentTurn == Color.RED && board.getBoard()[i][k] != null && board.getBoard()[i][k].getColor() == Color.RED){
                    // Get 1 space up left
                    if (i - 1 >= 0 && k - 1 >= 0 && board.getBoard()[i-1][k-1] == null){
                        if (!moveablePieces.contains(i + "-" + k))  moveablePieces.add(i + "-" + k);
                    }
                    // Get 1 space up right
                    if (i - 1 >= 0 && k + 1 < board.getBoard().length && board.getBoard()[i-1][k+1] == null){
                        if (!moveablePieces.contains(i + "-" + k))  moveablePieces.add(i + "-" + k);
                    }
                    // Get 2 space up left, delete
                    if (i - 2 >= 0 && k - 2 >= 0 && board.getBoard()[i-2][k-2] == null && board.getBoard()[i-1][k-1] != null && board.getBoard()[i-1][k-1].getColor() == Color.BLACK){
                        if (!jumpablePieces.contains(i + "-" + k))  jumpablePieces.add(i + "-" + k);
                    }
                    // Get 2 space up right, delete
                    if (i - 2 >= 0 && k + 2 < board.getBoard().length && board.getBoard()[i-2][k+2] == null && board.getBoard()[i-1][k+1] != null && board.getBoard()[i-1][k+1].getColor() == Color.BLACK){
                        if (!jumpablePieces.contains(i + "-" + k))  jumpablePieces.add(i + "-" + k);
                    }
                }
                // Black's turn
                else if (currentTurn == Color.BLACK && board.getBoard()[i][k] != null && board.getBoard()[i][k].getColor() == Color.BLACK){
                    // Get 1 space down left
                    if (i + 1 < board.getBoard().length && k - 1 >= 0 && board.getBoard()[i+1][k-1] == null){
                        if (!moveablePieces.contains(i + "-" + k))  moveablePieces.add(i + "-" + k);
                    }
                    // Get 1 space down right
                    if (i + 1 < board.getBoard().length && k + 1 < board.getBoard().length && board.getBoard()[i+1][k+1] == null){
                        if (!moveablePieces.contains(i + "-" + k))  moveablePieces.add(i + "-" + k);
                    }
                    // Get 2 space down left, delete
                    if (i + 2 < board.getBoard().length && k - 2 >= 0 && board.getBoard()[i+2][k-2] == null && board.getBoard()[i+1][k-1] != null && board.getBoard()[i+1][k-1].getColor() == Color.RED){
                        if (!jumpablePieces.contains(i + "-" + k))  jumpablePieces.add(i + "-" + k);
                    }
                    // Get 2 space down right, delete
                    if (i + 2 < board.getBoard().length && k + 2 < board.getBoard().length && board.getBoard()[i+2][k+2] == null && board.getBoard()[i+1][k+1] != null && board.getBoard()[i+1][k+1].getColor() == Color.RED){
                        if (!jumpablePieces.contains(i + "-" + k))  jumpablePieces.add(i + "-" + k);
                    }
                }
            }
        }
        if (jumpablePieces.size() > 0) return jumpablePieces;
        else return moveablePieces;
    }

    public void select(String spot){
        if (spot == null){
            selectedSpot = null;
            return;
        }

        ArrayList<String> moveablePieces = checkMoveablePieces();
        String[] newSpot = spot.split("-");
        if (selectedSpot != null && board.getBoard()[Integer.parseInt(newSpot[0])][Integer.parseInt(newSpot[1])] != null){
            return;
        }
        // Initial select if none are selected, check if spot can be selected
        if (selectedSpot == null && moveablePieces.contains(spot)){
            selectedSpot = spot;
        }
        // If spot is selected and new spot is empty
        else if (selectedSpot != null && board.getBoard()[Integer.parseInt(newSpot[0])][Integer.parseInt(newSpot[1])] == null){
            String[] oldSpot = selectedSpot.split("-");
            // TODO ensure new spot is empty and not wrong direction
            CheckerPiece piece = board.getBoard()[Integer.parseInt(oldSpot[0])][Integer.parseInt(oldSpot[1])];
//            board.movePiece(Integer.parseInt(oldSpot[0]), Integer.parseInt(oldSpot[1]),
//                    Integer.parseInt(newSpot[0]), Integer.parseInt(newSpot[1]), piece.getColor(), piece.isKing());
            selectedSpot = null;

            String msg = move(Integer.parseInt(oldSpot[0]), Integer.parseInt(oldSpot[1]),
                    Integer.parseInt(newSpot[0]), Integer.parseInt(newSpot[1]), piece.getColor(), piece.isKing(), 0);

            if (msg != null || nextChain != null){
                return;
            }

            // Switch turns
            if (currentTurn == Color.RED) currentTurn = Color.BLACK;
            else currentTurn = Color.RED;
        }
    }

    private String move(int y, int x, int g, int h, Color color, boolean isKing, int commandCounter){
        if (nextChain != null) System.out.println(nextChain.toString());
        // Checks to see if a chain needs to be made
        if (nextChain != null){
            String currentCommand = y + "-" + x + "-" + g + "-" + h;
            if (nextChain.contains(currentCommand)){
                nextChain = null;
            }
            else {
                return "Illegal Move: Another jump must be made";
            }
        }

        // If piece reaches end, change piece to king
        if ((color == Color.BLACK && g == 7) || (color == Color.RED && g == 0)){
            isKing = true;
        }

        // Check if piece moves 1 space
        if (Math.abs(y - g) == 1 && Math.abs(x - h) == 1 && board.getBoard()[g][h] == null){
            // If piece is a king, then move
            if (isKing){
                board.movePiece(y, x, g, h, color, isKing);
            }
            // If piece is black, check if move goes down
            else if (color == Color.BLACK && g > y){
                board.movePiece(y, x, g, h, color, isKing);
            }
            // If piece is red, check if move goes up
            else if (color == Color.RED && g < y){
                board.movePiece(y, x, g, h, color, isKing);
            }
            else{
                return "Illegal Move";
            }
            return null;
        }
        // Check if piece moves 2 spaces (needs to delete opposing piece)
        else if (Math.abs(y - g) == 2 && Math.abs(x - h) == 2 && board.getBoard()[g][h] == null){
            // If piece is a king
            if (isKing){
                // If piece moves up-left
                if (y - g > 0 && x - h > 0 && board.getBoard()[y - 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y - 1, x - 1);
                }
                // If piece moves up-right
                else if (y - g > 0 && x - h < 0 && board.getBoard()[y - 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y - 1, x + 1);
                }
                // If piece moves down-left
                else if (y - g < 0 && x - h > 0 && board.getBoard()[y + 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y + 1, x - 1);
                }
                // If piece moves down-right
                else if (y - g < 0 && x - h < 0 && board.getBoard()[y + 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y + 1, x + 1);
                }
                else{
                    return "Illegal Move";
                }
                // Check if next step is chainable
                nextChain = checkForChain(g, h, color, isKing);
                if (nextChain != null) return null;
                return null;
            }
            // If piece is black, check if move goes down
            else if (color == Color.BLACK && g > y){
                // check if case moves left and that the piece will skip over a red piece
                if (x - h > 0 && board.getBoard()[y + 1][x - 1] != null && board.getBoard()[y + 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y + 1, x - 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                // check if case moves right and that the piece will skip over a red piece
                else if (x - h < 0 && board.getBoard()[y + 1][x + 1] != null && board.getBoard()[y + 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y + 1, x + 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                else {
                    return "Illegal Move";
                }
                return null;
            }
            // If piece is red, check if move goes up
            else if (color == Color.RED && g < y){
                // check if case moves left and that the piece will skip over a black piece
                if (x - h > 0 && board.getBoard()[y - 1][x - 1] != null && board.getBoard()[y - 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y - 1, x - 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                // check if case moves right and that the piece will skip over a black piece
                else if (x - h < 0 && board.getBoard()[y - 1][x + 1] != null && board.getBoard()[y - 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    board.deletePiece(y - 1, x + 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                else {
                    return "Illegal Move";
                }
                return null;
            }
            else{
                return "Illegal Move";
            }
        }
        return "Illegal Move";
    }

    /**
     * Handles moving pieces. All logic is contained here
     * @return message if a winner is announced or an illegal move is made
     */
    public String moveOLD(int y, int x, int g, int h, Color color, boolean isKing, int commandCounter){

        // Checks to see if a chain needs to be made
        if (nextChain != null){
            String currentCommand = y + "-" + x + "-" + g + "-" + h;
            if (nextChain.contains(currentCommand)){
                nextChain = null;
            }
            else {
                return "Illegal Move: Another jump must be made";
            }
        }
        // Checks to see if a jump needs to be made
        else{
            ArrayList<String> mandatoryJumps = getMandatoryJump();
            String currentCommand = y + "-" + x + "-" + g + "-" + h;
            if (mandatoryJumps.size() > 0 && !mandatoryJumps.contains(currentCommand)){
                return "Illegal Move: A jump must be made";
            }
        }

        // Check if player's turn is correct
        if (color != nextTurn && !initialTurn) return "Wrong turn";

        // If piece reaches end, change piece to king
        if ((color == Color.BLACK && g == 7) || (color == Color.RED && g == 0)){
            isKing = true;
            commandWithKingCreated.add(commandCounter);
        }

        // Check if piece moves 1 space
        if (Math.abs(y - g) == 1 && Math.abs(x - h) == 1 && board.getBoard()[g][h] == null){

            // If piece is a king, then move
            if (isKing){
                board.movePiece(y, x, g, h, color, isKing);
            }
            // If piece is black, check if move goes down
            else if (color == Color.BLACK && g > y){
                board.movePiece(y, x, g, h, color, isKing);
            }
            // If piece is red, check if move goes up
            else if (color == Color.RED && g < y){
                board.movePiece(y, x, g, h, color, isKing);
            }
            else{
                return "Illegal Move";
            }
        }
        // Check if piece moves 2 spaces (needs to delete opposing piece)
        else if (Math.abs(y - g) == 2 && Math.abs(x - h) == 2 && board.getBoard()[g][h] == null){
            // If piece is a king
            if (isKing){
                // If piece moves up-left
                if (y - g > 0 && x - h > 0 && board.getBoard()[y - 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    // Check if King is deleted, if so then record (needed for previous button)
                    if (board.getBoard()[y - 1][x - 1].isKing()) commandWithKingDeleted.add(commandCounter);
                    board.deletePiece(y - 1, x - 1);
                }
                // If piece moves up-right
                else if (y - g > 0 && x - h < 0 && board.getBoard()[y - 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    // Check if King is deleted, if so then record (needed for previous button)
                    if (board.getBoard()[y - 1][x + 1].isKing()) commandWithKingDeleted.add(commandCounter);
                    board.deletePiece(y - 1, x + 1);
                }
                // If piece moves down-left
                else if (y - g < 0 && x - h > 0 && board.getBoard()[y + 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    // Check if King is deleted, if so then record (needed for previous button)
                    if (board.getBoard()[y + 1][x - 1].isKing()) commandWithKingDeleted.add(commandCounter);
                    board.deletePiece(y + 1, x - 1);
                }
                // If piece moves down-right
                else if (y - g < 0 && x - h < 0 && board.getBoard()[y + 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    // Check if King is deleted, if so then record (needed for previous button)
                    if (board.getBoard()[y + 1][x + 1].isKing()) commandWithKingDeleted.add(commandCounter);
                    board.deletePiece(y + 1, x + 1);
                }

                // Check if next step is chainable
                nextChain = checkForChain(g, h, color, isKing);
                if (nextChain != null) return null;
            }
            // If piece is black, check if move goes down
            else if (color == Color.BLACK && g > y){
                // check if case moves left and that the piece will skip over a red piece
                if (x - h > 0 && board.getBoard()[y + 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    // Check if King is deleted, if so then record (needed for previous button)
                    if (board.getBoard()[y + 1][x - 1].isKing()) commandWithKingDeleted.add(commandCounter);
                    board.deletePiece(y + 1, x - 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                // check if case moves right and that the piece will skip over a red piece
                else if (x - h < 0 && board.getBoard()[y + 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    // Check if King is deleted, if so then record (needed for previous button)
                    if (board.getBoard()[y + 1][x + 1].isKing()) commandWithKingDeleted.add(commandCounter);
                    board.deletePiece(y + 1, x + 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                else {
                    return "Illegal Move";
                }
            }
            // If piece is red, check if move goes up
            else if (color == Color.RED && g < y){
                // check if case moves left and that the piece will skip over a black piece
                if (x - h > 0 && board.getBoard()[y - 1][x - 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    // Check if King is deleted, if so then record (needed for previous button)
                    if (board.getBoard()[y - 1][x - 1].isKing()) commandWithKingDeleted.add(commandCounter);
                    board.deletePiece(y - 1, x - 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                // check if case moves right and that the piece will skip over a black piece
                else if (x - h < 0 && board.getBoard()[y - 1][x + 1].getColor() != color){
                    board.movePiece(y, x, g, h, color, isKing);
                    // Check if King is deleted, if so then record (needed for previous button)
                    if (board.getBoard()[y - 1][x + 1].isKing()) commandWithKingDeleted.add(commandCounter);
                    board.deletePiece(y - 1, x + 1);

                    // Check if next step is chainable
                    nextChain = checkForChain(g, h, color, isKing);
                    if (nextChain != null) return null;
                }
                else {
                    return "Illegal Move";
                }
            }
            else{
                return "Illegal Move";
            }
        }
        else{
            return "Illegal Move";
        }

        // Change turns
        initialTurn = false;
        if (color == Color.RED) nextTurn = Color.BLACK;
        else if (color == Color.BLACK) nextTurn = Color.RED;

        // Check for a winner
        String win = checkWinner();
        if (win != null){
            return win;
        }
        else return null;
    }

    /**
     * Checks if a winner exists
     * @return winner message
     */
    public String checkWinner(){
        int redCount = 0;
        int blackCount = 0;
        // search through entire board and count number of reds and blacks
        for(int i = 0; i < board.getBoard().length; i++){
            for(int k = 0; k < board.getBoard()[i].length; k++){
                if (board.getBoard()[k][i] != null){
                    if (board.getBoard()[k][i].getColor() == Color.RED){
                        redCount++;
                    }
                    else if (board.getBoard()[k][i].getColor() == Color.BLACK){
                        blackCount++;
                    }
                }
            }
        }
        if (redCount == 0){
            return "Black Wins!";
        }
        else if (blackCount == 0){
            return "Red Wins!";
        }
        else return null;
    }

    /**
     * Checks whether a chain can be made
     * @return list of possible chains
     */
    public ArrayList<String> checkForChain(int y, int x, Color color, boolean isKing){
        ArrayList<String> chainOptions = new ArrayList<>();
        // If Color is black
        if (color == Color.BLACK || isKing){
            // If piece can jump down-right
            if (y + 2 < 8 && x + 2 < 8 && board.getBoard()[y + 1][x + 1] != null
                    && board.getBoard()[y + 1][x + 1].getColor() != color
                    && board.getBoard()[y + 2][x + 2] == null){
                chainOptions.add(y + "-" + x + "-" + (y + 2) + "-" + (x + 2));
            }
            // If piece can jump down-left
            if (y + 2 < 8 && x - 2 >= 0 && board.getBoard()[y + 1][x - 1] != null
                    && board.getBoard()[y + 1][x - 1].getColor() != color
                    && board.getBoard()[y + 2][x - 2] == null){
                chainOptions.add(y + "-" + x + "-" + (y + 2) + "-" + (x - 2));
            }
        }
        // If Color is red
        if (color == Color.RED || isKing){
            // If piece can jump up-right
            if (y - 2 >= 0 && x + 2 < 8 && board.getBoard()[y - 1][x + 1] != null
                    && board.getBoard()[y - 1][x + 1].getColor() != color
                    && board.getBoard()[y - 2][x + 2] == null){
                chainOptions.add(y + "-" + x + "-" + (y - 2) + "-" + (x + 2));
            }
            // If piece can jump up-left
            if (y - 2 >= 0 && x - 2 >= 0 && board.getBoard()[y - 1][x - 1] != null
                    && board.getBoard()[y - 1][x - 1].getColor() != color
                    && board.getBoard()[y - 2][x - 2] == null){
                chainOptions.add(y + "-" + x + "-" + (y - 2) + "-" + (x - 2));
            }
        }
        if (chainOptions.size() > 0) return chainOptions;
        else return null;
    }

    /**
     * Checks whether a mandatory jump exists
     * @return list of mandatory jumps
     */
    public ArrayList<String> getMandatoryJump(){
        ArrayList<String> mandatoryJumps = new ArrayList<>();
        for(int i = 0; i < board.getBoard().length; i++){
            for(int k = 0; k < board.getBoard()[i].length; k++){
                // check whether a jump exists
                if (!initialTurn && board.getBoard()[i][k] != null
                        && board.getBoard()[i][k].getColor() == nextTurn){
                    CheckerPiece piece = board.getBoard()[i][k];
                    Color pieceColor = piece.getColor();
                    Boolean isKing = piece.isKing();

                    ArrayList<String> jumps = checkForChain(i, k, pieceColor, isKing);
                    if (jumps != null) mandatoryJumps.addAll(jumps);
                }
            }
        }
        return mandatoryJumps;
    }

    /**
     * rewinds the command. Checks if a king was deleted or if a king was created
     */
    public void rewind(int y, int x, int g, int h, Color color, boolean isKing, int commandCounter){
        nextTurn = color;
        Color oppositeColor = null;
        boolean kingDeleted = false;

        // Check if a King was deleted at specific commandCounter
        if (commandWithKingDeleted.contains(commandCounter)) kingDeleted = true;

        // Check if a King was created at specific commandCounter
        if (commandWithKingCreated.contains(commandCounter)) isKing = false;

        if (color == Color.RED) oppositeColor = Color.BLACK;
        else if (color == Color.BLACK) oppositeColor = Color.RED;

        // Check if piece moves 1 space
        if (Math.abs(y - g) == 1 && Math.abs(x - h) == 1){
            board.deletePiece(g, h);
            board.insertPiece(y, x, color, isKing);
        }
        else {
            // If piece moved up-left
            if (y - g > 0 && x - h > 0){
                board.deletePiece(g, h);
                board.insertPiece(y - 1, x - 1, oppositeColor, kingDeleted);
                board.insertPiece(y, x, color, isKing);
            }
            // If piece moved up-right
            else if (y - g > 0 && x - h < 0){
                board.deletePiece(g, h);
                board.insertPiece(y - 1, x + 1, oppositeColor, kingDeleted);
                board.insertPiece(y, x, color, isKing);
            }
            // If piece moved down-left
            else if (y - g < 0 && x - h > 0){
                board.deletePiece(g, h);
                board.insertPiece(y + 1, x - 1, oppositeColor, kingDeleted);
                board.insertPiece(y, x, color, isKing);
            }
            // If piece moved down-right
            else if (y - g < 0 && x - h < 0){
                board.deletePiece(g, h);
                board.insertPiece(y + 1, x + 1, oppositeColor, kingDeleted);
                board.insertPiece(y, x, color, isKing);
            }
        }
    }

}

