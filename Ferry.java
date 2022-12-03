/**
 * A ferry provides a one-way journey between two islands. It
 * has a ferry code and information about both the source and
 * the destination island
 *
 * @author Klevi
 * @version 27/11/2022
 */
public class Ferry {
    private String ferryID;
    private Island sourceIsland;
    private Island destinationIsland;

    public Ferry(String ferryCode, Island startIsland, Island endIsland) {
        ferryID = ferryCode;
        sourceIsland = startIsland;
        destinationIsland = endIsland;
    }

    public String getFerryCode() {
        return ferryID;
    }

    public Island getSourceIsland() {
        return sourceIsland;
    }

    public Island getDestinationIsland() {
        return destinationIsland;
    }

    public boolean canPassTravel(Pass pass) {return pass.getCredits() >= 3;}

    public String processPass(Pass pass, Island island1, Island island2) {
        if (pass.getLuxuryRating() < island2.getIslandRating()) {
            return "The pass has a lower luxury rating than the destination island";
        } else if (!island2.canPassEnter()) {
            return "The destination island has reached capacity";
        } else if (!canPassTravel(pass)) {
            return "The pass does not have enough credits to travel";
        } else if (!island1.isPassOnIsland(pass)) {
            return "The person is not on the source island";
        } else {
            island1.leave(pass);
            island2.enter(pass);
            pass.useFerry();
            return "The person can travel to the next island";
        }
    }

    public String toString() {
        return "********************\nFerry ID Number: " +
                ferryID + "\nSource Island: " + "\n" +
                getSourceIsland() + "\n\nDestination Island: " + "\n" +
                getDestinationIsland() + "\n********************";
    }
}
