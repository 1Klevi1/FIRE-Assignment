import java.util.ArrayList;

/**
 * This class implements the FIRE interface
 *
 * @author Jack
 * @version 29/11/2022
 **/
public class Resort implements FIRE {
    private String resortName;
    private String resortLocation;
    private ArrayList<Island> islands;
    private ArrayList<Ferry> ferries;
    private ArrayList<Pass> passes;

    /**
     * constructor
     * Some code has been provided
     */
    public Resort(String name, String location) {
        // Store some details about the resort
        resortName = name;
        resortLocation = location;
        islands = new ArrayList<Island>();
        ferries = new ArrayList<Ferry>();
        passes = new ArrayList<Pass>();

        // Set up the islands, ferries and passes
        loadIslandsAndFerries();
        loadPasses();

        // Add all the passes to the base island
        Island base = islands.get(0);
        for (Pass temp : passes) {
            base.enter(temp);
        }
    }

    /**
     * Returns information about the resort including its location/name and all
     * passes currently on each island, or "No passes" (if no pass on that island
     *
     * @return all of the details of all islands including location
     * and all passes currently on each island, or "No passes" if island has no passes
     */
    public String toString() {
        return "********************\nResort Name: " +
                resortName + "\nResort Location: " +
                resortLocation + "\nIsland List: " +
                islands + "\n********************";
    }

    /**
     * Returns a String representation of all the passes on all islands
     * with "No passes" if there are no passes on an island
     *
     * @return a String representation of all passes on all islands
     **/
    public String getAllPassesOnAllIslands() {
        ArrayList<Pass> allPasses = new ArrayList<Pass>();
        islands.forEach(island -> allPasses.addAll(island.getPassList()));
        if (allPasses.size() == 0) {
            return "No passes";
        } else {
            String s = "";
            for (Pass pass : allPasses) {
                s += pass + "\n";
            }
            return s;
        }
    }

    /**
     * Returns the name of the island which contains the specified pass or "Not found"
     *
     * @param cd -the id of the pass
     * @return the name of the Island which contains the pass, or "Not found"
     **/
    public String findPassLocation(int cd) {
        for (Island island : islands) {
            if (island.isPassOnIsland(cd)) {
                return island.getIslandName();
            }
        }
        return null;
    }

    /**
     * Returns details of the pass with the specified id or "Not found"
     *
     * @param cd - the id of the pass
     * @return the details of the pass, or "Not found"
     **/
    public String viewAPass(int cd) {
        for (Island island : islands) {
            Pass temp = island.getPassDetails(cd);
            if (temp != null) {
                return temp.toString();
            }
        }
        return "Not found";
    }

    /**
     * Given the name of a island, returns the island id number
     * or -1 if island does not exist
     *
     * @param isl is the name of island
     * @return id number of island
     */
    public int getIslandNumber(String isl) {
        for (Island island : islands) {
            if (island.getIslandName().equals(isl)) {
                return island.getIslandID();
            }
        }
        return -1;
    }

    /**
     * Returns a String representation of all passes on a specified island
     *
     * @param isl - the name of the island
     * @return a String representation of all passes on specified island
     **/
    public String getAllPassesOnIsland(String isl) {
        int targetInd = getIslandNumber(isl);
        if (targetInd == -1) {
            return "";
        } else {
            String s = "";
            Island target = islands.get(targetInd);
            for (Pass pass : target.getPassList()) {
                s += pass + "\n";
            }
            return s;
        }
    }

    /**
     * Returns true if a Pass is allowed to journey using a ferry, false otherwise
     * A journey can be made if:
     * the rating of the pass >= the rating of the destination island
     * AND the destination island is not full
     * AND the pass has sufficient credits (a journey costs 3 credits)
     * AND the pass is currently in the source island
     * AND the pass id and ferry code represent objects in the system
     *
     * @param cdId    is the id of the pass requesting the move
     * @param ferCode is the code of the ferry journey by which the pass wants to move
     * @return true if the pass is allowed on the ferry journey, false otherwise
     **/
    public boolean canTravel(int cdId, String ferCode) {
        Ferry ferry = getFerry(ferCode);
        Pass pass = getPass(cdId);
        return ferry != null &&
                pass != null &&
                pass.getLuxuryRating() >= ferry.getDestinationIsland().getIslandRating() &&
                ferry.getDestinationIsland().canPassEnter() &&
                ferry.canPassTravel(pass) &&
                ferry.getSourceIsland().isPassOnIsland(pass);
    }

