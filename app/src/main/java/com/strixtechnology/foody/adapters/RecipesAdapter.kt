package com.strixtechnology.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strixtechnology.foody.databinding.RecipesRowLayoutBinding
import com.strixtechnology.foody.models.FoodRecipe
import com.strixtechnology.foody.models.Result
import com.strixtechnology.foody.util.RecipesDiffUtil

class RecipesAdapter:  RecyclerView.Adapter<RecipesAdapter.MyViewHolder>(){
    private var recipes = emptyList<Result>()
    class MyViewHolder(private val binding: RecipesRowLayoutBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder{
              val layoutInflater = LayoutInflater.from(parent.context)
              val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
              return MyViewHolder(binding)
            }
        }

 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesAdapter.MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipesAdapter.MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: FoodRecipe){
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}