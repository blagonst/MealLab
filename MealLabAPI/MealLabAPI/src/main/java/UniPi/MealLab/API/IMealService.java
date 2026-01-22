package UniPi.MealLab.API;

import java.util.List;
import UniPi.MealLab.Model.Recipe;

public interface IMealService {
    List<Recipe> searchRecipes(String query) throws MealApiException;
    List<Recipe> getRecipesByIngredient(String ingredient) throws MealApiException;
    Recipe getRecipeById(String id) throws MealApiException;
    Recipe getRandomRecipe() throws MealApiException;
}