    /**
     * Returns the result of a pass requesting to journey by Ferry.
     * A journey will be successful if:
     * the luxury rating of the pass  >= the luxury rating of the destination island
     * AND the destination island is not full
     * AND the pass has sufficient credits
     * AND the pass is currently in the source island
     * AND both the pass id and the ferry code is on the system
     * If the ferry journey can be made, the pass is removed from the source island,
     * added to the destination island and a suitable message returned. Pass
     * information should be updated (A journey costs 3 credits and journey points incremented by 1)
     * If the ferry journey cannot be made, the state of the system remains unchanged
     * and a message specifying the reason is returned.
     *
     * @param pPassId is the id of the pass requesting the move
     * @param ferCode is the code of the ferry by which the pass wants to travel
     * @return a String giving the result of the request
     **/
    public String travel(int pPassId, String ferCode) {
        Pass pass = getPass(pPassId);
        Ferry ferry = getFerry(ferCode);
        if (pass != null && ferry != null) {
            return ferry.processPass(pass, ferry.getSourceIsland(), ferry.getDestinationIsland());
        }
        return "Invalid pass or ferry code";
    }

    /**
     * Allows credits to be added to a pass.
     *
     * @param id    the id of the pass toping up their credits
     * @param creds the number of credits to be added to pass
     */
    public void topUpCredits(int id, int creds) {
        Pass pass = getPass(id);
        if (pass != null) {
            pass.addCredits(creds);
        }
    }

    /**
     * Converts a pass's journey points into credits
     *
     * @param id the id of the pass whose points are to be converted
     */
    public void convertPoints(int id) {
        Pass pass = getPass(id);
        if (pass != null) {
            pass.journeyPointsToCredits();
        }
    }

    private void loadPasses() {
        // Create the passes and add them to the array list
        passes.add(new Pass(1000, "Lynn", 5, 10));
        passes.add(new Pass(1001, "May", 3, 30));
        passes.add(new Pass(1002, "Nils", 10, 0));
        passes.add(new Pass(1003, "Olek", 1, 12));
        passes.add(new Pass(1004, "Pan", 3, 3));
        passes.add(new Pass(1005, "Quin", 1, 30));
        passes.add(new Pass(1006, "Raj", 4, 5));
        passes.add(new Pass(1007, "Sol", 7, 20));
        passes.add(new Pass(1008, "Tel", 6, 30));
        passes.add(new TouristPass(3, "Bill", 10, 10, "Ukraine"));
        passes.add(new BusinessPass(11, "Anna", 10));
        passes.add(new EmployeePass(321, "Henry", 10, "Cleaner"));
    }

    private void loadIslandsAndFerries() {
        // Create the island
        Island base = new Island(0, "Base", 0, 100);
        Island yorkie = new Island(1, "Yorkie", 1, 100);
        Island bounty = new Island(2, "Bounty", 3, 10);
        Island twirl = new Island(3, "Twirl", 5, 2);
        Island aero = new Island(4, "Aero", 1, 1);

        // Add the islands to the array list
        islands.add(base);
        islands.add(yorkie);
        islands.add(bounty);
        islands.add(twirl);
        islands.add(aero);

        // Create the ferries and add them to the array list
        ferries.add(new Ferry("ABC1", base, yorkie));
        ferries.add(new Ferry("BCD2", yorkie, base));
        ferries.add(new Ferry("CDE3", yorkie, bounty));
        ferries.add(new Ferry("DEF4", bounty, yorkie));
        ferries.add(new Ferry("JKL8", bounty, twirl));
        ferries.add(new Ferry("EFG5", twirl, yorkie));
        ferries.add(new Ferry("GHJ6", yorkie, aero));
        ferries.add(new Ferry("HJK7", aero, yorkie));
    }

    /**
     * Returns the pass with the pass id specified by the parameter
     *
     * @param id pass id
     * @return the pass with the specified name
     **/
    public Pass getPass(int id) {
        for (Pass pass : passes) {
            if (pass.getPassIdNumber() == id) {
                return pass;
            }
        }
        return null;
    }

    /**
     * Returns the island with the name specified by the parameter
     *
     * @param islandName the island name
     * @return the island with the specified name
     **/
    private Island getIsland(String islandName) {
        for (Island island: islands) {
            if (island.getIslandName().equals(islandName)) {
                return island;
            }
        }
        return null;
    }

    /**
     * Returns the ferry with the ferry code specified by the parameter
     *
     * @param fer the ferry code
     * @return the island with the specified name
     **/
    private Ferry getFerry(String fer) {
        for (Ferry ferry : ferries) {
            if (ferry.getFerryCode().equals(fer)) {
                return ferry;
            }
        }
        return null;
    }
}
