package server;

import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bastin
 */
public class ListTasks implements SourceTasks {
    
    private LinkedList listTasks;
    
    public ListTasks()
    {
        listTasks = new LinkedList();
    }

    @Override
    public synchronized Runnable getTask() throws InterruptedException {
        while(!existTask())
            wait();
        
        return (Runnable) listTasks.remove();
    }

    @Override
    public synchronized boolean existTask() {
        return !listTasks.isEmpty();
    }

    @Override
    public synchronized void recordTask(Runnable r) { 
       listTasks.addLast(r);  
       notify();
    }
    
}
