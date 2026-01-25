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
 * Κλάση υπηρεσίας υπεύθυνη για την επικοινωνία με το εξωτερικό MealDB API.
 * Χειρίζεται αιτήματα HTTP και ανάλυση JSON.
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
     * Βοηθητική μέθοδος για την εκτέλεση αιτήματος GET στο API.
     * @param suffix Η κατάληξη του τελικού σημείου (π.χ., "search.php?s=chicken")
     * @return Λίστα αντικειμένων Recipe
     * @throws MealApiException αν αποτύχει το δίκτυο ή η ανάλυση
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
                //Retry on network/IO errors (IOException, InterruptedException)
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
     * Αναζητά συνταγές ανά συστατικό ή όνομα.
     * @param query Ο όρος αναζήτησης (π.χ. "chicken")
     * @return Λίστα συνταγών που ταιριάζουν
     */
    public List<Recipe> searchRecipes(String query) throws MealApiException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return makeRequest("search.php?s=" + encodedQuery);
    }

    /**
     * Φιλτράρει συνταγές ανά κύριο συστατικό.
     * @param ingredient Το συστατικό για φιλτράρισμα (π.χ. "chicken_breast")
     * @return Λίστα συνταγών που ταιριάζουν
     */
    public List<Recipe> getRecipesByIngredient(String ingredient) throws MealApiException {
        String encodedIngredient = URLEncoder.encode(ingredient, StandardCharsets.UTF_8);
        return makeRequest("filter.php?i=" + encodedIngredient);
    }
    
    /**
     * Ανακτά πλήρεις λεπτομέρειες για μια συγκεκριμένη συνταγή μέσω ID.
     * @param id Το ID της συνταγής
     * @return Το αντικείμενο Recipe ή null αν δεν βρεθεί
     */
    public Recipe getRecipeById(String id) throws MealApiException {
        List<Recipe> recipes = makeRequest("lookup.php?i=" + id);
        if (recipes != null && !recipes.isEmpty()) {
            return recipes.get(0);
        }
        return null;
    }

    /**
     * Ανακτά μια εντελώς τυχαία συνταγή.
     * @return Ένα τυχαίο αντικείμενο Recipe
     */
    public Recipe getRandomRecipe() throws MealApiException {
        List<Recipe> recipes = makeRequest("random.php");
        if (recipes != null && !recipes.isEmpty()) {
            return recipes.get(0);
        }
        return null;
    }
}