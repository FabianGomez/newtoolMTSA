import ltsa.control.util.ControlConstants;
import ltsa.dispatcher.TransitionSystemDispatcher;
import ltsa.enactment.EnactmentOptions;
import ltsa.enactment.EnactorFactory;
import ltsa.enactment.MTSAEnactmentSimulation;
import ltsa.enactment.SchedulerFactory;
import ltsa.lts.CompositeState;
import ltsa.lts.LTSCompiler;
import ltsa.lts.LTSInputString;
import ltsa.lts.WindowOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class ModelForm {
    private JPanel panel1;
    private JButton controllerButton;
    private JButton cancelButton;
    private JButton saveButton;
    private JTextArea textAreaLTS;
    private JTextArea textAreaResult;
    private JButton SimulationButton;


    private Model model;
    JFrame frame;
    private CompositeState controller;

    public ModelForm(Map mapParam, final String filename){
        final Map map = mapParam;

        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:newtool-context.xml");

        final SchedulerFactory schedulerFactory =  applicationContext.getBean(SchedulerFactory.class);
        final EnactorFactory enactorFactory = applicationContext.getBean(EnactorFactory.class);
        this.model = new Model(filename,mapParam);
        String text = "";
        for(String line : model.getLines())
            text += line + "\r\n";

        textAreaLTS.setText(text);

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }
                frame = new JFrame("MTSA TOOL");
                frame.setLayout(new BorderLayout());
                frame.add(panel1);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        controllerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    textAreaResult.setText("");
                    LTSInputString ltsinput = new LTSInputString(textAreaLTS.getText());
                    WindowOutput ltsoutput = new WindowOutput(textAreaResult);
                    String currentDirectory = filename;

                    LTSCompiler compiler = new LTSCompiler(ltsinput, ltsoutput, currentDirectory);
                    compiler.compile();
                    Hashtable cs = compiler.getComposites();
                    Hashtable ps = compiler.getProcesses();
                    Hashtable ex = compiler.getExplorers();
                    if (cs == null && ps == null) {
                        cs = new Hashtable();
                        ps = new Hashtable();
                        ex = new Hashtable();
                        compiler.parse(cs, ps, ex);
                    }

                    if (cs == null) {
                        JOptionPane.showMessageDialog(null, "There are errors in the file", "Error", 1, null);
                        return;
                    }

                    controller = compiler.continueCompilation("C");

                    TransitionSystemDispatcher.applyComposition(controller, ltsoutput);
                    if (controller.composition.name.contains(ControlConstants.NO_CONTROLLER)) {
                        controller = null;
                        JOptionPane.showMessageDialog(null, "No controller", "Error", 1, null);
                        return;
                    }

                    JOptionPane.showMessageDialog(null, "Controller found", "Correct", 2, null);

                    /*CONTROLADOR = compiler.continueCompilation("ANIMAR");
                    TransitionSystemDispatcher.applyComposition(CONTROLADOR,ltsoutput);
                    */
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", 1, null);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser dialog = new JFileChooser();
                    int returnVal = dialog.showOpenDialog(saveButton);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = dialog.getSelectedFile();
                        PrintWriter writer = new PrintWriter(file.getAbsolutePath().replace(".lts","") + ".lts", "UTF-8");

                        for(String line : model.getLines())
                            writer.println(line);
                        writer.close();
                    }
                }catch (Exception ex){}
            }
        });

        SimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(controller != null){

                        map.saveTemp();

                        MTSAEnactmentSimulation simulation = new MTSAEnactmentSimulation();
                        EnactmentOptions enactmentOptions = new EnactmentOptions();

                        List<String> schedulerNames = schedulerFactory.getSchedulersList();
                        enactmentOptions.scheduler = schedulerNames.get(4);
                        List<String> enactorsNames = enactorFactory.getEnactorNames();
                        int indexEnactorGridEnvironment = -1;
                        for(int index=0;index < enactorsNames.size();index++)
                            if(enactorsNames.get(index).equals(GridEnvironmentSimulation.ENACTORNAME))
                                indexEnactorGridEnvironment = index;
                        if (indexEnactorGridEnvironment == -1) {
                            JOptionPane.showMessageDialog(null, "Enactor 'GridSimulationEnactor' not found", "Error", 1, null);
                            return;
                        }

                        enactmentOptions.enactors = new LinkedList();
                        enactmentOptions.enactors.add(enactorsNames.get(indexEnactorGridEnvironment));
                        simulation.runSimulation(controller, applicationContext, enactmentOptions);
                    }else{
                        JOptionPane.showMessageDialog(null, "There is no controller done", "Error", 1, null);
                    }
                }catch (Exception ex){}
            }
        });
    }

}
