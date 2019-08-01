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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.lang.Thread;

public class BookingClient {

    public static boolean DEBUG = true;
    public static boolean DEBUG_VERBOSE = false; // only if you want to see every possible debug message

    public static Theater theater;

    public static Map<String, Integer> boMap;

    /**
     * @param office  maps box office id to number of customers in line
     * @param th the theater where the show is playing
     */
    public BookingClient(Map<String, Integer> office, Theater th) {

        //TODO: what goes here? Does it need a private copy of the map to be used during simulate()?
        boMap = new HashMap<String, Integer>(office);

        theater = th;


    }

    /**
     * Starts the box office simulation by creating (and starting) threads
     * for each box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are box offices
     */
    public List<Thread> simulate(){
        // Create empty list of Threads
        List<Thread> threadList = new ArrayList<>();

        // For each entry in Map
        for(String bxID : boMap.keySet()) {
            // Start a new thread and assign it to that box office (via BXID)
            clientThread t = new clientThread(bxID, boMap.get(bxID), theater);

            // Add the thread to a list
            threadList.add(t);

            // Start the Thread
            t.start();

        }
        // Return the list of threads
        return threadList;
    }

    // Break points suspend on Thread (rather than on all)

    public static void main(String[] args) {
        // TODO: Initialize test data to description
        // Plan:
        // 1) Make the bookings work with only one thread (box office)
        // 2) Add multithreading


        // Make a Map of Box Offices and people in line
        Map<String, Integer> officeMap = new HashMap<String, Integer>(6){
            {
                put("BX0", 0); // Each name matches initial number of people in line
                put("BX1", 1);
                put("BX2", 2);
                put("BX3", 3);
                put("BX4", 4);
                put("BX5", 5); // 15 total clients
            }
        };

        // Make a Theater
        Theater th = new Theater(4,4,"SHOW_NAME"); // 16 seats

        // Create a new BookingClient Object
        BookingClient bClient = new BookingClient(officeMap, th);

        List<Thread> tList = bClient.simulate();

        if(DEBUG){
            //System.out.println(tList);
            //System.out.println(bClient.theater.theaterString());
        }


    }
}
