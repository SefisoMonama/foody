package com.strixtechnology.foody.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.strixtechnology.foody.R
import com.strixtechnology.foody.data.database.entities.FavoriteEntity
import com.strixtechnology.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.strixtechnology.foody.ui.fragments.favorite.FavoriteRecipesFragmentDirections
import com.strixtechnology.foody.util.RecipesDiffUtil
import com.strixtechnology.foody.viewmodels.MainViewModel

class FavoriteRecipesAdapter(
        private val requireActivity: FragmentActivity,
        private val mainViewModel: MainViewModel
): RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback{

    private var multiSelection = false
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var favoriteRecipe = emptyList<FavoriteEntity>()

    class MyViewHolder(val binding:FavoriteRecipesRowLayoutBinding):
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
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipe[position]
        holder.bind(currentRecipe)

        saveItemStateOnScroll(currentRecipe, holder)

        /**
         * single click listener
         */
        holder.binding.favoriteRecipesRowLayout.setOnClickListener{
            if(multiSelection){
                applySelection(holder, currentRecipe)
            }else{
                val action =
                        FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                                currentRecipe.result
                        )
                holder.itemView.findNavController().navigate(action)
            }


        }
        /**
         * Long click listener
         */
        holder.binding.favoriteRecipesRowLayout.setOnLongClickListener{
            if(!multiSelection){
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            }else{
                applySelection(holder, currentRecipe)
                true
            }

        }

    }

    private  fun saveItemStateOnScroll(currentRecipe: FavoriteEntity, holder: MyViewHolder){
        if(selectedRecipes.contains(currentRecipe)){
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        }else{
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoriteEntity){
        if(selectedRecipes.contains(currentRecipe)){
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        }else{
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int){
        holder.binding.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.favoriteRowCardView.strokeColor =
        ContextCompat.getColor(requireActivity, strokeColor)
    }
    private fun applyActionModeTitle(){
        when(selectedRecipes.size){
            0 ->{
                multiSelection = false
                mActionMode.finish()
            }
            1 ->{
                mActionMode.title = "${selectedRecipes.size} item Selected"
            }
            else ->{
                mActionMode.title = "${selectedRecipes.size} items Selected"

            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipe.size
    }
    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if(menu?.itemId ==R.id.delete_favorite_recipe_menu){
           selectedRecipes.forEach{
               mainViewModel.deleteFavoriteRecipe(it)
           }
            showSnackBar("${selectedRecipes.size} Recipe/s removed!")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()

        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach{holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }
    private fun applyStatusBarColor(color: Int){
        requireActivity.window.statusBarColor =
           ContextCompat.getColor(requireActivity, color)
    }
    fun setData(newFavoriteRecipes: List<FavoriteEntity>){
        val favoriteRecipeDiffUtil = RecipesDiffUtil(favoriteRecipe, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipeDiffUtil)
        favoriteRecipe = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String){
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setAction("Ok"){}.show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }

}