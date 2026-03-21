import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * LegoSetManager Class
 * This class manages a collection of LegoSet objects.
 * It provides methods to add, delete, search, update, list,
 * and load Lego sets from a file.
 */
public class LegoSetManager {

    private ArrayList<LegoSet> legoSets;
    /**
     * Default constructor
     * Initializes the ArrayList that will store Lego sets.
     */
    public LegoSetManager() {
        legoSets = new ArrayList<>();
    }
    /**
     * Adds a new Lego set to the collection.
     * Prevents duplicate Lego set IDs from being added.
     *
     * @param set the LegoSet object to add
     * @return true if the set was added successfully, false if the ID already exists
     */
    public boolean addSet(LegoSet set) {
        if (findSet(set.getLegoSetID()) != null) {
            return false;    // duplicate ID
        }
        legoSets.add(set);
        return true;
    }
    /**
     * Deletes a Lego set from the collection based on its ID.
     *
     * @param legoSetID the ID of the Lego set to delete
     * @return true if the set was found and removed, false otherwise
     */
    public boolean deleteSet(String legoSetID) {
        LegoSet toRemove = findSet(legoSetID);
        if (toRemove == null) {
            return false;
        }
        legoSets.remove(toRemove);
        return true;
    }
    /**
     * Returns the full list of Lego sets.
     *
     * @return an ArrayList containing all LegoSet objects
     */
    public ArrayList<LegoSet> listSets() {
        return legoSets;
    }
    /**
     * Searches for a Lego set by its ID.
     * The search ignores uppercase and lowercase differences.
     *
     * @param legoSetID the ID of the Lego set to search for
     * @return the matching LegoSet object if found, otherwise null
     */
    public LegoSet findSet(String legoSetID) {
        for (LegoSet s : legoSets) {
            if (s.getLegoSetID().equalsIgnoreCase(legoSetID)) {
                return s;
            }
        }
        return null;
    }
    /**
     * Updates an attribute of an existing Lego set.
     * This method allows the user to update the set ID, name,
     * piece count, price, release year, or recommended age.
     *
     * @param legoSetID the ID of the Lego set to update
     * @param attribute the attribute name to update
     * @param value the new value to assign to the attribute
     * @return true if the update was successful, false otherwise
     */

    public boolean updateAttribute(String legoSetID, String attribute, String value) {
        LegoSet set = findSet(legoSetID);
        if (set == null) {
            return false;
        }

        attribute = attribute.toLowerCase();

        try {
            switch (attribute) {

                case "setid":
                case "legosetid":
                    if (!value.matches("\\d+")) {
                        return false; // must be digits
                    }

                    if (findSet(value) != null) {
                        return false; // duplicate ID
                    }

                    set.setLegoSetID(value);
                    return true;

                case "name":
                    set.setsetName(value);
                    return true;

                case "piececount":
                    set.setPieceCount(Integer.parseInt(value));
                    return true;

                case "price":
                    set.setPrice(Double.parseDouble(value));
                    return true;

                case "releaseyear":
                    set.setReleaseYear(Integer.parseInt(value));
                    return true;

                case "recommendedage":
                    set.setRecommendedAge(Integer.parseInt(value));
                    return true;

                default:
                    return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int countTotalPieces() {
        int total = 0;
        for (LegoSet s : legoSets) {
            total += s.getPieceCount();
        }
        return total;
    }

    // loadSetsFromFile(filePath : String) : int
    // expects lines: ID-Name-Pieces-Price-Year-Age
    public int loadSetsFromFile(String filePath) {
        int loadedCount = 0;

        try (FileReader fr = new FileReader(filePath)) {
            int c;
            StringBuilder currentLine = new StringBuilder();

            while ((c = fr.read()) != -1) {
                char ch = (char) c;
                if (ch == '\n' || ch == '\r') {
                    if (currentLine.length() > 0) {
                        LegoSet set = parseSet(currentLine.toString());
                        if (set != null && addSet(set)) {
                            loadedCount++;
                        }
                        currentLine.setLength(0);
                    }
                } else {
                    currentLine.append(ch);
                }

            }

            // last line if no newline at end
            if (currentLine.length() > 0) {
                LegoSet set = parseSet(currentLine.toString());
                if (set != null && addSet(set)) {
                    loadedCount++;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }

        return loadedCount;
    }

    // parseSet(line : String) : LegoSet
    // expects: ID-Name-Pieces-Price-Year-Age
    public LegoSet parseSet(String line) {
        String[] parts = line.split("-");
        if (parts.length != 6) {
            System.out.println("Skipping invalid line: " + line);
            return null;
        }

        try {
            String id = parts[0].trim();
            String name = parts[1].trim();
            int pieces = Integer.parseInt(parts[2].trim());
            double price = Double.parseDouble(parts[3].trim());
            int year = Integer.parseInt(parts[4].trim());
            int age = Integer.parseInt(parts[5].trim());

            return new LegoSet(id, name, pieces, price, year, age);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric value in line, skipping: " + line);
            return null;
        }
    }
}
