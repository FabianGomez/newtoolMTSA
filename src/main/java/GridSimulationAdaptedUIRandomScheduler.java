import MTSAEnactment.ar.uba.dc.lafhis.enactment.gui.UIControllerGui;
import MTSTools.ac.ic.doc.commons.relations.Pair;
import MTSTools.ac.ic.doc.mtstools.model.LTS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.management.GarbageCollectorMXBean;
import java.util.Set;

import static java.lang.Thread.sleep;

/**
 * @author Fabian
 *
 */

public class GridSimulationAdaptedUIRandomScheduler<State, Action> extends UIRandomScheduler<State, Action> {

	private boolean actionFired = false;
	public final static String SCHEDULERNAME = "GridSimulationAdaptedUIRandomScheduler";

	private final static double VELOCITYSLOW = 0.5;
	private final static double VELOCITYNORMAL = 0.1;
	private final static double VELOCITYFAST = 0.002;
	private final static double VELOCITYFASTER = 0.0004;

	private JComboBox velocityList;
	private KeyEventDispatcher keyEventDispatcher;

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

		String[] velocityStrings = { "SLOW", "NORMAL", "FAST", "FASTER" };
		velocityList = new JComboBox(velocityStrings);
		velocityList.setSelectedIndex(1);

		Container panel = uiControllerGui.getContentPane();
		panel.add(velocityList, BorderLayout.PAGE_END);
		uiControllerGui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	@Override
	public void takeNextAction() throws Exception {
		System.gc();
		super.takeNextAction();

		Pair<Action, State> currentPair	= lts.getTransitions(currentState).iterator().next();
		if(controllableActions.contains(currentPair.getFirst()))
			return;

		try {
			if(keyEventDispatcher != null)
				KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);
		}catch (Exception e){
		}

		keyEventDispatcher = new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {

				if (e.getID() == KeyEvent.KEY_RELEASED) {
					return  false;
				}

				String action = "";

				int keyCode = e.getKeyCode();
				switch( keyCode ) {
					case KeyEvent.VK_UP:
						action = "wind.n";
						break;
					case KeyEvent.VK_DOWN:
						action = "wind.s";
						break;
					case KeyEvent.VK_LEFT:
						action = "wind.w";
						break;
					case KeyEvent.VK_RIGHT :
						action = "wind.e";
						break;
					case KeyEvent.VK_SPACE :
						action = "nowind";
						break;
				}

				if(action.equals(""))
					return false;

				if(optUnControllableActions.contains(action)) {
					uiControllerGui.removeActions();
					fireAction(action);
				}

				return false;
			}
		};

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
		sleep(0,1);

		actionFired = false;

		new Thread()
		{
			public void run() {
				double seconds = 0;
				while(!actionFired){
					try {
						sleep(0,1);
						seconds += 0.0001 ;
						if(seconds >= getVELOCITY()){
							fireAction("nowind");
							return;
						}

					}catch (Exception e){

					}
				}
			}
		}.start();

	}

	@Override
	public void fireAction(String name)	{
		actionFired = true;
		try {
			if(keyEventDispatcher != null)
				KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);

			sleep(0,2);
		}catch (Exception e){
		}
		super.fireAction(name);

	}

	private double getVELOCITY(){
		switch(velocityList.getSelectedIndex()) {
			case 0:
				return GridSimulationAdaptedUIRandomScheduler.VELOCITYSLOW;
			case 1:
				return GridSimulationAdaptedUIRandomScheduler.VELOCITYNORMAL;
			case 2:
				return GridSimulationAdaptedUIRandomScheduler.VELOCITYFAST;
			case 3:
				return GridSimulationAdaptedUIRandomScheduler.VELOCITYFASTER;
		}
		return GridSimulationAdaptedUIRandomScheduler.VELOCITYNORMAL;
	}

}