package com.strixtechnology.foody.data.database

import androidx.room.*
import com.strixtechnology.foody.data.database.entities.FavoriteEntity
import com.strixtechnology.foody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    //will replace the dublicate entity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Query("SELECT * FROM favorite_recipe_table ORDER BY id ASC")
    fun readFavoriteRecipe(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite_recipe_table")
    suspend fun deleteAllFavoriteRecipes()
}