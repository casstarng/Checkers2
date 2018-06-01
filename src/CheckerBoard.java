import java.awt.*;

public class CheckerBoard {

    static CheckerPiece[][] board = new CheckerPiece[8][8];

    public CheckerBoard(){
        // Initialize Black Pieces
        for (int i = 0; i < 8; i++){
            for (int k = 0; k < 3; k++){
                if ((i + k) % 2 == 0) board[k][i] = new CheckerPiece(Color.BLACK);
            }
        }

        // Initialize Red Pieces
        for (int i = 0; i < 8; i++){
            for (int k = 7; k > 4; k--){
                if ((i + k) % 2 == 0) board[k][i] = new CheckerPiece(Color.RED);
            }
        }
    }

    public CheckerPiece[][] getBoard(){
        return board;
    }

    public void movePiece(int y, int x, int g, int h, Color color, boolean isKing){
        deletePiece(y, x);
        insertPiece(g, h, color, isKing);
    }

    public void deletePiece(int y, int x){
        board[y][x] = null;
    }

    public void insertPiece(int y, int x, Color color, boolean isKing) {
        board[y][x] = new CheckerPiece(color);
        if (isKing) board[y][x].setKing();
    }

}
