/* MULTITHREADING <Theater.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual BXID.
 * Eric Graves
 * edg732
 * Slip days used: <0>
 * Summer 2019
 */
package assignment6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Theater {

    // Created class variables
    public int rowsInside; // Number of rows in a theater
    public int seatsInRow; // Number of seats in a row
    public String nowShowing; // Name of the movie

    // Create a map to hold all the seats in the Theater, if they have been sold, BXID, and CustID
    public static ArrayList<Seat> seatMap; //TODO: should this be final?


    // Create a FIFO data structure to use as a log of transactions
    // To have multiple threads change the structure of this list concurrently, it must be synchronized on a wrapper object or, wrapped in Collections.synchronizedList
    public static List<Seat> salesLog;
    public static List<Seat> syncLog; // Individual threads add sold seats to this shared list

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

    public int getSeatsInRow() {
        return seatsInRow;
    }

    // ArrayList of each seat in the theater. Concurrently modified by each BoxOffice thread as tickets are purchased. When full, all seats have been sold.
    public ArrayList<Ticket> seatTickets = new ArrayList<>(); // TODO : didnt see this at first???

    /**
     * Represents a seat in the theater
     * A1, A2, A3, ... B1, B2, B3 ...
     */
    static class Seat {
        private int rowNum;
        private int seatNum;

        // Box office that sold the ticket for this seat
        private String BXID; // "BXID" if sold,  "UNASSIGNED" if unsold
        // Customer ID that purchased this seat
        private int CID; // int CID if sold,  -1 if unsold


        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
            this.BXID = "";
            this.CID = -1;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        public String getBXID() {
            return BXID;
        }

        public int getCID() {
            return CID;
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


        /**
         * Compare two theater seats.
         *
         * @param s1 first seat
         * @param s2 second seat
         * @return 0 iff s1 == s2
         * -1 iff s1 < s2 (s1 worse than s2)
         * 1 iff s1 > s2 (s1 better than s2)
         */
        public static int compare(Seat s1, Seat s2) {

            // Exactly equal
            if ((s1.getRowNum() == s2.getRowNum()) && (s1.getSeatNum() == s2.getSeatNum())) {
                return 0;
            }

            // Seats are not exactly equal: compare rows first
            if (s1.getRowNum() < s2.getRowNum()) {
                return 1; // closer seats are better
            } else if (s1.getRowNum() > s2.getRowNum()) {
                return -1; // further seats are worse
            }

            // Gets here if rows are equal, but seat numbers are different
            if (s1.getSeatNum() < s2.getSeatNum()) {
                return 1;
            } else { // s1 row == s2 row AND s2 seatNum < s1 seatNum
                return -1;
            }
        }

        /**
         * Set Box Office ID for this seat.
         *
         * @param dataString: BXID
         */
        public void setBXID(String dataString) {

            this.BXID = dataString;
        }

        /**
         * Set Customer ID for this seat
         *
         * @param data: CID as int
         */
        public void setCID(int data) {

            this.CID = data;
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

    /**
     * Theater Constructor
     *
     * @param numRows: Number of rows in the Theater (int)
     * @param seatsPerRow: Number of seats in each row (int)
     * @param show: Name of the movie (String)
     */
    public Theater(int numRows, int seatsPerRow, String show) {

        // Save the Theater size and movie at creation
        this.rowsInside = numRows;
        this.seatsInRow = seatsPerRow;
        this.nowShowing = show;

        if (BookingClient.DEBUG) {
            System.out.println("+================SETUP==================+");
            System.out.println("Created Theater with rows:" + rowsInside + " seats:" + seatsInRow);
        }

        // Set up salesLog to avoid nullpointer
        salesLog = new ArrayList<Seat>();

        // Set up logging in synchronized list
        syncLog = Collections.synchronizedList(salesLog);

        // Populate ArrayList of Seats at Theater Initialization
        //-- Seats will be in order from best to worst
        //-- All seats start as UNASSIGNED
        seatMap = new ArrayList<Seat>() {
            {
                // For each row starting from the front
                for (int row = 0; row < rowsInside; row++)

                    // For each seat starting from the left (0)
                    for (int sn = 0; sn < seatsInRow; sn++) {

                        // Create a new seat object that is unassigned
                        Seat currentSeat = new Seat(row, sn);
                        currentSeat.setBXID("UNASSIGNED");
                        currentSeat.setCID(-1);

                        // Add Seat to the seatMap
                        add(currentSeat);

                        if (BookingClient.DEBUG) {
                            System.out.println("-\tCreated Seat at: r:" + row + " c:" + sn);
                        }
                    }
            }
        };

        if (BookingClient.DEBUG) {
            System.out.println("+============END SETUP==================+\n");
        }

    }

    /**
     * Calculates the best seat not yet reserved. [O(N*M)]
     *
     * @return the best seat or null if theater is full
     */
    public Seat bestAvailableSeat() {

        // For each row starting from the front
        for (int row = 0; row < rowsInside; row++) { // TODO: rows start at 0?

            // For each seat starting from the left (0)
            for (int sn = 0; sn < seatsInRow; sn++) {  // TODO: seats start at 0?

                Seat currentSeat = seatMap.get(row * seatsInRow + sn); // check this math

                // if the current seat is available, return it
                if (currentSeat.getBXID().equals("UNASSIGNED")) { // TODO: is this the best structure? --> can we iterate through the seats in order?
                    if (BookingClient.DEBUG) {
                        System.out.println("-\tBest Seat r:" + row + " c:" + sn);
                    }

                    return currentSeat;
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

        // Search all seats in the Theater seat map
        for (Seat s : seatMap) {
            if (s.getSeatNum() == seat.getSeatNum() && s.getRowNum() == seat.getRowNum() && s.getBXID().equals(boxOfficeId) && s.getCID() == client) {

                // Make the Ticket
                Ticket tkt = new Ticket(nowShowing, s.getBXID(), s, s.getCID());

                // Print Ticket to console
                System.out.println(tkt.toString());

                // Small delay (using printDelay)
                //-- something with Thread.sleep(printDelay)
                try {
                    Thread.sleep(getPrintDelay());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Return the Ticket
                return tkt;
            }
        }

        // Return null if there is no ticket from this BX
        return null;
    }

    /**
     * Lists all tickets sold for this theater in order of purchase
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {

        // Make an empty list for tickets
        List<Ticket> ticketLog = new LinkedList<>();

        // for each sold seat
        for (Seat s : syncLog) {

//            if(BookingClient.DEBUG) {
//                System.out.println(s.toString());
//            }

            // Get the ticket from that seat
            Ticket tkt = new Ticket(nowShowing, s.getBXID(), s, s.getCID());

            // Add it to the list
            ticketLog.add(tkt);

        }

        return ticketLog;
    }

    /**
     * toString override for Theater objects for easier debugging
     */
    public static String theaterString(){

        String out = seatMap.stream().map(Object::toString).collect(Collectors.joining(","));

        return out;
    }
}
