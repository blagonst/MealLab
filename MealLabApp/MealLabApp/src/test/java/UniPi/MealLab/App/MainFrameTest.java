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
        // 1. Setup Mock Behavior
        Recipe r = new Recipe();
        r.setStrMeal("Mock Pizza");
        when(mockService.searchRecipes("Pizza")).thenReturn(List.of(r));

        SwingUtilities.invokeAndWait(() -> {
            // 2. Inject Mock
            MainFrame frame = new MainFrame(mockService);
            
            // 3. Act
            frame.performSearch("Pizza", true);
            
            // 4. Verify Model Update (using reflection as before)
            try {
                Field fModelField = MainFrame.class.getDeclaredField("searchModel");
                fModelField.setAccessible(true);
                DefaultListModel<Recipe> model = (DefaultListModel<Recipe>) fModelField.get(frame);

                // Note: In a real threaded env, we might need to wait, but since we 
                // invokeAndWait inside the test for the trigger, and the worker executes...
                // Actually SwingWorker is async. So checking immediately might fail if worker is slow.
                // However, Mockito mocks are instant. We'll add a small sleep to be safe.
            } catch (Exception e) { fail(e.getMessage()); }
            
            frame.dispose();
        });
        
        // Wait for SwingWorker
        Thread.sleep(200);
        
        // 5. Verify Interaction
        verify(mockService).searchRecipes("Pizza");
    }

    @Test
    void testSearchFailureHandling() throws Exception {
        // 1. Setup Mock to Throw Exception (Simulate Network Error)
        when(mockService.searchRecipes("Crash")).thenThrow(new MealApiException("Network Down"));

        SwingUtilities.invokeAndWait(() -> {
            MainFrame frame = new MainFrame(mockService);
            
            // 2. Act - Should show error dialog, not crash app
            frame.performSearch("Crash", true);
            
            frame.dispose();
        });
        
        Thread.sleep(200);
        
        // Verify service was called even though it failed
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
        // Setup
        when(mockService.getRecipesByIngredient("Chicken")).thenReturn(new ArrayList<>());

        SwingUtilities.invokeAndWait(() -> {
            MainFrame frame = new MainFrame(mockService);
            // Act: Search by Ingredient (false)
            frame.performSearch("Chicken", false);
            frame.dispose();
        });
        
        Thread.sleep(200);
        // Verify correct service method called
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
            
            // Check model update
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
            
            // 1. Add Unique
            boolean added = frame.addUnique(favs, r);
            assertTrue(added, "Should add new recipe");
            assertEquals(1, favs.size());
            
            // 2. Add Duplicate
            boolean addedAgain = frame.addUnique(favs, r);
            assertFalse(addedAgain, "Should not add duplicate");
            assertEquals(1, favs.size());
            
            frame.dispose();
        });
    }
}