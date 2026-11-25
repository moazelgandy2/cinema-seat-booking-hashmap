import java.util.*;

/*
 TASK: Cinema Seat Booking - a HashMap with your own class as the key

 key   = Seat   (row + number, for example B5)
 value = String (name of the customer who booked it)
*/
class Task_CinemaBooking {

    static class Seat {
        final char row;     // 'A' to 'E'
        final int number;   // 1 to 6

        Seat(char row, int number) {
            this.row = row;
            this.number = number;
        }

        @Override
        public String toString() {
            return "" + row + number;
        }

        // TODO 1: equals() - same seat when row AND number are the same
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Seat)) return false;
            Seat other = (Seat) o;
            return row == other.row && number == other.number;
        }

        // TODO 2: hashCode() - built from the same fields as equals()
        @Override
        public int hashCode() {
            return 31 * row + number;
        }
    }

    public static void main(String[] args) {

        HashMap<Seat, String> bookings = new HashMap<>();
        HashSet<Seat> broken = new HashSet<>();

        // Part A
        bookings.put(new Seat('A', 1), "Ali");
        bookings.put(new Seat('A', 2), "Sara");
        bookings.put(new Seat('B', 5), "Omar");
        bookings.put(new Seat('C', 3), "Mona");
        System.out.println("Bookings:           " + bookings);

        // Part B
        System.out.println("Who booked C3:      " + bookings.get(new Seat('C', 3)));

        // Part C
        System.out.println("Is A2 taken:        " + bookings.containsKey(new Seat('A', 2)));
        System.out.println("Is E6 taken:        " + bookings.containsKey(new Seat('E', 6)));

        // Part D
        String existing = bookings.putIfAbsent(new Seat('B', 5), "Youssef");
        if (existing == null) {
            System.out.println("Youssef -> B5:      booked");
        } else {
            System.out.println("Youssef -> B5:      refused, taken by " + existing);
        }

        // Part E
        bookings.remove(new Seat('A', 1));
        bookings.put(new Seat('D', 2), "Ali");
        System.out.println("After Ali moved:    " + bookings);

        // Part F
        broken.add(new Seat('E', 1));
        broken.add(new Seat('E', 2));
        if (broken.contains(new Seat('E', 1))) {
            System.out.println("Hana -> E1:         refused, seat is broken");
        } else {
            bookings.put(new Seat('E', 1), "Hana");
            System.out.println("Hana -> E1:         booked");
        }

        // Part G
        System.out.println("Seats booked:       " + bookings.size());

        // GIVEN
        System.out.println();
        printChart(bookings, broken);
    }

    // GIVEN - do not change.  X = booked   # = broken   . = free
    static void printChart(HashMap<Seat, String> bookings, HashSet<Seat> broken) {
        System.out.println("     1  2  3  4  5  6");
        for (char row = 'A'; row <= 'E'; row++) {
            System.out.print(row + "  ");
            for (int number = 1; number <= 6; number++) {
                Seat seat = new Seat(row, number);
                if (bookings.containsKey(seat)) {
                    System.out.print("  X");
                } else if (broken.contains(seat)) {
                    System.out.print("  #");
                } else {
                    System.out.print("  .");
                }
            }
            System.out.println();
        }
    }
}

/*
 QUESTIONS AND ANSWERS

 STEP 2: What changed before equals() and hashCode() were added?
 Each new Seat object acted as a different key. In the tested run:
 - get(C3) returned null and containsKey(A2) returned false.
 - putIfAbsent(B5, "Youssef") booked a duplicate seat.
 - Hana was allowed to book broken seat E1.
 - The map contained 7 entries instead of 4, and the chart did not show
   the expected booked or broken seats.

 Q1. Why did we have to override hashCode() and equals()?
 HashMap uses hashCode() to find a bucket, then equals() to check keys in
 that bucket. The default methods compare object identity, so a new Seat
 with the same row and number would not match the stored key. Overriding
 both makes seats with matching fields work as the same key.

 Q2. Delete hashCode() but keep equals(). What happens, and why?
 In the tested run, lookups failed again. The default hashCode() does not
 guarantee the same hash for seats that are equal by row and number. This
 breaks the equals/hashCode contract, so lookups are unreliable. Equal
 seats might happen to get the same default hash, but that is not guaranteed.

 Q3. Why are row and number final? What could go wrong if a booked seat's
 number changed after it was put in the map?
 The entry is stored in a bucket chosen using the key's hash at insertion.
 Changing the number could change the hash, so get() or remove() might look
 in a different bucket and fail to find the booking. Final fields prevent
 the key from changing after insertion.

 Q4. What happens if equals(Object o) is changed to equals(Seat o)?
 With @Override, it does not compile because that method does not override
 equals(Object). Without @Override, it compiles, but HashMap still calls
 equals(Object), which remains the default identity comparison, so lookups
 are wrong again.

 BONUS. What happens if Seat is replaced with
        record Seat(char row, int number) {}
 It works and the output is correct because a record generates equals(),
 hashCode(), and toString() from its fields, and its fields are final. The
 map prints keys like Seat[row=A, number=1] instead of A1. Records need
 Java 16 or newer.
*/
