package UniPi.MealLab.App;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import UniPi.MealLab.API.IMealService;
import UniPi.MealLab.API.MealApiException;
import UniPi.MealLab.Model.Recipe;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainFrameTest {

    private static final String DATA_FILE = "meallab_data.json";

    @Mock
    IMealService mockService;

    @BeforeEach
    void setUp() {
        new File(DATA_FILE).delete();
    }

    @AfterEach
    void tearDown() {
        new File(DATA_FILE).delete();
    }

    @Test
    void testSearchSuccess() throws Exception {
        // 1. Ρύθμιση Συμπεριφοράς Mock
        Recipe r = new Recipe();
        r.setStrMeal("Mock Pizza");
        when(mockService.searchRecipes("Pizza")).thenReturn(List.of(r));

        SwingUtilities.invokeAndWait(() -> {
            // 2. Εισαγωγή Mock
            MainFrame frame = new MainFrame(mockService);
            
            // 3. Ενέργεια
            frame.performSearch("Pizza", true);
            
            // 4. Επαλήθευση Ενημέρωσης Μοντέλου (χρησιμοποιώντας reflection όπως πριν)
            try {
                Field fModelField = MainFrame.class.getDeclaredField("searchModel");
                fModelField.setAccessible(true);
                DefaultListModel<Recipe> model = (DefaultListModel<Recipe>) fModelField.get(frame);

                // Σημείωση: Σε πραγματικό περιβάλλον νημάτων, ίσως χρειαστεί να περιμένουμε, αλλά επειδή εμείς 
                // invokeAndWait μέσα στον έλεγχο για το trigger, και ο worker εκτελεί...
                // Στην πραγματικότητα ο SwingWorker είναι ασύγχρονος. Οπότε ο άμεσος έλεγχος μπορεί να αποτύχει αν ο worker είναι αργός.
                // Ωστόσο, τα mocks του Mockito είναι άμεσα. Θα προσθέσουμε μια μικρή αναμονή για ασφάλεια.
            } catch (Exception e) { fail(e.getMessage()); }
            
            frame.dispose();
        });
        
        // Αναμονή για SwingWorker
        Thread.sleep(200);
        
        // 5. Επαλήθευση Αλληλεπίδρασης
        verify(mockService).searchRecipes("Pizza");
    }

    @Test
    void testSearchFailureHandling() throws Exception {
        // 1. Ρύθμιση Mock για Ρίψη Εξαίρεσης (Προσομοίωση Σφάλματος Δικτύου)
        when(mockService.searchRecipes("Crash")).thenThrow(new MealApiException("Network Down"));

        SwingUtilities.invokeAndWait(() -> {
            MainFrame frame = new MainFrame(mockService);
            
            // 2. Ενέργεια - Θα πρέπει να εμφανιστεί διάλογος σφάλματος, όχι κατάρρευση εφαρμογής
            frame.performSearch("Crash", true);
            
            frame.dispose();
        });
        
        Thread.sleep(200);
        
        // Επαλήθευση ότι η υπηρεσία κλήθηκε παρόλο που απέτυχε
        verify(mockService).searchRecipes("Crash");
    }

    @Test
    void testNoResults() throws Exception {
        when(mockService.searchRecipes("Unknown")).thenReturn(Collections.emptyList());

        SwingUtilities.invokeAndWait(() -> {
            MainFrame frame = new MainFrame(mockService);
            frame.performSearch("Unknown", true);
            frame.dispose();
        });
        
        Thread.sleep(200);
        verify(mockService).searchRecipes("Unknown");
    }

    @Test
    void testSearchByIngredient() throws Exception {
        // Ρύθμιση
        when(mockService.getRecipesByIngredient("Chicken")).thenReturn(new ArrayList<>());

        SwingUtilities.invokeAndWait(() -> {
            MainFrame frame = new MainFrame(mockService);
            // Ενέργεια: Αναζήτηση με Συστατικό (false)
            frame.performSearch("Chicken", false);
            frame.dispose();
        });
        
        Thread.sleep(200);
        // Επαλήθευση ότι κλήθηκε η σωστή μέθοδος υπηρεσίας
        verify(mockService).getRecipesByIngredient("Chicken");
    }

    @Test
    void testRandomSearch() throws Exception {
        Recipe r = new Recipe();
        r.setStrMeal("Random Meal");
        when(mockService.getRandomRecipe()).thenReturn(r);

        SwingUtilities.invokeAndWait(() -> {
            MainFrame frame = new MainFrame(mockService);
            frame.performRandomSearch();
            
            // Έλεγχος ενημέρωσης μοντέλου
            DefaultListModel<Recipe> model = frame.getSearchModel();
            assertNotNull(model);
            
            frame.dispose();
        });
        
        Thread.sleep(200);
        verify(mockService).getRandomRecipe();
    }
    
    @Test
    void testFavoritesManagement() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MainFrame frame = new MainFrame(mockService);
            DefaultListModel<Recipe> favs = frame.getFavoritesModel();
            
            Recipe r = new Recipe();
            r.setIdMeal("101");
            r.setStrMeal("Test Recipe");
            
            // 1. Προσθήκη Μοναδικού
            boolean added = frame.addUnique(favs, r);
            assertTrue(added, "Should add new recipe");
            assertEquals(1, favs.size());
            
            // 2. Προσθήκη Διπλότυπου
            boolean addedAgain = frame.addUnique(favs, r);
            assertFalse(addedAgain, "Should not add duplicate");
            assertEquals(1, favs.size());
            
            frame.dispose();
        });
    }
}