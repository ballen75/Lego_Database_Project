/**
 * LegoSet Class
 * This class represents a single Lego set and stores its details.
 * Each Lego set contains information such as ID, name, number of pieces,
 * price, release year, and recommended age.
 */
public class LegoSet {

    // Unique identifier for the Lego set
    private String legoSetID;

    // Name of the Lego set
    private String setName;

    // Total number of pieces included in the set
    private int pieceCount;

    // Price of the Lego set
    private double price;

    // Year the Lego set was released
    private int releaseYear;

    // Recommended minimum age for the set
    private int recommendedAge;

    /**
     * Parameterized constructor
     * Creates a LegoSet object with all attributes provided.
     *
     * @param legoSetID Unique ID of the Lego set
     * @param setName Name of the Lego set
     * @param pieceCount Number of pieces in the set
     * @param price Price of the set
     * @param releaseYear Year the set was released
     * @param recommendedAge Recommended age for the set
     */
    public LegoSet(String legoSetID, String setName,
                   int pieceCount, double price,
                   int releaseYear, int recommendedAge) {
        this.legoSetID = legoSetID;
        this.setName = setName;
        this.pieceCount = pieceCount;
        this.price = price;
        this.releaseYear = releaseYear;
        this.recommendedAge = recommendedAge;
    }

    /**
     * Default constructor
     * Allows creation of a LegoSet object without initial values.
     */
    public LegoSet() { }

    /**
     * Returns the Lego set ID.
     * @return legoSetID
     */
    public String getLegoSetID() {
        return legoSetID;
    }

    /**
     * Sets the Lego set ID.
     * @param legoSetID Unique identifier for the set
     */
    public void setLegoSetID(String legoSetID) {
        this.legoSetID = legoSetID;
    }

    /**
     * Returns the name of the Lego set.
     * @return setName
     */
    public String getSetName() {
        return setName;
    }

    /**
     * Sets the name of the Lego set.
     * @param setName Name of the set
     */
    public void setsetName(String setName) {
        this.setName = setName;
    }

    /**
     * Returns the number of pieces in the Lego set.
     * @return pieceCount
     */
    public int getPieceCount() {
        return pieceCount;
    }

    /**
     * Sets the number of pieces in the Lego set.
     * @param pieceCount Number of pieces
     */
    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }

    /**
     * Returns the price of the Lego set.
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the Lego set.
     * @param price Price of the set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the release year of the Lego set.
     * @return releaseYear
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets the release year of the Lego set.
     * @param releaseYear Year the set was released
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Returns the recommended age for the Lego set.
     * @return recommendedAge
     */
    public int getRecommendedAge() {
        return recommendedAge;
    }

    /**
     * Sets the recommended age for the Lego set.
     * @param recommendedAge Recommended age
     */
    public void setRecommendedAge(int recommendedAge) {
        this.recommendedAge = recommendedAge;
    }

    /**
     * Overrides the default toString() method.
     * Returns a formatted string containing the Lego set's information.
     *
     * @return String representation of the Lego set
     */
    @Override
    public String toString() {
        return legoSetID + " - " + setName +
                " | pieces=" + pieceCount +
                " | price=" + price +
                " | year=" + releaseYear +
                " | age " + recommendedAge + "+";
    }
}