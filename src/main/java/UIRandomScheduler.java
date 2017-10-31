import MTSAEnactment.ar.uba.dc.lafhis.enactment.RandomController;
import MTSAEnactment.ar.uba.dc.lafhis.enactment.TakeFirstController;
import MTSAEnactment.ar.uba.dc.lafhis.enactment.TransitionEvent;
import MTSAEnactment.ar.uba.dc.lafhis.enactment.gui.UIControllerGui;
import MTSTools.ac.ic.doc.commons.relations.BinaryRelation;
import MTSTools.ac.ic.doc.commons.relations.Pair;
import MTSTools.ac.ic.doc.mtstools.model.LTS;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * @author Fabian
 *
 */

public class UIRandomScheduler<State, Action> extends RandomController<State, Action> {

	public UIControllerGui uiControllerGui = null;
	private Logger logger = LogManager.getLogger(TakeFirstController.class.getName());
	private List<Action> optUnControllableActions;


	public UIRandomScheduler(String name, LTS<State, Action> lts,
                             Set<Action> controllableActions) {
		super(name, lts, controllableActions);

	}

	public UIRandomScheduler(String name) {
		super(name);
	}
	
	@Override
	public void setUp() {
		super.setUp();
		this.setReactItSelf(true);
	}
	
	@Override
	public void takeNextAction() throws Exception {

		Iterator<Pair<Action,State>> stateIterator = lts.getTransitions(currentState).iterator();

		if(!stateIterator.hasNext()){
			String exceptionString = "";
			logger.error(String.format("[Scheduler::primitiveHandleTransitionEvent] there is no reachable state from currentState %s"
					, currentState.toString(), exceptionString));
			throw new Exception(exceptionString);
		}
		
		
		optUnControllableActions = new ArrayList<Action>();
		boolean hasNextControllableAction = false;
		boolean hasNextUnControllableAction = false;

		while(stateIterator.hasNext()){
			Pair<Action, State> currentPair	= stateIterator.next();
			
			if(!controllableActions.contains(currentPair.getFirst())) {
				optUnControllableActions.add(currentPair.getFirst());
				hasNextUnControllableAction = true;
			}
			if(controllableActions.contains(currentPair.getFirst()))
				hasNextControllableAction = true;

		}

		if(!hasNextUnControllableAction){
			Thread.sleep(100);
			super.takeNextAction();
		}else{
			if(!hasNextControllableAction){
				updateInterface();
			}else{
				//In this case the controller has controllable and uncontrollable action to fire
				Random randomGenerator = new Random();
				int binary = randomGenerator.nextInt(2);
				if(binary == 0) {
					updateInterface();
				} else {
					Thread.sleep(100);
					super.takeNextAction();
				}
			}
		}
	}

	private void updateInterface()
	{
		if (this.uiControllerGui == null) 
		{
			this.uiControllerGui = new UIControllerGui();
			this.uiControllerGui.setTitle(this.getName());
		}
		
		this.uiControllerGui.removeActions();
		
		for (Action action : this.optUnControllableActions)
			this.uiControllerGui.addUnControllableAction(action.toString(), action.toString(), new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() instanceof JButton)
					{
						JButton sourceButton = (JButton) e.getSource();
						String srcName = sourceButton.getName();
						uiControllerGui.removeActions();
						
						fireAction(srcName);
					}
					
				}
			});



		this.uiControllerGui.setVisible(true);
	}
	
	public void fireAction(String name)
	{
		if (this.optUnControllableActions == null) return;
	
		processActionList(this.optUnControllableActions, name);

	}
	private void processActionList(List<Action> actions, String actionName)
	{
		for (Action action : actions)
		{
			if (action.toString().equals(actionName))
			{
				this.uiControllerGui.appendMessage("Fire controllable action: " +  actionName);
				try {
					addTransition(new TransitionEvent<Action>(this, action));
				} catch (Exception e) {
					logger.error("Error firing transition event " + action.toString(), e);				
				}
				
			}
		}		
	}
	
	
	@Override
	public void tearDown() {
		this.uiControllerGui.dispose();
		this.uiControllerGui.setVisible(false);
		this.uiControllerGui = null;
	}

}
