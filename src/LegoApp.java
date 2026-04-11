/**
 * Brianna Allen
 * CEN 3024 - Software Development 1
 * March 9, 2026
 * LegoApp.java
 * This application will allow users to add,remove,update,list and display the total lego piece count.
 */
import java.util.Scanner;

public class LegoApp {

    /**
     * Prompts the user to enter a valid Lego Set ID.
     * The ID must contain only digits.
     * @param scanner Scanner object used to read the users input.
     * @return valid Lego Set ID consiting only of digits.
     */
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

    /**
     * Prompts user to manually input details for a new Lego Set.
     * Adds to the manager if the ID is not a duplicate.
     * @param scanner Scanner object used to read user input.
     * @param manager LegoSetManager object used to manage Lego Sets.
     */
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

    /**
     * Main method that runs Lego Set Manager application.
     * Displays the menu and processes any user input.
     *
     * @param args command-line argument
     */
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

                            int subChoice;

                            do {
                                System.out.println("\nCurrent data: " + existing);
                                System.out.println("\n--- UPDATE MENU ---");
                                System.out.println("1. Name");
                                System.out.println("2. Set ID");
                                System.out.println("3. Piece Count");
                                System.out.println("4. Price");
                                System.out.println("5. Release Year");
                                System.out.println("6. Recommended Age");
                                System.out.println("7. Back");
                                System.out.print("Choose option: ");

                                while (!scanner.hasNextInt()) {
                                    System.out.println("Enter a number 1–7.");
                                    scanner.next();
                                }

                                subChoice = scanner.nextInt();
                                scanner.nextLine(); // consume newline

                                String newValue = "";
                                String attribute = "";

                                switch (subChoice) {

                                    case 1: // Name
                                        attribute = "name";
                                        System.out.print("Enter new name: ");
                                        newValue = scanner.nextLine().trim();
                                        break;

                                    case 2: // Set ID
                                        attribute = "setid";
                                        newValue = setIDValidation(scanner);

                                        if (manager.findSet(newValue) != null &&
                                                !newValue.equalsIgnoreCase(updateID)) {
                                            System.out.println("Duplicate ID. Update canceled.");
                                            continue;
                                        }
                                        break;

                                    case 3: // Piece Count
                                        attribute = "piececount";
                                        while (true) {
                                            System.out.print("Enter piece count (0–10000): ");
                                            try {
                                                int val = Integer.parseInt(scanner.nextLine());
                                                if (val >= 0 && val <= 10000) {
                                                    newValue = String.valueOf(val);
                                                    break;
                                                }
                                                System.out.println("Out of range.");
                                            } catch (NumberFormatException e) {
                                                System.out.println("Invalid number.");
                                            }
                                        }
                                        break;

                                    case 4: // Price
                                        attribute = "price";
                                        while (true) {
                                            System.out.print("Enter price (0–1000): ");
                                            try {
                                                double val = Double.parseDouble(scanner.nextLine());
                                                if (val >= 0 && val <= 1000) {
                                                    newValue = String.valueOf(val);
                                                    break;
                                                }
                                                System.out.println("Out of range.");
                                            } catch (NumberFormatException e) {
                                                System.out.println("Invalid number.");
                                            }
                                        }
                                        break;

                                    case 5: // Release Year
                                        attribute = "releaseyear";
                                        while (true) {
                                            System.out.print("Enter year (1950–2026): ");
                                            try {
                                                int val = Integer.parseInt(scanner.nextLine());
                                                if (val >= 1950 && val <= 2026) {
                                                    newValue = String.valueOf(val);
                                                    break;
                                                }
                                                System.out.println("Out of range.");
                                            } catch (NumberFormatException e) {
                                                System.out.println("Invalid year.");
                                            }
                                        }
                                        break;

                                    case 6: // Recommended Age
                                        attribute = "recommendedage";
                                        while (true) {
                                            System.out.print("Enter age (1–99): ");
                                            try {
                                                int val = Integer.parseInt(scanner.nextLine());
                                                if (val >= 1 && val <= 99) {
                                                    newValue = String.valueOf(val);
                                                    break;
                                                }
                                                System.out.println("Out of range.");
                                            } catch (NumberFormatException e) {
                                                System.out.println("Invalid age.");
                                            }
                                        }
                                        break;

                                    case 7:
                                        System.out.println("Returning to main menu...");
                                        continue;

                                    default:
                                        System.out.println("Invalid choice.");
                                        continue;
                                }

                                boolean updated = manager.updateAttribute(updateID, attribute, newValue);

                                if (updated) {
                                    System.out.println("Updated successfully!");
                                    existing = manager.findSet(updateID); // refresh object
                                } else {
                                    System.out.println("Update failed.");
                                }

                            } while (subChoice != 7);

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
