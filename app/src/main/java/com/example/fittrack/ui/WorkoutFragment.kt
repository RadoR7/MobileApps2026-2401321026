package com.example.fittrack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fittrack.R
import com.example.fittrack.data.Workout
import com.example.fittrack.databinding.FragmentRecordsBinding
import com.example.fittrack.util.Validation
import com.example.fittrack.util.toast
import com.example.fittrack.util.value
import com.example.fittrack.util.visible
import java.time.LocalDate

class WorkoutFragment : Fragment() {
    private val vm by sharedFitViewModel()
    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recordTitle.text = "Activity log"
        binding.recordSubtitle.text = "Track each workout and your active minutes."
        val adapter = LogAdapter(
            onEdit = { id -> vm.workouts.value?.firstOrNull { it.id == id }?.let(::edit) },
            onDelete = { id -> vm.workouts.value?.firstOrNull { it.id == id }?.let { item ->
                confirmDelete("Delete ${item.title}?") { vm.deleteWorkout(item) }
            } }
        )
        binding.recordsList.layoutManager = LinearLayoutManager(requireContext())
        binding.recordsList.adapter = adapter
        binding.addRecord.setOnClickListener { edit(null) }
        vm.workouts.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.map { LogRow(it.id, it.title,
                "${it.type} • ${it.durationMinutes} min", "${it.caloriesBurned} kcal burned • ${it.notes}", it.date) })
            binding.emptyText.visible(list.isEmpty())
        }
    }

    private fun edit(old: Workout?) {
        formDialog(if (old == null) "Add workout" else "Edit workout", R.layout.dialog_workout,
            prefill = { v ->
                v.field(R.id.inputTitle).setText(old?.title.orEmpty())
                v.field(R.id.inputType).setText(old?.type.orEmpty())
                v.field(R.id.inputDuration).setText(old?.durationMinutes?.toString().orEmpty())
                v.field(R.id.inputCalories).setText(old?.caloriesBurned?.toString().orEmpty())
                v.initDate(R.id.inputDate, old?.date ?: LocalDate.now().toString())
                v.field(R.id.inputNotes).setText(old?.notes.orEmpty())
            }, save = { v ->
                val title = v.field(R.id.inputTitle).value()
                val type = v.field(R.id.inputType).value()
                val min = v.field(R.id.inputDuration).value().toIntOrNull()
                val cal = v.field(R.id.inputCalories).value().toIntOrNull()
                val date = v.field(R.id.inputDate).value()
                val error = Validation.workout(title, type, min, cal, date)
                if (error != null) { toast(error); false } else {
                    vm.saveWorkout(Workout(old?.id ?: 0L, title, type, min!!, cal!!, date, v.field(R.id.inputNotes).value()))
                    true
                }
            })
    }

    override fun onDestroyView() { binding.recordsList.adapter = null; _binding = null; super.onDestroyView() }
}
