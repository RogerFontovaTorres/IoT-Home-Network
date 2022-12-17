package iot.cloudservice.data;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PasiveWaitQueue<E> {

    // Data received from mqtt will be stored in this queue until kafka producer takes it (in order) and sends it to the AI agent
    private final ConcurrentLinkedQueue<E> queue;

    public PasiveWaitQueue(){
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void push(E message){
        this.queue.add(message);
        synchronized (this.queue){
            this.queue.notifyAll();
        }
    }

    public E poll(){
        if(this.isEmpty()){
            this.waitData();
        }
        return this.queue.poll();
    }

    private boolean isEmpty(){
        return this.queue.isEmpty();
    }

    // waits until it receives a notification from poll push method
    private void waitData(){
        synchronized (this.queue){
            try {
                this.queue.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
