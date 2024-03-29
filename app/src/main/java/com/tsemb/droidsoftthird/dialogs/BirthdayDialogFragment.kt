package com.tsemb.droidsoftthird.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate
import java.util.Calendar
import android.widget.NumberPicker
import android.widget.Toast
import com.tsemb.droidsoftthird.R
import java.time.Period

class BirthdayDialogFragment(private val onPositiveClickListener: (LocalDate) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val maxEligibleYear = currentYear - 13 // 13歳以上を確実にするための年。

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_birthday, null)

        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker).apply {
            minValue = currentYear - 100
            maxValue = maxEligibleYear
            value = currentYear - 20 // デフォルトの選択年。
        }

        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker).apply {
            minValue = 1
            maxValue = 12
        }

        val dayPicker = dialogView.findViewById<NumberPicker>(R.id.dayPicker).apply {
            minValue = 1
            maxValue = 31
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_birthday))
            .setView(dialogView)
            .setPositiveButton(R.string.ok) { _, _ ->
                val selectedYear = yearPicker.value
                val selectedMonth = monthPicker.value
                val selectedDay = dayPicker.value
                val birthday = LocalDate.of(selectedYear, selectedMonth, selectedDay)

                if (isAgeAbove13(birthday)) {
                    onPositiveClickListener(birthday)
                } else {
                    Toast.makeText(requireContext(), "登録するには13歳以上である必要があります。", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun isAgeAbove13(birthday: LocalDate): Boolean {
        val current = LocalDate.now()
        val age = Period.between(birthday, current).years
        return age >= 13
    }
}

