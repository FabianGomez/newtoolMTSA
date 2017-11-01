import MTSAEnactment.ar.uba.dc.lafhis.enactment.Enactor;
import MTSAEnactment.ar.uba.dc.lafhis.enactment.TransitionEvent;
import MTSAEnactment.ar.uba.dc.lafhis.enactment.robot.PainterEnvironmentUI;
import MTSAEnactment.ar.uba.dc.lafhis.enactment.robot.RobotAdapter;

import javax.imageio.ImageIO;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Created by lnahabedian on 18/07/16.
 */
public class GridEnvironmentSimulationEnactor<State, Action> extends
        Enactor<State, Action> {

    // controllable
    private Action goE, goW, goN, goS, arrive;
    // uncontrollable
    private Action detourE, detourW, detourN, detourS;
    private Action nodetour;


    public final static String ENACTORNAME = "GridSimulationEnactor";

    private GridEnviromentSimulationUI simulationWindow;

    public GridEnvironmentSimulationEnactor(String name, Action goE, Action goW, Action goN, Action goS, Action detourE, Action detourW, Action detourN, Action detourS, Action nodetour, Action arrive) {
        super(name);
        this.goE = goE;
        this.goW = goW;
        this.goN = goN;
        this.goS = goS;
        this.detourE = detourE;
        this.detourW = detourW;
        this.detourN = detourN;
        this.detourS = detourS;
        this.nodetour = nodetour;
        this.arrive = arrive;
    }

    @Override
    protected void primitiveHandleTransitionEvent(TransitionEvent<Action> transitionEvent) throws Exception {

        if (transitionEvent.getAction().equals(goE)) {
            startingToMove(0,1);
        } else if (transitionEvent.getAction().equals(goW)) {
            startingToMove(0,-1);
        } else if (transitionEvent.getAction().equals(goN)) {
            startingToMove(-1,0);
        } else if (transitionEvent.getAction().equals(goS)) {
            startingToMove(1,0);
        } else if (transitionEvent.getAction().equals(detourE)) {
            finishingToMove(0,1);
        } else if (transitionEvent.getAction().equals(detourW)) {
            finishingToMove(0,-1);
        } else if (transitionEvent.getAction().equals(detourN)) {
            finishingToMove(-1,0);
        } else if (transitionEvent.getAction().equals(detourS)) {
            finishingToMove(1,0);
        } else if (transitionEvent.getAction().equals(nodetour)) {
            finishingToMove(0,0);
        }

        simulationWindow.repaint();

    }
    private void startingToMove(int vertical, int horizontal){
        simulationWindow.startingToMove(vertical,horizontal);
    }
    private void finishingToMove(int vertical, int horizontal){
        simulationWindow.finishingToMove(vertical,horizontal);
    }

    @Override
    public void setUp() {
        System.out.println("SET UP");
        simulationWindow = new GridEnviromentSimulationUI();
    }

    @Override
    public void tearDown() {
        System.out.println("TEAR DOWN");
        simulationWindow.dispatchEvent(new WindowEvent(simulationWindow, WindowEvent.WINDOW_CLOSING));
    }
}





