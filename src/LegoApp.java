/**
 * Brianna Allen
 * CEN 3024 - Software Development 1
 * March 9, 2026
 * LegoApp.java
 * This application will allow users to add,remove,update,list and display the total lego piece count.
 */
import java.util.Scanner;

public class LegoApp {

    // String ID, digits only, any length
    public static String setIDValidation(Scanner scanner) {
        String setID;

        while (true) {
            System.out.print("Please enter the Lego Set ID (digits only): ");
            setID = scanner.nextLine().trim();

            if (setID.matches("\\d+")) {
                return setID;
            }

            System.out.println("Invalid Set ID. It must contain digits only.");
        }
    }

    // helper to add a Lego set manually
    private static void addLegoSetManually(Scanner scanner, LegoSetManager manager) {
        String id;

        while (true) {
            id = setIDValidation(scanner);

            if (manager.findSet(id) != null) {
                System.out.println("Duplicate ID. Please enter a different Lego Set ID.");
            } else {
                break;
            }
        }

        System.out.print("Please enter the Lego Set Name: ");
        String name = scanner.nextLine();

        int pieces;
        while (true) {
            System.out.print("Please enter the Piece Count (0 - 10000): ");
            try {
                pieces = Integer.parseInt(scanner.nextLine().trim());
                if (pieces >= 0 && pieces <= 10000) break;
                System.out.println("Error: must be between 0 and 10000.");
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid number. Try again.");
            }
        }

        double price;
        while (true) {
            System.out.print("Please enter the Price (0 - 1000): ");
            try {
                price = Double.parseDouble(scanner.nextLine().trim());
                if (price >= 0 && price <= 1000) break;
                System.out.println("Error: must be between 0 and 1000.");
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid number. Try again.");
            }
        }

        int year;
        while (true) {
            System.out.print("Please enter the Release Year (1950 - 2026): ");
            try {
                year = Integer.parseInt(scanner.nextLine().trim());
                if (year >= 1950 && year <= 2026) break;
                System.out.println("Error: year out of range.");
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid year. Try again.");
            }
        }

        int age;
        while (true) {
            System.out.print("Please enter the Recommended Age (1 - 99): ");
            try {
                age = Integer.parseInt(scanner.nextLine().trim());
                if (age >= 1 && age <= 99) break;
                System.out.println("Error: age out of range.");
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid age. Try again.");
            }
        }

        LegoSet set = new LegoSet(id, name, pieces, price, year, age);
        if (manager.addSet(set)) {
            System.out.println("Lego Set added successfully: " + set);
        } else {
            System.out.println("A set with that ID already exists.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LegoSetManager manager = new LegoSetManager();

        int choice = 0;

        while (choice != 7) {
            System.out.println("\n Lego Set Manager ");
            System.out.println("1. Add Lego Set Manually");
            System.out.println("2. Add Lego Sets from text file");
            System.out.println("3. Remove Lego Set");
            System.out.println("4. List Lego Sets");
            System.out.println("5. Show Total Pieces");
            System.out.println("6. Update Lego Set");
            System.out.println("7. Quit");
            System.out.print("Enter choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a number between 1 and 7.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline



                    switch (choice) {
                        case 1:
                            addLegoSetManually(scanner, manager);
                            break;
                        case 2:
                            System.out.println("Please enter the filepath to the Lego sets file:");
                            String path = scanner.nextLine();
                            int loaded = manager.loadSetsFromFile(path);
                            System.out.println("Lego sets loaded: " + loaded);
                            break;



                case 3:
                    System.out.print("Please enter the Lego Set ID to remove: ");
                    String removeID = scanner.nextLine().trim();
                    boolean removed = manager.deleteSet(removeID);
                    if (removed) {
                        System.out.println("Lego Set removed successfully: " + removeID);
                    } else {
                        System.out.println("Lego Set ID does not exist.");
                    }
                    break;

                case 4:
                    System.out.println("Displaying all Lego Sets:");
                    if (manager.listSets().isEmpty()) {
                        System.out.println("No Lego Sets in the system.");
                    } else {
                        for (LegoSet s : manager.listSets()) {
                            System.out.println(s);
                        }
                    }
                    break;

                case 5:
                    System.out.println("Total pieces across all sets: " +
                            manager.countTotalPieces());
                    break;

                case 6:
                    System.out.print("Enter the Lego Set ID to update: ");
                    String updateID = scanner.nextLine().trim();

                    LegoSet existing = manager.findSet(updateID);
                    if (existing == null) {
                        System.out.println("No Lego Set found with that ID.");
                        break;
                    }

                    System.out.println("Current data: " + existing);
                    System.out.println("You can update one of these attributes:");
                    System.out.println("setID, Name, pieceCount, price, releaseYear, recommendedAge");
                    System.out.print("Enter attribute name: ");
                    String attribute = scanner.nextLine().trim();

                    System.out.print("Enter new value for " + attribute + ": ");
                    String newValue = scanner.nextLine().trim();

                    boolean updated = manager.updateAttribute(updateID, attribute, newValue);
                    if (updated) {
                        System.out.println("Lego Set updated: " + manager.findSet(updateID));
                    } else {
                        System.out.println("Update failed. Check attribute name and value.");
                    }
                    break;

                case 7:
                    System.out.println("Quitting the program.");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1–7.");
            }
        }

        scanner.close();
    }
}
