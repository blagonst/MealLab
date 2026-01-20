package UniPi.MealLab.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {
    @Test
    public void testBuilder() {
        Recipe r = new Recipe.Builder()
            .name("Pasta")
            .category("Main")
            .addIngredient("Noodles", "500g")
            .build();
        
        assertEquals("Pasta", r.getStrMeal());
        assertEquals("Main", r.getStrCategory());
        assertEquals("Noodles", r.getStrIngredient1());
        
        java.util.List<String> list = r.getIngredientsList();
        assertEquals(1, list.size());
        assertEquals("Noodles: 500g", list.get(0));
    }
}
