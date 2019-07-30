/* MULTITHREADING <Theater.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Eric Graves
 * edg732
 * Slip days used: <0>
 * Summer 2019
 */
package assignment6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Theater {

    // Created class variables
    public int rowsInside; // Number of rows in a theater
    public int seatsInRow; // Number of seats in a row
    public String nowShowing; // Name of the movie

    // Create a map to hold all the seats in the Theater, if they have been sold, BXID, and CustID
    public static Map<String, String> seatMap;  // <Seat.toString(), ""> if unsold
                                                // <Seat.toString(), "BXID-CustID"> if sold
    // Easier to populate the map with empty seats at initialization, because they are not stored in order. Search for best seat by iteration over the entire theater and looking for unassigned seats rather than using containsKey().

    /**
     * the delay time you will use when print tickets
     */
    private int printDelay = 50; // 50 ms.  Use it in your Thread.sleep()

    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    // ArrayList of each seat in the theater. Concurrently modified by each BoxOffice thread as tickets are purchased. When full, all seats have been sold.
    public ArrayList<Ticket> seatTickets = new ArrayList<>();

    /**
     * Represents a seat in the theater
     * A1, A2, A3, ... B1, B2, B3 ...
     */
    static class Seat {
        private int rowNum;
        private int seatNum;

        //TODO: give Seat information about owners?

        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        @Override
        public String toString() {
            String result = "";
            int tempRowNumber = rowNum + 1;
            do {
                tempRowNumber--;
                result = ((char) ('A' + tempRowNumber % 26)) + result;
                tempRowNumber = tempRowNumber / 26;
            } while (tempRowNumber > 0);
            result += seatNum;
            return result;
        }

        // Reurns the seat (from seatMap) at a certain position as a String
        public static String findSeatString(int row, int sn){

            Seat tmpSeat = new Seat(row, sn);
            String tmpSeatStr = tmpSeat.toString();

            return tmpSeatStr;
        }


        /**
         * Compare two theater seats.
         *
         * @param s1 first seat
         * @param s2 second seat
         * @return 0 iff s1 == s2
         *        -1 iff s1 < s2 (s1 worse than s2)
         *         1 iff s1 > s2 (s1 better than s2)
         */
        public static int compare(Seat s1, Seat s2){

            // Exactly equal
            if((s1.getRowNum() == s2.getRowNum()) && (s1.getSeatNum() == s2.getSeatNum())){
                return 0;
            }

            // Seats are not exactly equal: compare rows first
            if(s1.getRowNum() < s2.getRowNum()){
                return 1; // closer seats are better
            }else if(s1.getRowNum() > s2.getRowNum()){
                return -1; // further seats are worse
            }

            // Gets here if rows are equal, but seat numbers are different
            if(s1.getSeatNum() < s2.getSeatNum()){
                return 1;
            }else{ // s1 row == s2 row AND s2 seatNum < s1 seatNum
                return -1;
            }
        }
    }

    /**
     * Represents a ticket purchased by a client
     */
    static class Ticket {
        private String show;
        private String boxOfficeId;
        private Seat seat;
        private int client;
        public static final int ticketStringRowLength = 31;


        public Ticket(String show, String boxOfficeId, Seat seat, int client) {
            this.show = show;
            this.boxOfficeId = boxOfficeId;
            this.seat = seat;
            this.client = client;
        }

        public Seat getSeat() {
            return seat;
        }

        public String getShow() {
            return show;
        }

        public String getBoxOfficeId() {
            return boxOfficeId;
        }

        public int getClient() {
            return client;
        }

        @Override
        public String toString() {
            String result, dashLine, showLine, boxLine, seatLine, clientLine, eol;

            eol = System.getProperty("line.separator");

            dashLine = new String(new char[ticketStringRowLength]).replace('\0', '-');

            showLine = "| Show: " + show;
            for (int i = showLine.length(); i < ticketStringRowLength - 1; ++i) {
                showLine += " ";
            }
            showLine += "|";

            boxLine = "| Box Office ID: " + boxOfficeId;
            for (int i = boxLine.length(); i < ticketStringRowLength - 1; ++i) {
                boxLine += " ";
            }
            boxLine += "|";

            seatLine = "| Seat: " + seat.toString();
            for (int i = seatLine.length(); i < ticketStringRowLength - 1; ++i) {
                seatLine += " ";
            }
            seatLine += "|";

            clientLine = "| Client: " + client;
            for (int i = clientLine.length(); i < ticketStringRowLength - 1; ++i) {
                clientLine += " ";
            }
            clientLine += "|";

            result = dashLine + eol +
                    showLine + eol +
                    boxLine + eol +
                    seatLine + eol +
                    clientLine + eol +
                    dashLine;

            return result;
        }
    }

    public Theater(int numRows, int seatsPerRow, String show) {

        // Save the Theater size and movie at creation
        this.rowsInside = numRows;
        this.seatsInRow = seatsPerRow;
        this.nowShowing = show;

        if(BookingClient.DEBUG){
            System.out.println("Created Theater with rows:" + rowsInside + " seats:" + seatsInRow);
        }

        // TODO: Implement this constructor. Anything else?

        // Populate map of seats --> mark all as empty
        seatMap = new HashMap<String, String>(){
            {
                // For each row starting from the front
                for(int row = 0; row < rowsInside; row++) { // TODO: rows start at 0?

                    // For each seat starting from the left (0)
                    for (int sn = 0; sn < seatsInRow; sn++) {  // TODO: seats start at 0?

                        Seat s = new Seat(row, sn);
                        put(s.toString(), "UNASSIGNED"); // Dont initialize as empty because HashMap.get() returns null if no key exists
                        // Not seatMap.put() because it isnt instantiated yet --> just use put()

                        if(BookingClient.DEBUG){
                            System.out.println("Created Seat at: r:" + row + " c:" + sn);
                        }
                    }
                }
            }
        }; // Initialize via anonymous class

    }

    /**
     * Calculates the best seat not yet reserved. [O(N*M)]
     *
     * @return the best seat or null if theater is full
     */
    public Seat bestAvailableSeat() {

        // For each row starting from the front
        for(int row = 0; row < rowsInside; row++) { // TODO: rows start at 0?

            // For each seat starting from the left (0)
            for (int sn = 0; sn < seatsInRow; sn++) {  // TODO: seats start at 0?

                // TODO: instead of HashMap, use sorted array, such that seats in the array are ordered best to worst?

                // if the current seat is availible, return it
                Seat currentSeat = new Seat(row, sn);

                if (seatMap.get(currentSeat.toString().) { // TODO: is this the best structure?

                } else{

                    return new Seat(row, sn); // TODO: is it okay to create a duplicate Seat to use in searching?
                }
            }
        }

        return null;
    }

    /**
     * Prints a ticket for the client after they reserve a seat
     * Also prints the ticket to the console
     *
     * @param seat a particular seat in the theater
     * @return a ticket or null if a box office failed to reserve the seat
     */
    public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
        // TODO: Implement this method


        return null;
    }

    /**
     * Lists all tickets sold for this theater in order of purchase
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        // TODO: Implement this method
        return null;
    }
}
