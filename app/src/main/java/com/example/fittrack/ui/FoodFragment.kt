package com.example.fittrack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fittrack.R
import com.example.fittrack.data.Food
import com.example.fittrack.databinding.FragmentRecordsBinding
import com.example.fittrack.util.Validation
import com.example.fittrack.util.toast
import com.example.fittrack.util.value
import com.example.fittrack.util.visible
import java.time.LocalDate
import java.util.Locale

class FoodFragment : Fragment() {
    private val vm by sharedFitViewModel()
    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recordTitle.text = "Nutrition log"
        binding.recordSubtitle.text = "Record your meals and macronutrients."
        val adapter = LogAdapter(
            onEdit = { id -> vm.foods.value?.firstOrNull { it.id == id }?.let(::edit) },
            onDelete = { id -> vm.foods.value?.firstOrNull { it.id == id }?.let { item ->
                confirmDelete("Delete ${item.name}?") { vm.deleteFood(item) }
            } }
        )
        binding.recordsList.layoutManager = LinearLayoutManager(requireContext())
        binding.recordsList.adapter = adapter
        binding.addRecord.setOnClickListener { edit(null) }
        vm.foods.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.map { LogRow(it.id, it.name,
                "${it.mealType} • ${it.calories} kcal",
                String.format(Locale.US, "P %.1f g • C %.1f g • F %.1f g • %s", it.protein, it.carbs, it.fat, it.notes),
                it.date) })
            binding.emptyText.visible(list.isEmpty())
        }
    }

    private fun edit(old: Food?) {
        formDialog(if (old == null) "Add meal" else "Edit meal", R.layout.dialog_food,
            prefill = { v ->
                v.field(R.id.inputName).setText(old?.name.orEmpty())
                v.field(R.id.inputMealType).setText(old?.mealType ?: "Lunch")
                v.field(R.id.inputCalories).setText(old?.calories?.toString().orEmpty())
                v.field(R.id.inputProtein).setText(old?.protein?.toString() ?: "0")
                v.field(R.id.inputCarbs).setText(old?.carbs?.toString() ?: "0")
                v.field(R.id.inputFat).setText(old?.fat?.toString() ?: "0")
                v.initDate(R.id.inputDate, old?.date ?: LocalDate.now().toString())
                v.field(R.id.inputNotes).setText(old?.notes.orEmpty())
            }, save = { v ->
                val name = v.field(R.id.inputName).value()
                val type = v.field(R.id.inputMealType).value()
                val calories = v.field(R.id.inputCalories).value().toIntOrNull()
                val protein = v.field(R.id.inputProtein).value().toDoubleOrNull()
                val carbs = v.field(R.id.inputCarbs).value().toDoubleOrNull()
                val fat = v.field(R.id.inputFat).value().toDoubleOrNull()
                val date = v.field(R.id.inputDate).value()
                val error = Validation.food(name, type, calories, protein, carbs, fat, date)
                if (error != null) { toast(error); false } else {
                    vm.saveFood(Food(old?.id ?: 0L, name, type, calories!!, protein!!, carbs!!, fat!!,
                        date, v.field(R.id.inputNotes).value()))
                    true
                }
            })
    }

    override fun onDestroyView() { binding.recordsList.adapter = null; _binding = null; super.onDestroyView() }
}
