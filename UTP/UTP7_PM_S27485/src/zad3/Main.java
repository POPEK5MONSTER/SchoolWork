///**
// *
// *  @author Popowski Mateusz S27485
// *
// */
//
//package zad3;
//
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.concurrent.Future;
//
//public class Main extends JFrame {
//
//  private DefaultListModel<Future<String>> taskListModel;
//  private JList<Future<String>> taskJList;
//
//  public Main(){
//    super("FutureTask Manager");
//
//    taskListModel = new DefaultListModel<>();
//    taskJList = new JList<>(taskListModel);
//
//    JButton addButton = new JButton("Add Task");
//    JButton cancelButton = new JButton("Cancel Selected Task");
//    JButton showButton = new JButton("Show Results");
//
//    setLayout(new BorderLayout());
//    add(new JScrollPane(taskJList), BorderLayout.CENTER);
//
//
//
//    setSize(500,400);
//    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    setLocationRelativeTo(null);
//    setVisible(true);
//  }
//
//  public static void main(String[] args) {
//      SwingUtilities.invokeLater(new Runnable() {
//        @Override
//        public void run() {
//          new Main();
//        }
//      });
//  }
//}
