package com.example.android.bakingapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    List<RecipeEntry> loadAllRecipes();

    @Insert
    void insertRecipe(RecipeEntry recipeEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipeEntry taskEntry);

    //@Delete with id
    @Query("DELETE FROM recipe WHERE id = :id")
    void deleteRecipe(String id);

    //@Delete
    @Query("DELETE FROM recipe")
    void deleteAllRecipes();

    @Query("SELECT * FROM recipe WHERE id = :id")
    List<RecipeEntry> loadRecipeById(String id);

    @Query("SELECT * FROM recipe WHERE id = :id")
    LiveData<List<RecipeEntry>> loadRecipeNamesById(String id);

}