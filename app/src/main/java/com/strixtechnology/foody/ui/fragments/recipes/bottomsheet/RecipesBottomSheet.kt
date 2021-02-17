package com.strixtechnology.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.strixtechnology.foody.R
import com.strixtechnology.foody.databinding.FragmentRecipesBinding
import com.strixtechnology.foody.databinding.RecipesBottomSheetBinding
import com.strixtechnology.foody.util.Constants
import com.strixtechnology.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.strixtechnology.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.strixtechnology.foody.viewmodels.RecipesViewModel
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0
    private var mealTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
        })

        binding.mealTypeChipGroup.setOnCheckedChangeListener{group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChipId = selectedChipId
            mealTypeChip = selectedMealType
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChipId = selectedChipId
            dietTypeChip = selectedDietType
        }

        binding.applyBtn.setOnClickListener {

            if (recipesViewModel.networkStatus) {
                recipesViewModel.saveMealAndDietType(
                        mealTypeChip,
                        mealTypeChipId,
                        dietTypeChip,
                        dietTypeChipId
                )
                val action =
                        RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
                findNavController().navigate(action)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }
        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup){
        if(chipId != 0){
            try{
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            }catch (e: Exception){
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}