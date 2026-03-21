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
    void findSet() {
    }

    @org.junit.jupiter.api.Test
    void updateAttribute() {
    }

    @org.junit.jupiter.api.Test
    void countTotalPieces() {
    }

    @org.junit.jupiter.api.Test
    void loadSetsFromFile() {
    }
}