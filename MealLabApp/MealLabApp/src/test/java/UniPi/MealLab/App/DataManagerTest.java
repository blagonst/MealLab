package UniPi.MealLab.App;

import org.junit.jupiter.api.Test;
import UniPi.MealLab.Model.Recipe;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataManagerTest {

    @Test
    void testSaveAndLoad() {
        Recipe r = new Recipe();
        r.setIdMeal("999");
        r.setStrMeal("Test Burger");
        
        List<Recipe> favs = new ArrayList<>();
        favs.add(r);
        
        List<Recipe> cooked = new ArrayList<>();

        DataManager manager = new DataManager();
        manager.save(favs, cooked);
        
        DataManager.UserData loaded = manager.load();
        
        assertNotNull(loaded);
        assertEquals(1, loaded.favorites.size());
        assertEquals("Test Burger", loaded.favorites.get(0).getStrMeal());
        
        new File("meallab_data.json").delete();
    }

    @Test
    void testLoadMissingFile() {
        // Διασφάλιση ότι το αρχείο δεν υπάρχει
        new File("meallab_data.json").delete();
        
        DataManager manager = new DataManager();
        DataManager.UserData data = manager.load();
        
        assertNotNull(data, "Should return empty object, not null");
        assertNotNull(data.favorites, "Favorites list should be initialized");
        assertNotNull(data.cooked, "Cooked list should be initialized");
        assertTrue(data.favorites.isEmpty());
    }

    @Test
    void testLoadCorruptedFile() throws java.io.IOException {
        // Δημιουργία αρχείου με garbage content
        File f = new File("meallab_data.json");
        try (java.io.FileWriter fw = new java.io.FileWriter(f)) {
            fw.write("THIS IS NOT JSON");
        }
        
        DataManager manager = new DataManager();
        DataManager.UserData data = manager.load();
        
        assertNotNull(data, "Should recover from corruption with empty object");
        assertTrue(data.favorites.isEmpty());
        
        f.delete();
    }
}
