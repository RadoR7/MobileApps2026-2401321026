package com.example.fittrack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fittrack.R
import com.example.fittrack.data.SleepEntry
import com.example.fittrack.databinding.FragmentRecordsBinding
import com.example.fittrack.util.TrackerMath
import com.example.fittrack.util.Validation
import com.example.fittrack.util.toast
import com.example.fittrack.util.value
import com.example.fittrack.util.visible
import java.time.LocalDate
import java.util.Locale

class SleepFragment : Fragment() {
    private val vm by sharedFitViewModel()
    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recordTitle.text = "Sleep log"
        binding.recordSubtitle.text = "The date represents the morning you woke up."
        val adapter = LogAdapter(
            onEdit = { id -> vm.sleep.value?.firstOrNull { it.id == id }?.let(::edit) },
            onDelete = { id -> vm.sleep.value?.firstOrNull { it.id == id }?.let { item ->
                confirmDelete("Delete sleep entry for ${item.date}?") { vm.deleteSleep(item) }
            } }
        )
        binding.recordsList.layoutManager = LinearLayoutManager(requireContext())
        binding.recordsList.adapter = adapter
        binding.addRecord.setOnClickListener { edit(null) }
        vm.sleep.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.map { LogRow(it.id,
                String.format(Locale.US, "%.1f hours of sleep", it.hours),
                "${it.bedTime} → ${it.wakeTime} • Quality ${it.quality}/5", it.notes, it.date) })
            binding.emptyText.visible(list.isEmpty())
        }
    }

    private fun edit(old: SleepEntry?) {
        formDialog(if (old == null) "Add sleep" else "Edit sleep", R.layout.dialog_sleep,
            prefill = { v ->
                v.initDate(R.id.inputDate, old?.date ?: LocalDate.now().toString())
                v.field(R.id.inputBed).setText(old?.bedTime ?: "23:00")
                v.field(R.id.inputWake).setText(old?.wakeTime ?: "07:00")
                v.field(R.id.inputQuality).setText(old?.quality?.toString() ?: "4")
                v.field(R.id.inputNotes).setText(old?.notes.orEmpty())
            }, save = { v ->
                val date = v.field(R.id.inputDate).value()
                val bed = v.field(R.id.inputBed).value()
                val wake = v.field(R.id.inputWake).value()
                val quality = v.field(R.id.inputQuality).value().toIntOrNull()
                val error = Validation.sleep(date, bed, wake, quality)
                if (error != null) { toast(error); false } else {
                    vm.saveSleep(SleepEntry(old?.id ?: 0L, date, bed, wake,
                        TrackerMath.sleepHours(bed, wake), quality!!, v.field(R.id.inputNotes).value()))
                    true
                }
            })
    }

    override fun onDestroyView() { binding.recordsList.adapter = null; _binding = null; super.onDestroyView() }
}
