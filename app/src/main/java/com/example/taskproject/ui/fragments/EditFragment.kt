package com.example.taskproject.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.taskproject.databinding.FragmentEditBinding
import com.example.taskproject.model.Task


class EditFragment(private val task: Task, private val listener: OnNoteEditedListener) :
    DialogFragment() {
    private lateinit var binding: FragmentEditBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        binding = FragmentEditBinding.inflate(inflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root).setTitle("Edit Task").setPositiveButton("Edit") { _, _ ->
            val newTitle = binding.etTitle.text.toString()
            val newDesc = binding.etDesc.text.toString()
            if (newTitle.isNotBlank() && newDesc.isNotBlank()) {
                listener.onNoteEdited(task, newTitle, newDesc)
            }
        }.setNegativeButton("Cancel") { _, _ ->
            dialog?.cancel()
        }
        // отвечает за старых данных который ввел пользователь
        binding.etTitle.setText(task.title)
        binding.etDesc.setText(task.desc)

        return builder.create()
    }
    interface OnNoteEditedListener {
        fun onNoteEdited(task: Task, newTitle: String, newDesc: String)
    }
}