package assignment6;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class testSeatMap {

    // Initailize the seatmap (by creating a Theater), and make sure that the seats are all present and contain "UNMAPPED"
    @Test (timeout = 3000)
    public void testInitialize(){

        // Create a Theater of 12 seats
        Theater thea = new Theater(3, 4, "TEST_NAME");
        assertEquals(12, Theater.seatMap.size());

        for(Theater.Seat s : Theater.seatMap){
             //All seats should be unassigned
            assertEquals("UNASSIGNED", s.getBXID());
        }


    }

    // Add seats in order until the seatMap is full
    @Test (timeout = 3000)
    public void testFindBest(){

        // Create a Theater of 12 seats
        Theater thea = new Theater(3, 4, "TEST_NAME");

        // In a loop, call bestAvailableSeat() and assign the seat
        for(int i = 0; i < 12; i++ ){

            //Theater.Seat bestAvailable = Theater.bestAvailableSeat();
        }


    }

    // Test that a full theater behaves correctly
    @Test (timeout = 3000)
    public void testFull(){

    }
}
