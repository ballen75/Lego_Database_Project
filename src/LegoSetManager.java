import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LegoSetManager {

    private ArrayList<LegoSet> legoSets;

    public LegoSetManager() {
        legoSets = new ArrayList<>();
    }

    public boolean addSet(LegoSet set) {
        if (findSet(set.getLegoSetID()) != null) {
            return false;    // duplicate ID
        }
        legoSets.add(set);
        return true;
    }

    public boolean deleteSet(String legoSetID) {
        LegoSet toRemove = findSet(legoSetID);
        if (toRemove == null) {
            return false;
        }
        legoSets.remove(toRemove);
        return true;
    }

    public ArrayList<LegoSet> listSets() {
        return legoSets;
    }

    public LegoSet findSet(String legoSetID) {
        for (LegoSet s : legoSets) {
            if (s.getLegoSetID().equalsIgnoreCase(legoSetID)) {
                return s;
            }
        }
        return null;
    }

    // Only allow numeric fields to be updated: pieceCount, price, releaseYear, recommendedAge
    public boolean updateAttribute(String legoSetID, String attribute, String value) {
        LegoSet set = findSet(legoSetID);
        if (set == null) {
            return false;
        }

        attribute = attribute.toLowerCase();

        try {
            switch (attribute) {
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
                    // setName and legoSetID are NOT updatable through this method
                    return false;
            }
        } catch (NumberFormatException e) {
            return false; // invalid numeric value
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
