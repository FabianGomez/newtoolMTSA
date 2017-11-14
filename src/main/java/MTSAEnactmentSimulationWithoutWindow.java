import MTSAEnactment.ar.uba.dc.lafhis.enactment.BaseController;
import MTSAEnactment.ar.uba.dc.lafhis.enactment.Enactor;
import MTSTools.ac.ic.doc.mtstools.model.MTS;
import MTSTools.ac.ic.doc.mtstools.model.impl.LTSAdapter;
import ltsa.ac.ic.doc.mtstools.util.fsp.AutomataToMTSConverter;
import ltsa.enactment.EnactmentOptions;
import ltsa.enactment.EnactorFactory;
import ltsa.enactment.SchedulerFactory;
import ltsa.lts.CompositeState;
import ltsa.ui.HPWindow;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;


import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class MTSAEnactmentSimulationWithoutWindow implements Runnable {

    //Simulation Thread
    private static Thread updateThread;
    private BaseController<Long, String> controllerScheduler;
    private Set<Enactor<Long, String>> enactors = new HashSet<Enactor<Long, String>>();

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private boolean close = false;

    public void runSimulation(CompositeState currentComposition,
                              ApplicationContext applicationContext,
                              EnactmentOptions<State, Action> enactmentOptions)
    {

        EnactorFactory<Long, String> enactorFactory = applicationContext.getBean(EnactorFactory.class);
        if (enactorFactory == null)
        {
            logger.error("Enactor Factory not found");
            return;
        }


        if (enactmentOptions.enactors != null)
        {
            for (String enactorName : enactmentOptions.enactors)
            {
                try {
                    enactors.add(enactorFactory.getEnactor(enactorName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        MTS<Long, String> mts = AutomataToMTSConverter.getInstance().convert(currentComposition.composition);
        LTSAdapter<Long, String> ltsAdapter = new LTSAdapter<Long, String>(mts, MTS.TransitionType.REQUIRED);
        Set<String> controllableActions;

        controllableActions = currentComposition.goal.getControllableActions();



        SchedulerFactory<Long, String> schedulerFactory = applicationContext.getBean(SchedulerFactory.class);
        if (schedulerFactory == null)
        {
            logger.error("Scheduler factory not found");
            return;
        }
        BaseController<Long, String> controllerScheduler;
        try {
            controllerScheduler = schedulerFactory.getScheduler(enactmentOptions.scheduler, ltsAdapter, controllableActions);
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }


        for (Enactor<Long, String> enactor : enactors)
        {
            try {
                enactor.start();
                controllerScheduler.addTransitionEventListener(enactor);
                enactor.addTransitionEventListener(controllerScheduler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.setControllerScheduler(controllerScheduler);

        logger.info("Simulation setup: ");
        logger.info("Scheduler: " + this.getControllerScheduler().getName());

        controllerScheduler.start();

        try {
            this.getControllerScheduler().takeNextAction();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {

                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    return  false;
                }

                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    close = true;
                return false;
            }
        });

        updateThread = new Thread(this);
        updateThread.start();
    }

    /**
     * @return the controllerScheduler
     */
    private synchronized BaseController<Long, String> getControllerScheduler() {
        return controllerScheduler;
    }
    /**
     * @param controllerScheduler the controllerScheduler to set
     */
    private synchronized void setControllerScheduler(
            BaseController<Long, String> controllerScheduler) {
        this.controllerScheduler = controllerScheduler;
    }

    /**
     * @return the logger
     */
    private synchronized Logger getLogger() {
        return logger;
    }


    @Override
    public void run() {
        this.getLogger().info("Simulation started");

        while(!close)
        {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onClose();
    }

    private void onClose(){
        this.getLogger().info("Simulation ended");
        for (Enactor<Long, String> enactor : this.enactors)
        {
            enactor.tearDown();
        }
        controllerScheduler.tearDown();

    }
}
