import javax.swing.*;
import java.awt.event.*;

public class DialogCellAction extends JDialog {

    public static final int CONSTCLEAR = 1;
    public static final int CONSTSTART = 2;
    public static final int CONSTGOAL = 3;
    public static final int CONSTDANGER = 4;

    private JPanel contentPane;
    private JButton startButton;
    private JButton cancelButton;
    private JButton goalButton;
    private JButton dangerButton;
    private JButton clearButton;

    public int dialogResponse;

    public DialogCellAction() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(startButton);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogResponse = CONSTSTART;
                onOK();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogResponse = CONSTCLEAR;
                onOK();
            }
        });
        goalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogResponse = CONSTGOAL;
                onOK();
            }
        });
        dangerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogResponse = CONSTDANGER;
                onOK();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DialogCellAction dialog = new DialogCellAction();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);

    }
}
