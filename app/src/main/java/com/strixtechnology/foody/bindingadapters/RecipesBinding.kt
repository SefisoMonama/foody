package com.strixtechnology.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.strixtechnology.foody.data.database.RecipesDatabase
import com.strixtechnology.foody.data.database.RecipesEntity
import com.strixtechnology.foody.models.FoodRecipe
import com.strixtechnology.foody.util.NetworkResult
import okhttp3.Response

class RecipesBinding {
    companion object{
        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ){
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                imageView.visibility = View.VISIBLE
            }else if( apiResponse is NetworkResult.Loading){
                imageView.visibility = View.INVISIBLE
            }else if( apiResponse is NetworkResult.Success){
                imageView.visibility = View.INVISIBLE
            }
         }

        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextViewViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ){
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            }else if( apiResponse is NetworkResult.Loading){
                textView.visibility = View.INVISIBLE
            }else if( apiResponse is NetworkResult.Success){
                textView.visibility = View.INVISIBLE
            }
        }

    }
}