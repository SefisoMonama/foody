package com.strixtechnology.foody.data

import com.strixtechnology.foody.data.network.FoodRecipesAPI
import com.strixtechnology.foody.models.FoodJoke
import com.strixtechnology.foody.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesAPI: FoodRecipesAPI
){
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesAPI.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe>{
        return foodRecipesAPI.searchRecipes(searchQuery)
    }
    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
        return foodRecipesAPI.getFoodJoke(apiKey)
    }
}