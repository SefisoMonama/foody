package com.strixtechnology.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strixtechnology.foody.data.database.entities.FavoriteEntity
import com.strixtechnology.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.strixtechnology.foody.util.RecipesDiffUtil

class FavoriteRecipesAdapter: RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(){

    private var favoriteRecipe = emptyList<FavoriteEntity>()

    class MyViewHolder(private val binding:FavoriteRecipesRowLayoutBinding):
          RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEntity: FavoriteEntity){
            binding.favoriteEntity = favoriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedRecipe = favoriteRecipe[position]
        holder.bind(selectedRecipe)
    }

    override fun getItemCount(): Int {
        return favoriteRecipe.size
    }

    fun setData(newFavoriteRecipes: List<FavoriteEntity>){
        val favoriteRecipeDiffUtil = RecipesDiffUtil(favoriteRecipe, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipeDiffUtil)
        favoriteRecipe = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

}