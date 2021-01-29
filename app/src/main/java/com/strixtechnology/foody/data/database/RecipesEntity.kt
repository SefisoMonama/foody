package com.strixtechnology.foody.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.strixtechnology.foody.models.FoodRecipe
import com.strixtechnology.foody.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipe: FoodRecipe) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}