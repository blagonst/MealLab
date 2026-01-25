package UniPi.MealLab.App;

import com.fasterxml.jackson.databind.ObjectMapper;
import UniPi.MealLab.Model.Recipe;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Χειρίζεται την τοπική αποθήκευση δεδομένων για την εφαρμογή.
 * Αποθηκεύει και φορτώνει δεδομένα συγκεκριμένα για τον χρήστη (Αγαπημένες και Μαγειρεμένες συνταγές) 
 * σε ένα τοπικό αρχείο JSON χρησιμοποιώντας τη βιβλιοθήκη Jackson.
 */
public class DataManager {
    private static final String DATA_FILE = "meallab_data.json";
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Κλάση container για τα δεδομένα του χρήστη προς σειριοποίηση σε JSON.
     */
    public static class UserData {
        public List<Recipe> favorites = new ArrayList<>();
        public List<Recipe> cooked = new ArrayList<>();
    }

    /**
     * Αποθηκεύει τις τρέχουσες λίστες αγαπημένων και μαγειρεμένων στο τοπικό αρχείο JSON.
     * @param favorites Λίστα συνταγών που έχουν επισημανθεί ως αγαπημένες
     * @param cooked Λίστα συνταγών που έχουν επισημανθεί ως μαγειρεμένες
     */
    public void save(List<Recipe> favorites, List<Recipe> cooked) {
        try {
            UserData data = new UserData();
            data.favorites = favorites;
            data.cooked = cooked;
            // Σειριοποίηση του αντικειμένου UserData στο αρχείο
            mapper.writeValue(new File(DATA_FILE), data);
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }

    /**
     * Φορτώνει δεδομένα χρήστη από το τοπικό αρχείο JSON.
     * @return Αντικείμενο UserData που περιέχει τις φορτωμένες λίστες, ή ένα κενό αν το αρχείο δεν υπάρχει.
     */
    public UserData load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return new UserData();
        try { 
            // Αποσειριοποίηση αρχείου JSON πίσω σε αντικείμενα Java
            return mapper.readValue(file, UserData.class); 
        }
        catch (IOException e) { 
            return new UserData(); 
        }
    }
}
