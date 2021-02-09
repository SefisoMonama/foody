package com.strixtechnology.foody.data

import com.strixtechnology.foody.data.database.RecipesDao
import com.strixtechnology.foody.data.database.entities.FavoriteEntity
import com.strixtechnology.foody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {
    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>> {
        return recipesDao.readFavoriteRecipe()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity){
        recipesDao.insertFavoriteRecipe(favoriteEntity)
    }

    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity){
        recipesDao.deleteFavoriteRecipe(favoriteEntity)
    }

    suspend fun  deleteAllFavoriteRecipes(){
        return recipesDao.deleteAllFavoriteRecipes()
    }


}