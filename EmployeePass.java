public class EmployeePass extends Pass {
    private int employeeNumber;
    private String jobDescription;
    private int journeyScore;

    public EmployeePass(int employeeNo, String name, int employeeNumber, String jobDesc){
        super(employeeNo, name, 10, 4);
        this.employeeNumber = employeeNumber;
        jobDescription = jobDesc;
        journeyScore = 0;
    }

    public void useFerry() {
            journeyScore += 1;
    }

    public String toString() {
        return "********************\nEmployee Name: " +
                getGuestName() + "\nPass ID Number: " +
                getPassIdNumber() + "\nEmployee Number: " +
                employeeNumber + "\nJob Description: " +
                jobDescription + "\nJourney Score: " +
                journeyScore + "\n********************";
    }
}
