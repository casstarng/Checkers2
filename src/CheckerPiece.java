import java.awt.*;

/**
 * Created by Cassidy Tarng on 5/8/2018.
 */
public class CheckerPiece{
    private Color color;
    private boolean isKing = false;

    public CheckerPiece(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void setKing(){
        this.isKing = true;
    }

    public boolean isKing(){
        return this.isKing;
    }
}

