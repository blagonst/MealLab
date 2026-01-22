package UniPi.MealLab.App;

import com.fasterxml.jackson.databind.ObjectMapper;
import UniPi.MealLab.Model.Recipe;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles local data persistence for the application.
 * Saves and loads user-specific data (Favorites and Cooked recipes) 
 * to a local JSON file using the Jackson library.
 */
public class DataManager {
    private static final String DATA_FILE = "meallab_data.json";
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Container class for user data to be serialized to JSON.
     */
    public static class UserData {
        public List<Recipe> favorites = new ArrayList<>();
        public List<Recipe> cooked = new ArrayList<>();
    }

    /**
     * Saves the current favorites and cooked lists to the local JSON file.
     * @param favorites List of recipes marked as favorites
     * @param cooked List of recipes marked as cooked
     */
    public void save(List<Recipe> favorites, List<Recipe> cooked) {
        try {
            UserData data = new UserData();
            data.favorites = favorites;
            data.cooked = cooked;
            // Serialize the UserData object to the file
            mapper.writeValue(new File(DATA_FILE), data);
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }

    /**
     * Loads user data from the local JSON file.
     * @return UserData object containing loaded lists, or an empty one if file doesn't exist.
     */
    public UserData load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return new UserData();
        try { 
            // Deserialize JSON file back into Java objects
            return mapper.readValue(file, UserData.class); 
        }
        catch (IOException e) { 
            return new UserData(); 
        }
    }
}
