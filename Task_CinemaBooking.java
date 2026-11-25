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

 STEP 2: What was wrong before equals() and hashCode()?
 Every new Seat('B', 5) was treated as a different key, even with the same
 row and number. So get(C3) gave null, containsKey(A2) gave false, Youssef
 could book B5, Hana could book the broken E1, and the map had 7 entries
 instead of 4.

 Q1. Why did we have to override hashCode() and equals()?
 HashMap uses hashCode() to pick the bucket and equals() to find the key
 inside it. By default both compare object identity (the memory address), so
 a new Seat with the same row and number never matches. Overriding them makes
 two seats with the same row and number the same key.

 Q2. Delete hashCode() but keep equals(). What happens, and why?
 Lookups fail again. Equal seats now have different default hash codes, so
 HashMap looks in the wrong bucket and never even calls equals(). Rule: if
 two objects are equal, they must have the same hashCode().

 Q3. Why are row and number final? What if the number changed after the seat
     was put in the map?
 The seat was stored in a bucket based on its hash at that time. If the
 number changed, the hash would change, and get() or remove() would look in
 another bucket and not find the booking. final stops this.

 Q4. What happens if equals(Object o) becomes equals(Seat o)?
 With @Override it does not compile, because it no longer overrides
 equals(Object). Without @Override it compiles, but HashMap still calls
 equals(Object), which is the default one, so the bugs come back.

 BONUS. What happens if Seat is replaced with: record Seat(char row, int number) {}
 It works and gives the right results. A record automatically makes
 equals(), hashCode() and toString() from its fields, and the fields are
 final. The only difference is the printing: Seat[row=A, number=1] instead
 of A1. Records need Java 16 or newer.
*/
