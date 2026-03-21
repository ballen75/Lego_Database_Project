import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class LegoSetManagerTest {


    //create an object to be tested
    LegoSet legoset;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        legoset = new LegoSet("75419", "Death Star", 9023, 999.99, 2026, 18);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Add Lego Set Test")
    void addSet() {
        LegoSetManager manager = new LegoSetManager();
        boolean result1 = manager.addSet(legoset);
        assertTrue(result1);
        assertEquals(1, manager.listSets().size());
    }

        // This test will fail because the set is unable to add a duplicate setID
    @org.junit.jupiter.api.Test
    @DisplayName("Add Lego Set Test using Duplicate ID")
    void addDuplicateSet() {
        LegoSetManager manager = new LegoSetManager();
        boolean result1 = manager.addSet(legoset);
       LegoSet duplicateSet = new LegoSet("75419", "Death Star", 9023, 999.99, 2026, 18);
        boolean result2 = manager.addSet(duplicateSet);

        assertFalse(result2);  //Test will fail because duplicate ID is not added
        assertEquals(2, manager.listSets().size());


    }

    @org.junit.jupiter.api.Test
    @DisplayName("Delete Set using setID")
    void deleteSet() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        boolean result = manager.deleteSet("75419");

        assertTrue(result);
        assertEquals(0, manager.listSets().size());

    }
        //This test will fail because the legoSetID does not exist in the database to remove.
     @org.junit.jupiter.api.Test
     @DisplayName("Delete setID that does not exist in database")
     void deleteSetNoSetID() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        boolean result2 = manager.deleteSet("123456");
        assertTrue(result2);
        assertEquals(0, manager.listSets().size());



    }

    @org.junit.jupiter.api.Test
    @DisplayName("List Sets - Empty List")
    void listSetsEmpty() {
        LegoSetManager manager = new LegoSetManager();
        assertTrue(manager.listSets().isEmpty());
        assertEquals(0, manager.listSets().size());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("List Sets - List Set")
    void listSets() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        assertEquals(1, manager.listSets().size());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Add Set - Set found using setID")
    void findSet() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        assertEquals(legoset, manager.findSet("75419"));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Set can not be found because setID does not exist")
    void findSetNoSetID() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        LegoSet result = manager.findSet("123456");
        assertNull(result, " Expected no Lego Set to be found for ID 123456");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Update attribute of Lego Set using pieceCount")
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

    @org.junit.jupiter.api.Test
    @DisplayName("Count total pieces - expect piece count to match 9023")
    void countTotalPieces() {
        LegoSetManager manager = new LegoSetManager();
        manager.addSet(legoset);
        int result = manager.countTotalPieces();
        assertEquals(9023, result);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Expect pieces count to be zero per no sets have been added")
    void countTotalPiecesNoSets() {
        LegoSetManager manager = new LegoSetManager();
        int totalPieces = manager.countTotalPieces();
        assertEquals(0, totalPieces);
    }

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
        assertEquals("Buggy the Clown Figure", secondSet.getSetName());

    }
}