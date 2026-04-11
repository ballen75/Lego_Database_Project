import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LegoSetManagerTest class contains unit tests for the LegoSetManager.
 * Verifies correct functionality for adding, deleting, listing, updating, and loading data from files.
 */
class LegoSetManagerTest {


    /**
     * Sample legoset object used across test cases
     */
    LegoSet legoset;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        legoset = new LegoSet("75419", "Death Star", 9023, 999.99, 2026, 18);
    }

    /**
     * Tests that a Lego set can be successfully added to the manager.
     *
     */

    @org.junit.jupiter.api.Test
    @DisplayName("Add lego set to database")
    void addSet() {
        LegoSetManager manager = new LegoSetManager();
        boolean result1 = manager.addSet(legoset);
        assertTrue(result1);
        assertEquals(1, manager.listSets().size());
    }

    /**
     * Tests that duplicate set IDs are not allowed.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Verify duplicate ID can not be saved to the database")
    void addDuplicateSet() {
        LegoSetManager manager = new LegoSetManager();
        boolean result1 = manager.addSet(legoset);
        LegoSet duplicateSet = new LegoSet("75419", "Death Star", 9023, 999.99, 2026, 18);
        boolean result2 = manager.addSet(duplicateSet);

        assertFalse(result2);
        assertEquals(1, manager.listSets().size());


    }

    /**
     * Tests that a Lego set can be removed using its ID
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Remove set from database using setID")
    void deleteSet() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        boolean result = manager.deleteSet("75419");

        assertTrue(result);
        assertEquals(0, manager.listSets().size());

    }

    /**
     * Tests deletion with a non-existent set ID
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Delete set - reject non-existent setID")
    void deleteSetNoSetID() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        boolean result2 = manager.deleteSet("123456");
        assertFalse(result2);
        assertEquals(1, manager.listSets().size());


    }

    /**
     * Tests listing sets when no data has been added.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("List set - before any data is loaded")
    void listSetsEmpty() {
        LegoSetManager manager = new LegoSetManager();
        assertTrue(manager.listSets().isEmpty());
        assertEquals(0, manager.listSets().size());
    }

    /**
     * Tests that stored Lego Sets are returned correctly.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("List Sets - Verify data is saved and displayed")
    void listSets() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        assertEquals(1, manager.listSets().size());
    }

    /**
     * Tests that a Lego Set can be found using its ID.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Find Set - Set found using setID")
    void findSet() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        assertEquals(legoset, manager.findSet("75419"));
    }

    /**
     * Tests searching for a non-existing Lego set ID.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Set can not be found because setID does not exist")
    void findSetNoSetID() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        LegoSet result = manager.findSet("123456");
        assertNull(result, " Expected no Lego Set to be found for ID 123456");
    }

    /**
     * Test updating a Lego set with a valid piece count.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Update pieceCount with valid value")
    void updateAttribute() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);

        boolean updated = manager.updateAttribute(
                "75419",
                "pieceCount",
                "9500"
        );
        LegoSet updatedSet = manager.findSet("75419");
        assertTrue(updated);
        assertEquals(9500, updatedSet.getPieceCount());
    }

    /**
     * Tests updating a lego set with an invalid piece count input.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Reject invalid pieceCount input ( non-numeric)")
    void updateAttributedatavalidation() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);

        boolean updated = manager.updateAttribute(
                "75419",
                "pieceCount",
                "9$OO"
        );
        LegoSet updatedSet = manager.findSet("75419");
        assertFalse(updated);
        assertEquals(9023, updatedSet.getPieceCount());
    }

    /**
     * Tests the total piece count calculation.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Count total pieces - expect piece count to match 9023")
    void countTotalPieces() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        int result = manager.countTotalPieces();
        assertEquals(9023, result);
    }

    /**
     * Tests total piece count with no data added.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Expect pieces count to be zero per no sets have been added")
    void countTotalPiecesNoSets() {
        LegoSetManager manager = new LegoSetManager();
        int totalPieces = manager.countTotalPieces();
        assertEquals(0, totalPieces);
    }

    /**
     * Tests loading Lego sets from a valid file.
     */
    @org.junit.jupiter.api.Test
    void loadSetsFromFile() {
        LegoSetManager manager = new LegoSetManager();
        String filepath = "src/lego_sets";
        int loadedCount = manager.loadSetsFromFile(filepath);

        LegoSet firstSet = manager.findSet("77056");
        assertNotNull(firstSet, "First Lego set should exist after loading");
        assertEquals("Blathers's Museum Collection", firstSet.getSetName());

        LegoSet secondSet = manager.findSet("77093");
        assertNotNull(secondSet, "Second Lego set should exist after loading");
        assertEquals("Ocarina of Time The Final Battle", secondSet.getSetName());

    }


    /**
     * Tests loading Lego sets from an invalid file path.
     */
    @org.junit.jupiter.api.Test
    void loadSetsFromFileIncorrectPath() {
        LegoSetManager manager = new LegoSetManager();
        String filepath = "src/thispathisincorrect";
        int loadedCount = manager.loadSetsFromFile(filepath);

       assertEquals(0, loadedCount, "No sets should be loaded for an inccorect file path");

       assertTrue(manager.listSets().isEmpty(), "Lego set should remain empty");

       LegoSet firstSet = manager.findSet("77056");
       assertNull(firstSet, "No data should be found for invalid filepath");
    }


}