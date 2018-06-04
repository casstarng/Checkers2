import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Cassidy Tarng on 5/8/2018.
 */
public class MainController {

    public static void main(String[] args){


        CheckerBoard board = new CheckerBoard();

        JPanel commandPanel = new JPanel();
        commandPanel.setBorder(new EmptyBorder(175,0,100,0));
        JButton hintButton = new JButton("Hint");

        Box box = Box.createVerticalBox();
        box.add(hintButton);
        commandPanel.add(box, BorderLayout.CENTER);

        CheckerBoardManager boardManager = new CheckerBoardManager(board);
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1, 2));
        frame.setTitle("Checkers");
        frame.setSize(850, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardManager.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                String coord = getMouseCoordinate(x, y);
                boardManager.select(coord);
                boardManager.repaint();
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {}
        });

        frame.add(boardManager);
        frame.add(commandPanel);

    }

    private static String getMouseCoordinate(int x, int y){
        if ( 10 < x && x < 60 && 10 < y && y < 60 ) return "0-0";
        else if ( 110 < x && x < 160 && 10 < y && y < 60 ) return "0-2";
        else if ( 210 < x && x < 260 && 10 < y && y < 60 ) return "0-4";
        else if ( 310 < x && x < 360 && 10 < y && y < 60 ) return "0-6";

        else if ( 60 < x && x < 110 && 60 < y && y < 110 ) return "1-1";
        else if ( 160 < x && x < 210 && 60 < y && y < 110 ) return "1-3";
        else if ( 260 < x && x < 310 && 60 < y && y < 110 ) return "1-5";
        else if ( 360 < x && x < 410 && 60 < y && y < 110 ) return "1-7";

        else if ( 10 < x && x < 60 && 110 < y && y < 160 ) return "2-0";
        else if ( 110 < x && x < 160 && 110 < y && y < 160 ) return "2-2";
        else if ( 210 < x && x < 260 && 110 < y && y < 160 ) return "2-4";
        else if ( 310 < x && x < 360 && 110 < y && y < 160 ) return "2-6";

        else if ( 60 < x && x < 110 && 160 < y && y < 210 ) return "3-1";
        else if ( 160 < x && x < 210 && 160 < y && y < 210 ) return "3-3";
        else if ( 260 < x && x < 310 && 160 < y && y < 210 ) return "3-5";
        else if ( 360 < x && x < 410 && 160 < y && y < 210 ) return "3-7";

        else if ( 10 < x && x < 60 && 210 < y && y < 260 ) return "4-0";
        else if ( 110 < x && x < 160 && 210 < y && y < 260 ) return "4-2";
        else if ( 210 < x && x < 260 && 210 < y && y < 260 ) return "4-4";
        else if ( 310 < x && x < 360 && 210 < y && y < 260 ) return "4-6";

        else if ( 60 < x && x < 110 && 260 < y && y < 310 ) return "5-1";
        else if ( 160 < x && x < 210 && 260 < y && y < 310 ) return "5-3";
        else if ( 260 < x && x < 310 && 260 < y && y < 310 ) return "5-5";
        else if ( 360 < x && x < 410 && 260 < y && y < 310 ) return "5-7";

        else if ( 10 < x && x < 60 && 310 < y && y < 360 ) return "6-0";
        else if ( 110 < x && x < 160 && 310 < y && y < 360 ) return "6-2";
        else if ( 210 < x && x < 260 && 310 < y && y < 360 ) return "6-4";
        else if ( 310 < x && x < 360 && 310 < y && y < 360 ) return "6-6";

        else if ( 60 < x && x < 110 && 360 < y && y < 410 ) return "7-1";
        else if ( 160 < x && x < 210 && 360 < y && y < 410 ) return "7-3";
        else if ( 260 < x && x < 310 && 360 < y && y < 410 ) return "7-5";
        else if ( 360 < x && x < 410 && 360 < y && y < 410 ) return "7-7";
        return null;
    }

}
