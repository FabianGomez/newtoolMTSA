import MTSAEnactment.ar.uba.dc.lafhis.enactment.Enactor;
import MTSAEnactment.ar.uba.dc.lafhis.enactment.TransitionEvent;


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

    private GridEnvironmentSimulationUI simulationWindow;

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
            moveTo(0,1);
        } else if (transitionEvent.getAction().equals(goW)) {
            moveTo(0,-1);
        } else if (transitionEvent.getAction().equals(goN)) {
            moveTo(-1,0);
        } else if (transitionEvent.getAction().equals(goS)) {
            moveTo(1,0);
        } else if (transitionEvent.getAction().equals(detourE)) {
            moveTo(0,1);
        } else if (transitionEvent.getAction().equals(detourW)) {
            moveTo(0,-1);
        } else if (transitionEvent.getAction().equals(detourN)) {
            moveTo(-1,0);
        } else if (transitionEvent.getAction().equals(detourS)) {
            moveTo(1,0);
        } else if (transitionEvent.getAction().equals(nodetour)) {
            moveTo(0,0);
        }

        simulationWindow.repaint();

    }
    private void moveTo(int vertical, int horizontal){
        simulationWindow.moveTo(vertical,horizontal);
    }

    @Override
    public void setUp() {
        System.out.println("SET UP");
        simulationWindow = new GridEnvironmentSimulationUI();
    }

    @Override
    public void tearDown() {
        System.out.println("TEAR DOWN");
        simulationWindow.tearDown();
    }
}





