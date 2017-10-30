import MTSAEnactment.ar.uba.dc.lafhis.enactment.gui.UIControllerGui;
import MTSTools.ac.ic.doc.commons.relations.Pair;
import MTSTools.ac.ic.doc.mtstools.model.LTS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

import static java.lang.Thread.sleep;

/**
 * @author Fabian
 *
 */

public class GridSimulationAdaptedUIRandomScheduler<State, Action> extends UIRandomScheduler<State, Action> {

	private boolean actionFired = false;
	public final static String SCHEDULLERNAME = "GridSimulationAdaptedUIRandomScheduler";

	public final static double VELOCITYSLOW = 7;
	public final static double VELOCITYNORMAL = 5;
	public final static double VELOCITYFAST = 0.5;
	public final static double VELOCITYTOOFAST = 0.125;

	JComboBox velocitytList;

	public GridSimulationAdaptedUIRandomScheduler(String name, LTS<State, Action> lts,
												  Set<Action> controllableActions) {
		super(name, lts, controllableActions);

	}

	public GridSimulationAdaptedUIRandomScheduler(String name) {
		super(name);
	}
	
	@Override
	public void setUp() {
		super.setUp();
		this.setReactItSelf(true);

		uiControllerGui = new UIControllerGui();
		uiControllerGui.setTitle(this.getName());

		String[] velocityStrings = { "SLOW", "NORMAL", "FAST", "TOOFAST" };
		velocitytList = new JComboBox(velocityStrings);
		velocitytList.setSelectedIndex(1);
		Container panel = uiControllerGui.getContentPane();
		panel.add(velocitytList, BorderLayout.PAGE_END);


		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					public boolean dispatchKeyEvent(KeyEvent e) {

						if (e.getID() == KeyEvent.KEY_RELEASED) {
							return  false;
						}

						String action = "";

						int keyCode = e.getKeyCode();
						switch( keyCode ) {
							case KeyEvent.VK_UP:
								action = "detour.n";
								break;
							case KeyEvent.VK_DOWN:
								action = "detour.s";
								break;
							case KeyEvent.VK_LEFT:
								action = "detour.w";
								break;
							case KeyEvent.VK_RIGHT :
								action = "detour.e";
								break;
							case KeyEvent.VK_SPACE :
								action = "nodetour";
								break;
						}

						if(action.equals(""))
							return false;

						uiControllerGui.removeActions();
						fireAction(action);

						return false;
					}
				});
	}

	@Override
	public void takeNextAction() throws Exception {
		super.takeNextAction();

		Pair<Action, State> currentPair	= lts.getTransitions(currentState).iterator().next();
		if(controllableActions.contains(currentPair.getFirst()))
			return;

		actionFired = false;
		new Thread()
		{
			public void run() {
				double seconds = 0;
				while(actionFired == false){
					try {
						sleep(125);
						seconds += 0.125 ;
						if(seconds >= getVELOCITY()){
							fireAction("nodetour");
							return;
						}

					}catch (Exception e){

					}
				}
			}
		}.start();

	}

	@Override
	public void fireAction(String name)
	{
		actionFired = true;
		try {
			sleep(250);
		}catch (Exception e){
		}
		super.fireAction(name);

	}

	public double getVELOCITY(){
		switch(velocitytList.getSelectedIndex()) {
			case 0:
				return GridSimulationAdaptedUIRandomScheduler.VELOCITYSLOW;
			case 1:
				return GridSimulationAdaptedUIRandomScheduler.VELOCITYNORMAL;
			case 2:
				return GridSimulationAdaptedUIRandomScheduler.VELOCITYFAST;
			case 3:
				return GridSimulationAdaptedUIRandomScheduler.VELOCITYTOOFAST;
		}
		return GridSimulationAdaptedUIRandomScheduler.VELOCITYNORMAL;
	}

}