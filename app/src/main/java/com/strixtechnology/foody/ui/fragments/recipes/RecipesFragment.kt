package com.strixtechnology.foody.ui.fragments.recipes

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.strixtechnology.foody.viewmodels.MainViewModel
import com.strixtechnology.foody.R
import com.strixtechnology.foody.adapters.RecipesAdapter
import com.strixtechnology.foody.databinding.FragmentRecipesBinding
import com.strixtechnology.foody.util.Constants.Companion.API_KEY
import com.strixtechnology.foody.util.NetworkListener
import com.strixtechnology.foody.util.NetworkResult
import com.strixtechnology.foody.util.observeOnce
import com.strixtechnology.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment() : Fragment(), SearchView.OnQueryTextListener, Parcelable {

    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    //private lateinit var mView: View

    private lateinit var networkListener: NetworkListener

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment

        _binding= FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)
        setupRecyclerView()

    recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
        recipesViewModel.backOnline = it
    })

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.recipesFab.setOnClickListener{
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }
        return binding.root
    }


    private fun setupRecyclerView() {
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchApiData(query)
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun readDatabase() {
       lifecycleScope.launch{
           mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, {database ->
               if(database.isNotEmpty() && args.backFromBottomSheet){
                   Log.d("RecipesFragment","readDatabase called")
                   mAdapter.setData(database[0].foodRecipe)
                   hideShimmerEffect()
               }else{
                   requestApiData()
               }
           })
       }
    }

    private fun searchApiData(searchQuery: String){
        showShimmerEffect()

        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner, {response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })

    }
    private fun requestApiData() {
        Log.d("RecipesFragment","requestApiData called")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }


    private fun loadDataFromCache(){
        lifecycleScope.launch{
            mainViewModel.readRecipes.observe(viewLifecycleOwner, {database ->
                if(database.isNotEmpty()){
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }
    private fun showShimmerEffect() {
        binding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerView.hideShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecipesFragment> {
        override fun createFromParcel(parcel: Parcel): RecipesFragment {
            return RecipesFragment(parcel)
        }

        override fun newArray(size: Int): Array<RecipesFragment?> {
            return arrayOfNulls(size)
        }
    }
}