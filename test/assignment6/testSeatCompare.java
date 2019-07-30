package assignment6;

import assignment6.Theater;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class testSeatCompare {

    @Test (timeout = 3000)
    public void testGreater(){
        Theater.Seat s1 = new Theater.Seat(1, 4);
        Theater.Seat s2 = new Theater.Seat(5, 6);

        assertEquals(1, Theater.Seat.compare(s1,s2)); // Better in both row and seatnumber


        Theater.Seat s3 = new Theater.Seat(1, 10);
        Theater.Seat s4 = new Theater.Seat(5, 6);

        assertEquals(1, Theater.Seat.compare(s3,s4)); // Better row, worse seat number


        Theater.Seat s5 = new Theater.Seat(5, 4);
        Theater.Seat s6 = new Theater.Seat(5, 6);

        assertEquals(1, Theater.Seat.compare(s5,s6)); // Same row
    }

    @Test (timeout = 3000)
    public void testEquals(){
        Theater.Seat s1 = new Theater.Seat(5, 6);
        Theater.Seat s2 = new Theater.Seat(5, 6);

        assertEquals(0, Theater.Seat.compare(s1,s2)); // Same seat

        Theater.Seat s3 = new Theater.Seat(5, 4);
        Theater.Seat s4 = new Theater.Seat(5, 6);

        assertEquals(1, Theater.Seat.compare(s3,s4)); // Same row, different seatnum

        Theater.Seat s5 = new Theater.Seat(2, 6);
        Theater.Seat s6 = new Theater.Seat(5, 6);

        assertEquals(1, Theater.Seat.compare(s5,s6)); // Same seatnum, different row


    }

    @Test (timeout = 3000)
    public void testLess(){
        Theater.Seat s1 = new Theater.Seat(1, 4);
        Theater.Seat s2 = new Theater.Seat(5, 6);

        assertEquals(-1, Theater.Seat.compare(s2,s1)); // Worse in both row and seatnumber


        Theater.Seat s3 = new Theater.Seat(1, 10);
        Theater.Seat s4 = new Theater.Seat(5, 6);

        assertEquals(-1, Theater.Seat.compare(s4,s3)); // Worse row, better seat number


        Theater.Seat s5 = new Theater.Seat(5, 4);
        Theater.Seat s6 = new Theater.Seat(5, 6);

        assertEquals(-1, Theater.Seat.compare(s6,s5)); // Same row
    }
}
