import java.util.*;
import javax.swing.*;
/**
 *
 * @author Ryan
 */
public class ABChess {
    static int humanAsWhite=-1;//1=human as white, 0=human as black
    static int globalDepth=4;//This number decided the computer difficulty as well as the level at which the algorithm searches
    public static void main(String[] args) {
        while (!"A".equals(Moves.chessBoard[Moves.kingPositionC/8][Moves.kingPositionC%8])) {Moves.kingPositionC++;}//get King's location
        while (!"a".equals(Moves.chessBoard[Moves.kingPositionL/8][Moves.kingPositionL%8])) {Moves.kingPositionL++;}//get king's location
        /*
         * My strategy is to create an alpha-beta tree diagram wich returns
         * the best outcome
         * 
         * (1234b represents row1,column2 moves to row3, column4 which captured
         * b (a space represents no capture). A sequence such as this is helpful
         * for debugging purposes.)
         */
        JFrame f=new JFrame("Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui=new UserInterface();
        f.add(ui);
        f.setSize(525, 550);
        f.setVisible(true);
        System.out.println(Moves.sortMoves(Moves.posibleMoves()));
        Object[] option={"Computer","Human"};
        humanAsWhite=JOptionPane.showOptionDialog(null, "Who should play as white?", "ABC Options", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
        if (humanAsWhite==0) {
            long startTime=System.currentTimeMillis();
            Moves.makeMove(alphaBeta(globalDepth, 1000000, -1000000, "", 0));
            long endTime=System.currentTimeMillis();
            System.out.println("That took "+(endTime-startTime)+" milliseconds");
            Moves.flipBoard();
            f.repaint();
        }
        //Moves.makeMove("7655 ");
        //Moves.undoMove("7655 ");
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(Moves.chessBoard[i]));
        }
    }
    public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {
        String list=Moves.posibleMoves();//Make a list of possible moves
        if (depth==0 || list.length()==0) {return move+(Rating.rating(list.length(), depth)*(player*2-1));}
        list=Moves.sortMoves(list);//This like is the pruning for the AlphBeta
        player=1-player;//either 1 or 0
        for (int i=0;i<list.length();i+=5) {//A recursive loop to create nodes.
            Moves.makeMove(list.substring(i,i+5));
            Moves.flipBoard();
            String returnString=alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
            int value=Integer.valueOf(returnString.substring(5));
            Moves.flipBoard();
            Moves.undoMove(list.substring(i,i+5));
            if (player==0) {//The initial node parameters
                if (value<=beta) {beta=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}
            } else {
                if (value>alpha) {alpha=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}
            }
            if (alpha>=beta) {
                if (player==0) {return move+beta;} else {return move+alpha;}
            }
        }//The nodes!
        if (player==0) {return move+beta;} else {return move+alpha;}
    }
}