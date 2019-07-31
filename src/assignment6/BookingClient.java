/* MULTITHREADING <BookingClient.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Eric Graves
 * edg732
 * Slip days used: <0>
 * Summer 2019
 */
package assignment6;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.lang.Thread;

public class BookingClient {

    public static boolean DEBUG = true;

    /**
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */
    public BookingClient(Map<String, Integer> office, Theater theater) {

        //TODO: what goes here? Does it need a private copy of the map to be used during simulate()?



    }

    /**
     * Starts the box office simulation by creating (and starting) threads
     * for each box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are box offices
     */
    public List<Thread> simulate() {
        // Create empty list of Threads
        List<Thread> threadList = new ArrayList<>();

        // Create a new BookingClient Object
        //BookingClient bk = new BookingClient();

        // For each entry in Map

        // Start a new thread and assign it to that box office (via BXID)

        // Add the thread to a list

        // Return the list of threads
        return threadList;
    }

    public static void main(String[] args) {
        // TODO: Initialize test data to description
        // Plan:
        // 1) Make the bookings work with only one thread (box office)
        // 2) Add multithreading

        Theater.Ticket test = new Theater.Ticket("TEST_SHOW", "000", new Theater.Seat(28, 1), 999); // is A0 a valid seat? is it rowNum 0, seatNum 0?

        System.out.println(test.toString());
    }
}
