import MTSAEnactment.ar.uba.dc.lafhis.enactment.gui.UIControllerGui;
import MTSTools.ac.ic.doc.commons.relations.Pair;
import MTSTools.ac.ic.doc.mtstools.model.LTS;

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
				int seconds = 0;
				while(actionFired == false){
					try {
						sleep(250);
						seconds++;
						if(seconds == 20){
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
			sleep(500);
		}catch (Exception e){
		}
		super.fireAction(name);

	}

}