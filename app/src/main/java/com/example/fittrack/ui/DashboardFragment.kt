package com.example.fittrack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.fittrack.R
import com.example.fittrack.data.Goals
import com.example.fittrack.data.WaterEntry
import com.example.fittrack.databinding.FragmentDashboardBinding
import com.example.fittrack.util.DailySummary
import com.example.fittrack.util.QrTools
import com.example.fittrack.util.ShareTools
import com.example.fittrack.util.TrackerMath
import com.example.fittrack.util.Validation
import com.example.fittrack.util.prettyDate
import com.example.fittrack.util.toast
import com.example.fittrack.util.value
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate
import java.util.Locale

class DashboardFragment : Fragment() {
    private val vm by sharedFitViewModel()
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val currentDate: String get() = LocalDate.now().toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.workouts.observe(viewLifecycleOwner) { render() }
        vm.foods.observe(viewLifecycleOwner) { render() }
        vm.sleep.observe(viewLifecycleOwner) { render() }
        vm.water.observe(viewLifecycleOwner) { render() }
        vm.goals.observe(viewLifecycleOwner) { render() }
        binding.addWater.setOnClickListener { editWater(null) }
        binding.waterHistory.setOnClickListener { showWaterHistory() }
        binding.shareDay.setOnClickListener {
            ShareTools.share(requireContext(), ShareTools.report(LocalDate.now(), summary(), currentGoals()))
        }
        binding.showQr.setOnClickListener {
            val report = ShareTools.report(LocalDate.now(), summary(), currentGoals())
            val qr = try { QrTools.fromText(report) } catch (_: Exception) {
                toast("Could not generate QR code"); return@setOnClickListener
            }
            val image = ImageView(requireContext()).apply {
                setImageBitmap(qr)
                setPadding(24, 24, 24, 24)
                adjustViewBounds = true
                contentDescription = "QR code containing your daily report"
            }
            MaterialAlertDialogBuilder(requireContext()).setTitle("Scan your report")
                .setView(image).setPositiveButton("Done", null).show()
        }
        render()
    }

    private fun currentGoals() = vm.goals.value ?: Goals()
    private fun summary(): DailySummary = TrackerMath.today(currentDate, vm.workouts.value.orEmpty(),
        vm.foods.value.orEmpty(), vm.sleep.value.orEmpty(), vm.water.value.orEmpty())

    private fun render() {
        if (_binding == null) return
        val s = summary()
        val g = currentGoals()
        binding.todayDate.text = prettyDate(currentDate)
        binding.headlineSummary.text = "${s.workoutsCount} workout(s) • ${s.burnedCalories} active kcal • ${"%.1f".format(Locale.US, s.proteinGrams)} g protein"
        binding.dailyNet.text = "${s.loggedNetCalories} logged net kcal"
        binding.foodSummary.text = "${s.foodCalories} / ${g.caloriesTarget} kcal eaten"
        binding.workoutSummary.text = "${s.activeMinutes} / ${g.activityMinutesTarget} min active"
        binding.sleepSummary.text = String.format(Locale.US, "%.1f / %.1f hours • quality %s",
            s.sleepHours, g.sleepHoursTarget, s.sleepQuality?.let { "$it/5" } ?: "not logged")
        binding.waterSummary.text = "${s.waterMl} / ${g.waterMlTarget} ml"
        binding.foodProgress.progress = TrackerMath.percent(s.foodCalories.toDouble(), g.caloriesTarget.toDouble())
        binding.workoutProgress.progress = TrackerMath.percent(s.activeMinutes.toDouble(), g.activityMinutesTarget.toDouble())
        binding.sleepProgress.progress = TrackerMath.percent(s.sleepHours, g.sleepHoursTarget)
        binding.waterProgress.progress = TrackerMath.percent(s.waterMl.toDouble(), g.waterMlTarget.toDouble())
    }

    private fun showWaterHistory() {
        val entries = vm.water.value.orEmpty().filter { it.date == currentDate }
        if (entries.isEmpty()) { toast("No water entries today"); return }
        val labels = entries.map { "${it.amountMl} ml • ${it.date}" }.toTypedArray()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Water history • tap to manage")
            .setItems(labels) { _, index ->
                val item = entries[index]
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("${item.amountMl} ml")
                    .setItems(arrayOf("Edit", "Delete")) { _, which ->
                        if (which == 0) editWater(item) else confirmDelete("Delete this water entry?") {
                            vm.deleteWater(item)
                        }
                    }.show()
            }.setNegativeButton("Close", null).show()
    }

    private fun editWater(old: WaterEntry?) {
        formDialog(if (old == null) "Log water" else "Edit water", R.layout.dialog_water,
            prefill = { v -> v.field(R.id.inputAmount).setText(old?.amountMl?.toString() ?: "250") },
            save = { v ->
                val amount = v.field(R.id.inputAmount).value().toIntOrNull()
                val error = Validation.water(amount)
                if (error != null) { toast(error); false } else {
                    vm.saveWater(WaterEntry(old?.id ?: 0L, old?.date ?: currentDate, amount!!))
                    true
                }
            })
    }

    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}
