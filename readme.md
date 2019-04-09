# Quick rundown

A single class (with entirely static methods) was written for this assignment, called KnightMovementCalculations.

The main method can be run so:

`
mvn compile && mvn exec:java -Dexec.mainClass="me.apmats.KnightMovementCalculations" -Dexec.args="A1 B1"
`

and the arguments should be a space separated tuple of board positions.
The main method calculates the shortest path within the predefined maximum of 3 moves and prints it out.

The main method exposed by this utility class is the findOptimalPath method, which can be used by callers to find an optimal path for a knight between any 2 positions and with any maximum number of moves for the path.

A few tests are included, which can be run so:

`
mvn test
`

Some remarks:

For clarity and because this is a toy example, I wanted to avoid having class hierarchies.
If our use case required this, we could instead have a class hierarchy of ChestPiece -> Knight where any piece specific behavior (such as the path-seeking behaviour we're developing here)
would be expressed as an instance method. This way, if we designed a program that for example needed to calculate the path for any given set piece, it could operate on any instance of 
the ChestPiece base class.
But for any real chess-playing program I don't see the value of seeking out the shortest path between piece positions.