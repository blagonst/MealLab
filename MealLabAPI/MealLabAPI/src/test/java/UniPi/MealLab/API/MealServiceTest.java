package UniPi.MealLab.API;

import org.junit.jupiter.api.Test;
import UniPi.MealLab.Model.Recipe;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MealServiceTest {

    @Test
    public void testSearchWorks() {
        try {
            MealService service = new MealService();
            List<Recipe> recipes = service.searchRecipes("chicken");
            
            assertNotNull(recipes, "List should not be null");
            assertFalse(recipes.isEmpty(), "List should not be empty");
            
        } catch (MealApiException e) {
            fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testRandomRecipe() {
        try {
            MealService service = new MealService();
            Recipe r = service.getRandomRecipe();
            assertNotNull(r, "Random recipe should not be null");
        } catch (MealApiException e) {
            fail("Random failed: " + e.getMessage());
        }
    }

    @Test
    public void testFilterByIngredient() {
        try {
            MealService service = new MealService();
            List<Recipe> recipes = service.getRecipesByIngredient("chicken_breast");
            assertNotNull(recipes, "List should not be null");
            assertFalse(recipes.isEmpty(), "List should not be empty");
        } catch (MealApiException e) {
            fail("Filter by ingredient failed: " + e.getMessage());
        }
    }

    @Test
    public void testSearchWithSpecialCharacters() {
        try {
            MealService service = new MealService();
            // Αναζήτηση για κάτι με κενά και εμπορικό και (&)
            List<Recipe> recipes = service.searchRecipes("Cream Cheese");
            assertNotNull(recipes, "Should handle spaces correctly");
            
            // Απλά επαληθεύουμε ότι δεν πετάει εξαίρεση για ειδικούς χαρακτήρες
            // Το "Sweet & Sour" είναι ένας κοινός όρος
            service.searchRecipes("Sweet & Sour");
        } catch (MealApiException e) {
            fail("Search with special characters failed: " + e.getMessage());
        }
    }

    @Test
    public void testGetRecipeById() {
        try {
            MealService service = new MealService();
            // Το 52772 είναι "Teriyaki Chicken Casserole" - ένα γνωστό ID στο TheMealDB
            Recipe r = service.getRecipeById("52772");
            assertNotNull(r, "Should return a recipe for valid ID");
            assertEquals("52772", r.getIdMeal(), "ID should match");
            
            Recipe missing = service.getRecipeById("0000000");
            assertNull(missing, "Should return null for invalid ID");
        } catch (MealApiException e) {
            fail("Get by ID failed: " + e.getMessage());
        }
    }
}