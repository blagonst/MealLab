package UniPi.MealLab.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import UniPi.MealLab.Model.MealResponse;
import UniPi.MealLab.Model.Recipe;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

/**
 * Service class responsible for communicating with the external MealDB API.
 * Handles HTTP requests and JSON parsing.
 */
public class MealService implements IMealService {
    
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public MealService() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    /**
     * Helper method to execute a GET request to the API.
     * @param suffix The endpoint suffix (e.g., "search.php?s=pasta")
     * @return List of Recipe objects
     * @throws MealApiException if network error or parsing fails
     */
    private List<Recipe> makeRequest(String suffix) throws MealApiException {
        int maxRetries = 2;
        int attempt = 0;
        Exception lastException = null;

        while (attempt < maxRetries) {
            try {
                String url = BASE_URL + suffix;
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();
                
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() != 200) {
                    throw new MealApiException("API Error: HTTP " + response.statusCode());
                }

                MealResponse result = mapper.readValue(response.body(), MealResponse.class);
                return result.getMeals();

            } catch (MealApiException e) {
                throw e; 
            } catch (Exception e) {
                // Don't retry parsing errors
                if (e instanceof com.fasterxml.jackson.core.JsonProcessingException) {
                    throw new MealApiException("Invalid API Response Format", e);
                }
                // Retry on network/IO errors (IOException, InterruptedException)
                lastException = e;
                attempt++;
                if (attempt < maxRetries) {
                    try { 
                        Thread.sleep(2000); 
                    } catch (InterruptedException ie) { 
                        Thread.currentThread().interrupt();
                        throw new MealApiException("Request interrupted", ie);
                    }
                }
            }
        }
        throw new MealApiException("Failed after " + maxRetries + " attempts: " + lastException.getMessage(), lastException);
    }

    /**
     * Searches for recipes by ingredient or name.
     * @param query The search term (e.g., "chicken")
     * @return List of matching recipes
     */
    public List<Recipe> searchRecipes(String query) throws MealApiException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return makeRequest("search.php?s=" + encodedQuery);
    }

    /**
     * Filters recipes by main ingredient.
     * @param ingredient The ingredient to filter by (e.g., "chicken_breast")
     * @return List of matching recipes
     */
    public List<Recipe> getRecipesByIngredient(String ingredient) throws MealApiException {
        String encodedIngredient = URLEncoder.encode(ingredient, StandardCharsets.UTF_8);
        return makeRequest("filter.php?i=" + encodedIngredient);
    }
    
    /**
     * Retrieves full details for a specific recipe by ID.
     * @param id The recipe ID
     * @return The Recipe object or null if not found
     */
    public Recipe getRecipeById(String id) throws MealApiException {
        List<Recipe> recipes = makeRequest("lookup.php?i=" + id);
        if (recipes != null && !recipes.isEmpty()) {
            return recipes.get(0);
        }
        return null;
    }

    /**
     * Fetches a completely random recipe.
     * @return A random Recipe object
     */
    public Recipe getRandomRecipe() throws MealApiException {
        List<Recipe> recipes = makeRequest("random.php");
        if (recipes != null && !recipes.isEmpty()) {
            return recipes.get(0);
        }
        return null;
    }
}