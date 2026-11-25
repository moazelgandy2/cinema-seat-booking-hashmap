# Answers

Run on OpenJDK 17.

## Step 2: output before equals() and hashCode()

Each `new Seat(...)` is a different key (identity comparison), so lookups fail:

- `Who booked C3` prints `null`
- `Is A2 taken` prints `false`
- `Youssef -> B5` prints `booked`, and B5 is now in the map twice
- `Hana -> E1` prints `booked` even though E1 is broken
- `Seats booked` prints 7 instead of 4
- the chart shows no X and no #

## Q1. Why override hashCode() and equals()?

HashMap finds a key by hashCode() and then checks it with equals(). The defaults compare object identity, so a new `Seat('B', 5)` never matches the stored one. Overriding both makes two seats with the same row and number the same key.

## Q2. Delete hashCode(), keep equals()

In my run the lookups failed again. The default hashCode() does not guarantee the same hash for seats that are equal by row and number. This breaks the equals/hashCode contract, so HashMap lookups are unreliable. Two equal seats could happen to get the same default hash, but you cannot rely on it.

## Q3. Why are row and number final?

The entry is stored in a bucket chosen by the hash at put time. If the number changed afterwards, the hash would change, and get() and remove() could look in the wrong bucket and fail to find the booking. It would still be in the map but not reliably reachable. Final fields prevent this.

## Q4. equals(Seat o) instead of equals(Object o)

With `@Override`: compile error, because it overloads equals and does not override it.

Without `@Override`: it compiles, but HashMap calls equals(Object), which is still the default. The output is wrong again.

## Bonus: record Seat(char row, int number) {}

It works and the output is correct. A record generates equals(), hashCode() and toString() from its fields, and the fields are final. The only visible change is the map printing `Seat[row=A, number=1]=Ali` instead of `A1=Ali`, because of the generated toString(). Needs Java 16 or newer.
