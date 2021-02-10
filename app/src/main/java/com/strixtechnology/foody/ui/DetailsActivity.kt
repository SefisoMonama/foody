package com.strixtechnology.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.strixtechnology.foody.R
import com.strixtechnology.foody.adapters.PagerAdapter
import com.strixtechnology.foody.data.database.entities.FavoriteEntity
import com.strixtechnology.foody.ui.fragments.ingredients.IngredientsFragment
import com.strixtechnology.foody.ui.fragments.instructions.instructionsFragment
import com.strixtechnology.foody.ui.fragments.overview.overviewFragment
import com.strixtechnology.foody.util.Constants.Companion.RECIPE_RESULT_KEY
import com.strixtechnology.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import kotlin.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private var recipeSaved = false
    private var savedRecipeId = 0



    //@SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolBar)
        toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(overviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(instructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorite_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }else if(item.itemId == R.id.save_to_favorite_menu && !recipeSaved){
            saveToFavorites(item)
        }else if (item.itemId == R.id.save_to_favorite_menu && recipeSaved){
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, {favouriteEntity ->
          try{
              for (savedRecipe in favouriteEntity){
                  if (savedRecipe.result.recipeId == args.result.recipeId){
                      changeMenuItemColor(menuItem, R.color.yellow)
                      savedRecipeId = savedRecipe.id
                      recipeSaved = true
                  } else{
                      changeMenuItemColor(menuItem, R.color.white)
                  }
              }
          }catch (e: Exception){
              Log.d("DetailsActivity", e.message.toString())
          }
        })
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoriteEntity(0,args.result)
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Added to favorite!")
        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem){
        val favoriteEntity = FavoriteEntity(savedRecipeId, args.result)
        mainViewModel.deleteFavoriteRecipe(favoriteEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites!")
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(detailsLayout, message, Snackbar.LENGTH_SHORT).setAction("Ok"){}.show()
        recipeSaved = false
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this,color))
    }
}
