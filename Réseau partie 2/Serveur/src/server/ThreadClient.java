/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bastin
 */
public class ThreadClient extends Thread {
    private SourceTasks tasksToExecute;
    private Runnable onGoingTask;
    
    public ThreadClient(SourceTasks st)
    {
        tasksToExecute = st;
    }
    
    @Override
    public void run()
    {
        while(!isInterrupted())
        {
            try {
                onGoingTask = tasksToExecute.getTask();
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            onGoingTask.run();
        }
    }
}
