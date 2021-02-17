package com.strixtechnology.foody.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.strixtechnology.foody.R
import com.strixtechnology.foody.databinding.IngredientsRowLayoutBinding
import com.strixtechnology.foody.models.ExtendedIngredient
import com.strixtechnology.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.strixtechnology.foody.util.RecipesDiffUtil

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: IngredientsRowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private var ingredientsList = emptyList<ExtendedIngredient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(IngredientsRowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ingredientImageView.load(BASE_IMAGE_URL + ingredientsList[position].image){
            crossfade(900)
            error(R.drawable.ic_error_placeholder)
        }

        holder.binding.ingredientName.text = ingredientsList[position].name.capitalize()
        holder.binding.ingredientAmount.text = ingredientsList[position].amount.toString()
        holder.binding.ingredientUnit.text =ingredientsList[position].unit
        holder.binding.ingredientConsistency.text = ingredientsList[position].consistency
        holder.binding.ingredientOriginal.text =ingredientsList[position].original

    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients : List<ExtendedIngredient>){
        val ingredientsDiffUtil =
                RecipesDiffUtil(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}