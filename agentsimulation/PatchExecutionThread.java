/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Patch;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Z98
 */
public class PatchExecutionThread implements Runnable {

    CyclicBarrier startSignal;
    CyclicBarrier doneSignal;
    LinkedBlockingQueue<Patch> patches;
    public PatchExecutionThread(LinkedBlockingQueue<Patch> p, CyclicBarrier startSignal, CyclicBarrier doneSignal)
    {
        patches = p;
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }
    @Override
    public void run() {
        Patch patch;
        while(true)
        {
            try {
                startSignal.await();
                while(true)
                {
                    patch = patches.poll();
                    if (patch == null) {
                        break;
                    }

                    patch.SetNext();
                }
                
                doneSignal.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(PatchExecutionThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BrokenBarrierException ex) {
                Logger.getLogger(PatchExecutionThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
