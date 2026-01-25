package UniPi.MealLab.Model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void testGetIngredientsList() {
        Recipe r = new Recipe();
        r.setStrIngredient1("Chicken");
        r.setStrMeasure1("1kg");
        r.setStrIngredient2("Salt");
        r.setStrMeasure2("1 tsp");
        r.setStrIngredient3(""); // empty
        r.setStrMeasure3("");
        r.setStrIngredient4(null); // Null
        r.setStrMeasure4("1 cup"); // Η μέτρηση χωρίς συστατικό θα πρέπει να αγνοηθεί

        List<String> ingredients = r.getIngredientsList();
        
        assertEquals(2, ingredients.size());
        assertTrue(ingredients.contains("Chicken: 1kg"));
        assertTrue(ingredients.contains("Salt: 1 tsp"));
    }

    @Test
    void testGetIngredientsList_NoMeasures() {
        Recipe r = new Recipe();
        r.setStrIngredient1("Water");
        // Η μέτρηση 1 είναι null/empty από προεπιλογή

        List<String> ingredients = r.getIngredientsList();
        
        assertEquals(1, ingredients.size());
        assertTrue(ingredients.contains("Water")); // Δεν πρέπει να έχει άνω και κάτω τελεία στο τέλος
    }

    @Test
    void testEqualsAndHashCode() {
        Recipe r1 = new Recipe();
        r1.setIdMeal("123");
        
        Recipe r2 = new Recipe();
        r2.setIdMeal("123");
        
        Recipe r3 = new Recipe();
        r3.setIdMeal("456");

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
    
    @Test
    void testToString() {
        Recipe r = new Recipe();
        r.setStrMeal("Pizza");
        assertEquals("Pizza", r.toString());
    }
    
    @Test
    void testBuilder() {
        Recipe r = new Recipe.Builder()
                .id("100")
                .name("Soup")
                .category("Starter")
                .area("French")
                .instructions("Boil water")
                .thumb("img.jpg")
                .addIngredient("Carrot", "1")
                .build();
        
        assertEquals("100", r.getIdMeal());
        assertEquals("Soup", r.getStrMeal());
        assertEquals("Starter", r.getStrCategory());
        assertEquals("French", r.getStrArea());
        assertEquals("Boil water", r.getStrInstructions());
        assertEquals("img.jpg", r.getStrMealThumb());
        assertEquals("Carrot", r.getStrIngredient1());
        assertEquals("1", r.getStrMeasure1());
    }
}