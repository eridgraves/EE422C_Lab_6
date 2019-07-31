package assignment6;

import java.util.ArrayList;
import java.util.List;

public class clientThread implements Runnable {

    Thread cThread;
    private int numCustomers;
    private String BXID;

    clientThread(String id, int nc) {

        // Set the Box Office ID for this thread
        BXID = id;

        // And the number of customers in line
        numCustomers = nc;

    }

    @Override
    public void run() {

        // While there are seats availible in the theater --> Theater.bestSeatAvailable != null

        // Book the best seat

        // Mark it as taken

        // Let other threads have a turn --> synchronize on seatMap or syncLog?


//        while(CountMain.list.size() < 100) {
//
//            // old version
//            //synchronized (CountMain.list) {
//            CountMain.lock.lock();
//
//            System.out.print("Thread " + threadName + " running: ");
//
//            if(CountMain.list.size() < 100) {
//
//
//                CountMain.list.add(threadName);
//                System.out.println(CountMain.list.toString());
//
//                // Force threads to run in order?
//            }
//            try{
//                CountMain.list.wait();//Thread.sleep(50); // Sleep to give other threads that havent gone a chance to go
//            }catch (InterruptedException e){
//                Thread.currentThread().interrupt();
//
//            }
//            CountMain.lock.unlock();
//            CountMain.list.notify();
//            //}
//
//
////            Thread.yield();
//
//
//        }
//
//        // does putting this here mean that it runs after the synchronized block finishes?
//        if(CountMain.list.size() >= 10)
//            System.out.println("Thread " + threadName + " finished");

    }

    public void start() {

        if(BookingClient.DEBUG) {
            System.out.println("Thread started for: " + BXID);
        }

        // Start the custom Thread using an underlying Thread object
        if (cThread == null) {
            cThread = new Thread(this, BXID);
            cThread.start();
        }

    }
}
