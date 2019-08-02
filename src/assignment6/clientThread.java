package assignment6;

import java.util.ArrayList;
import java.util.List;

import static assignment6.Theater.syncLog;

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

        if (BookingClient.DEBUG && numCustomers < 1) {
            System.out.println(BXID + ": No customers");
        }

        // While there are still people in line
        for (int currentCustomer = 1; currentCustomer <= numCustomers; currentCustomer++) {

            synchronized (Theater.seatMap) {
                if (BookingClient.DEBUG) {
                    System.out.println(BXID + ": (" + currentCustomer + "/" + numCustomers + ")");
                }

                Theater.Seat bestSeat = theater.bestAvailableSeat();

                // if there are no available seats: exit
                if (bestSeat == null) {

                    if (BookingClient.DEBUG) {
                        System.out.println("-\tNo more seats available: " + BXID);
                    }
                    break; // Thread will reach the end of run --> will not try to run again

                } else {

                    // Book the best seat
                    bestSeat.setBXID(BXID);
                    bestSeat.setCID(currentCustomer);

                    // Mark it as taken
                    Theater.seatMap.set(bestSeat.getRowNum() * theater.getSeatsInRow() + bestSeat.getSeatNum(), bestSeat);


                    // Add the ticket to the log
                    //synchronized (syncLog) {
                        syncLog.add(bestSeat); // TODO: synchronize this with getTransactionLog()
                    //}
                    if (BookingClient.DEBUG) {
                        System.out.println("-\tSync Log: " + syncLog.toString());
                    }

                    // Print the ticket
                    theater.printTicket(bestSeat.getBXID(), bestSeat, currentCustomer);

                    if (BookingClient.DEBUG) {
                        System.out.println("-\tSeat Added: " + BXID + ":" + currentCustomer + " - " + bestSeat.toString());
                    }
                }
            }
                    // Let other threads have a turn --> synchronize on seatMap or syncLog?
                    // TODO: figure out how to force the thread to give up its key here, and return at a later time to the same place
                    try {
                        Thread.yield();
                        Thread.sleep(8); // Try to give Threads that have not yielded yet priority
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                //} // close else

            //} // close synchronized()
        }
    }

    public void start() {

        if (BookingClient.DEBUG) {
            System.out.println("==> STARTED " + BXID + " <==");
        }

        // Start the custom Thread using an underlying Thread object
        if (cThread == null) {
            cThread = new Thread(this, BXID);
            cThread.start();
        }

    }
}
