public class LegoSet {
    private String legoSetID;
    private String setName;
    private int pieceCount;
    private double price;
    private int releaseYear;
    private int recommendedAge;

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

    public LegoSet() { }

    public String getLegoSetID() {
        return legoSetID;
    }

    public void setLegoSetID(String legoSetID) {
        this.legoSetID = legoSetID;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public int getPieceCount() {
        return pieceCount;
    }

    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getRecommendedAge() {
        return recommendedAge;
    }

    public void setRecommendedAge(int recommendedAge) {
        this.recommendedAge = recommendedAge;
    }

    @Override
    public String toString() {
        return legoSetID + " - " + setName +
                " | pieces=" + pieceCount +
                " | price=" + price +
                " | year=" + releaseYear +
                " | age " + recommendedAge + "+";
    }
}
