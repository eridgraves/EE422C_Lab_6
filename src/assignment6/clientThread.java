package assignment6;

import java.util.ArrayList;
import java.util.List;

public class clientThread extends Thread {

    Thread cThread;
    private int numCustomers;
    private String BXID;
    private Theater theater;

    clientThread(String id, int nc, Theater th) {

        // Set the Box Office ID for this thread
        BXID = id;

        // And the number of customers in line
        numCustomers = nc;

        // Store the Theater this is acting on
        theater = th;

    }

    @Override
    public void run() { //TODO: Change the structure here

        // While there are still people in line
        for (int currentCustomer = 1; currentCustomer <= numCustomers; currentCustomer++) {

            synchronized (Theater.syncLog) { // TODO: need to restructure on one synchronized structure
                if (BookingClient.DEBUG) {
                    System.out.println("Thread " + BXID + " has control.");
                }

                // if there are no available seats: exit
                if (theater.bestAvailableSeat() == null) {

                    if (BookingClient.DEBUG) {
                        System.out.println("No more seats available: " + BXID);
                    }
                    break;

                } else {

                    Theater.Seat bestSeat = theater.bestAvailableSeat();

                    // Book the best seat
                    bestSeat.setBXID(BXID);
                    bestSeat.setCID(currentCustomer);

                    // Mark it as taken
                    Theater.seatMap.add(bestSeat.getRowNum() * theater.getSeatsInRow() + bestSeat.getSeatNum(), bestSeat);

                    if (BookingClient.DEBUG) {
                        System.out.println("Seat Added: " + BXID + ":" + currentCustomer + " - " + bestSeat.toString());
                    }

                    // Let other threads have a turn --> synchronize on seatMap or syncLog?
                    try {
                        Thread.yield();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public void start() {

        if (BookingClient.DEBUG) {
            System.out.println("Thread started for: " + BXID);
        }

        // Start the custom Thread using an underlying Thread object
        if (cThread == null) {
            cThread = new Thread(this, BXID);
            cThread.start();
        }

    }
}
