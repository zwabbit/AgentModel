/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Patch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Z98
 */
public class PatchExecutionThread implements Runnable {

    LinkedBlockingQueue<Patch> patches;
    public PatchExecutionThread(LinkedBlockingQueue<Patch> p)
    {
        patches = p;
    }
    @Override
    public void run() {
        Patch patch;
        while(true)
        {
            patch = patches.poll();
            if(patch == null)
                break;
            
            patch.SetNext();
        }
    }
    
}
