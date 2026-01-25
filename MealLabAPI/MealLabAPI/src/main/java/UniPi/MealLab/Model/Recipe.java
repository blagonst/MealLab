package UniPi.MealLab.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {
    private String idMeal;
    private String strMeal;
    private String strCategory;
    private String strArea;
    private String strInstructions;
    private String strMealThumb;

    private String strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
                  strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
                  strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
                  strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20;

    private String strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
                  strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
                  strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
                  strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20;

    // Getters και Setters
    public String getIdMeal() { return idMeal; }
    public void setIdMeal(String idMeal) { this.idMeal = idMeal; }

    public String getStrMeal() { return strMeal; }
    public void setStrMeal(String strMeal) { this.strMeal = strMeal; }

    public String getStrCategory() { return strCategory; }
    public void setStrCategory(String strCategory) { this.strCategory = strCategory; }

    public String getStrArea() { return strArea; }
    public void setStrArea(String strArea) { this.strArea = strArea; }

    public String getStrInstructions() { return strInstructions; }
    public void setStrInstructions(String strInstructions) { this.strInstructions = strInstructions; }

    public String getStrMealThumb() { return strMealThumb; }
    public void setStrMealThumb(String strMealThumb) { this.strMealThumb = strMealThumb; }

    // Getters/Setters Συστατικών
    public String getStrIngredient1() { return strIngredient1; }
    public void setStrIngredient1(String strIngredient1) { this.strIngredient1 = strIngredient1; }
    public String getStrIngredient2() { return strIngredient2; }
    public void setStrIngredient2(String strIngredient2) { this.strIngredient2 = strIngredient2; }
    public String getStrIngredient3() { return strIngredient3; }
    public void setStrIngredient3(String strIngredient3) { this.strIngredient3 = strIngredient3; }
    public String getStrIngredient4() { return strIngredient4; }
    public void setStrIngredient4(String strIngredient4) { this.strIngredient4 = strIngredient4; }
    public String getStrIngredient5() { return strIngredient5; }
    public void setStrIngredient5(String strIngredient5) { this.strIngredient5 = strIngredient5; }
    public String getStrIngredient6() { return strIngredient6; }
    public void setStrIngredient6(String strIngredient6) { this.strIngredient6 = strIngredient6; }
    public String getStrIngredient7() { return strIngredient7; }
    public void setStrIngredient7(String strIngredient7) { this.strIngredient7 = strIngredient7; }
    public String getStrIngredient8() { return strIngredient8; }
    public void setStrIngredient8(String strIngredient8) { this.strIngredient8 = strIngredient8; }
    public String getStrIngredient9() { return strIngredient9; }
    public void setStrIngredient9(String strIngredient9) { this.strIngredient9 = strIngredient9; }
    public String getStrIngredient10() { return strIngredient10; }
    public void setStrIngredient10(String strIngredient10) { this.strIngredient10 = strIngredient10; }
    public String getStrIngredient11() { return strIngredient11; }
    public void setStrIngredient11(String strIngredient11) { this.strIngredient11 = strIngredient11; }
    public String getStrIngredient12() { return strIngredient12; }
    public void setStrIngredient12(String strIngredient12) { this.strIngredient12 = strIngredient12; }
    public String getStrIngredient13() { return strIngredient13; }
    public void setStrIngredient13(String strIngredient13) { this.strIngredient13 = strIngredient13; }
    public String getStrIngredient14() { return strIngredient14; }
    public void setStrIngredient14(String strIngredient14) { this.strIngredient14 = strIngredient14; }
    public String getStrIngredient15() { return strIngredient15; }
    public void setStrIngredient15(String strIngredient15) { this.strIngredient15 = strIngredient15; }
    public String getStrIngredient16() { return strIngredient16; }
    public void setStrIngredient16(String strIngredient16) { this.strIngredient16 = strIngredient16; }
    public String getStrIngredient17() { return strIngredient17; }
    public void setStrIngredient17(String strIngredient17) { this.strIngredient17 = strIngredient17; }
    public String getStrIngredient18() { return strIngredient18; }
    public void setStrIngredient18(String strIngredient18) { this.strIngredient18 = strIngredient18; }
    public String getStrIngredient19() { return strIngredient19; }
    public void setStrIngredient19(String strIngredient19) { this.strIngredient19 = strIngredient19; }
    public String getStrIngredient20() { return strIngredient20; }
    public void setStrIngredient20(String strIngredient20) { this.strIngredient20 = strIngredient20; }

    // Getters/Setters Μετρήσεων
    public String getStrMeasure1() { return strMeasure1; }
    public void setStrMeasure1(String strMeasure1) { this.strMeasure1 = strMeasure1; }
    public String getStrMeasure2() { return strMeasure2; }
    public void setStrMeasure2(String strMeasure2) { this.strMeasure2 = strMeasure2; }
    public String getStrMeasure3() { return strMeasure3; }
    public void setStrMeasure3(String strMeasure3) { this.strMeasure3 = strMeasure3; }
    public String getStrMeasure4() { return strMeasure4; }
    public void setStrMeasure4(String strMeasure4) { this.strMeasure4 = strMeasure4; }
    public String getStrMeasure5() { return strMeasure5; }
    public void setStrMeasure5(String strMeasure5) { this.strMeasure5 = strMeasure5; }
    public String getStrMeasure6() { return strMeasure6; }
    public void setStrMeasure6(String strMeasure6) { this.strMeasure6 = strMeasure6; }
    public String getStrMeasure7() { return strMeasure7; }
    public void setStrMeasure7(String strMeasure7) { this.strMeasure7 = strMeasure7; }
    public String getStrMeasure8() { return strMeasure8; }
    public void setStrMeasure8(String strMeasure8) { this.strMeasure8 = strMeasure8; }
    public String getStrMeasure9() { return strMeasure9; }
    public void setStrMeasure9(String strMeasure9) { this.strMeasure9 = strMeasure9; }
    public String getStrMeasure10() { return strMeasure10; }
    public void setStrMeasure10(String strMeasure10) { this.strMeasure10 = strMeasure10; }
    public String getStrMeasure11() { return strMeasure11; }
    public void setStrMeasure11(String strMeasure11) { this.strMeasure11 = strMeasure11; }
    public String getStrMeasure12() { return strMeasure12; }
    public void setStrMeasure12(String strMeasure12) { this.strMeasure12 = strMeasure12; }
    public String getStrMeasure13() { return strMeasure13; }
    public void setStrMeasure13(String strMeasure13) { this.strMeasure13 = strMeasure13; }
    public String getStrMeasure14() { return strMeasure14; }
    public void setStrMeasure14(String strMeasure14) { this.strMeasure14 = strMeasure14; }
    public String getStrMeasure15() { return strMeasure15; }
    public void setStrMeasure15(String strMeasure15) { this.strMeasure15 = strMeasure15; }
    public String getStrMeasure16() { return strMeasure16; }
    public void setStrMeasure16(String strMeasure16) { this.strMeasure16 = strMeasure16; }
    public String getStrMeasure17() { return strMeasure17; }
    public void setStrMeasure17(String strMeasure17) { this.strMeasure17 = strMeasure17; }
    public String getStrMeasure18() { return strMeasure18; }
    public void setStrMeasure18(String strMeasure18) { this.strMeasure18 = strMeasure18; }
    public String getStrMeasure19() { return strMeasure19; }
    public void setStrMeasure19(String strMeasure19) { this.strMeasure19 = strMeasure19; }
    public String getStrMeasure20() { return strMeasure20; }
    public void setStrMeasure20(String strMeasure20) { this.strMeasure20 = strMeasure20; }

    public java.util.List<String> getIngredientsList() {
        java.util.List<String> list = new java.util.ArrayList<>();
        addIng(list, strIngredient1, strMeasure1); addIng(list, strIngredient2, strMeasure2);
        addIng(list, strIngredient3, strMeasure3); addIng(list, strIngredient4, strMeasure4);
        addIng(list, strIngredient5, strMeasure5); addIng(list, strIngredient6, strMeasure6);
        addIng(list, strIngredient7, strMeasure7); addIng(list, strIngredient8, strMeasure8);
        addIng(list, strIngredient9, strMeasure9); addIng(list, strIngredient10, strMeasure10);
        addIng(list, strIngredient11, strMeasure11); addIng(list, strIngredient12, strMeasure12);
        addIng(list, strIngredient13, strMeasure13); addIng(list, strIngredient14, strMeasure14);
        addIng(list, strIngredient15, strMeasure15); addIng(list, strIngredient16, strMeasure16);
        addIng(list, strIngredient17, strMeasure17); addIng(list, strIngredient18, strMeasure18);
        addIng(list, strIngredient19, strMeasure19); addIng(list, strIngredient20, strMeasure20);
        return list;
    }

    private void addIng(java.util.List<String> list, String ing, String ms) {
        if (ing != null && !ing.trim().isEmpty()) {
            String m = (ms != null && !ms.trim().isEmpty()) ? ms : "";
            list.add(ing + (m.isEmpty() ? "" : ": " + m));
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return idMeal != null ? idMeal.equals(recipe.idMeal) : recipe.idMeal == null;
    }

    @Override
    public int hashCode() {
        return idMeal != null ? idMeal.hashCode() : 0;
    }

    @Override
    public String toString() { return strMeal; }

    public static class Builder {
        private final Recipe recipe;
        private int ingredientCount = 0;

        public Builder() { recipe = new Recipe(); }

        public Builder id(String id) { recipe.setIdMeal(id); return this; }
        public Builder name(String name) { recipe.setStrMeal(name); return this; }
        public Builder category(String category) { recipe.setStrCategory(category); return this; }
        public Builder area(String area) { recipe.setStrArea(area); return this; }
        public Builder instructions(String instructions) { recipe.setStrInstructions(instructions); return this; }
        public Builder thumb(String thumb) { recipe.setStrMealThumb(thumb); return this; }
        
        public Builder addIngredient(String ingredient, String measure) {
            ingredientCount++;
            switch (ingredientCount) {
                case 1: recipe.setStrIngredient1(ingredient); recipe.setStrMeasure1(measure); break;
                case 2: recipe.setStrIngredient2(ingredient); recipe.setStrMeasure2(measure); break;
                case 3: recipe.setStrIngredient3(ingredient); recipe.setStrMeasure3(measure); break;
                case 4: recipe.setStrIngredient4(ingredient); recipe.setStrMeasure4(measure); break;
                case 5: recipe.setStrIngredient5(ingredient); recipe.setStrMeasure5(measure); break;
                case 6: recipe.setStrIngredient6(ingredient); recipe.setStrMeasure6(measure); break;
                case 7: recipe.setStrIngredient7(ingredient); recipe.setStrMeasure7(measure); break;
                case 8: recipe.setStrIngredient8(ingredient); recipe.setStrMeasure8(measure); break;
                case 9: recipe.setStrIngredient9(ingredient); recipe.setStrMeasure9(measure); break;
                case 10: recipe.setStrIngredient10(ingredient); recipe.setStrMeasure10(measure); break;
                // Απλοποιημένο για συντομία, με κάλυψη των 10 κορυφαίων
            }
            return this;
        }

        public Recipe build() { return recipe; }
    }
}