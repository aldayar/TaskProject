package com.example.taskproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskproject.databinding.ItemRcViewBinding
import com.example.taskproject.model.Task

class TaskAdapter(
    private val onTaskDone: (Task) -> Unit,
    private val onTaskRemoved: (Task) -> Unit,
    private val onTaskEdit: (Task) -> Unit
) :
    androidx.recyclerview.widget.ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffUtil()) {


    inner class TaskViewHolder(private var binding: ItemRcViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, onNoteDone: (Task) -> Unit, onNoteRemoved: (Task) -> Unit) {
            binding.itemTvTitle.text = task.title
            binding.itemTvDesc.text = task.desc
            binding.isDone.isChecked = task.isDone

            binding.isDone.setOnCheckedChangeListener { _, isDone ->
                task.isDone = isDone
                onNoteDone(task)
            }
            itemView.setOnLongClickListener {
                onNoteRemoved(task)
                true
            }
            itemView.setOnClickListener {
                onTaskEdit(task)
            }
        }

    }

    class TaskDiffUtil : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskViewHolder(
        ItemRcViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskPosition = getItem(position)
        holder.bind(taskPosition, onTaskDone, onTaskRemoved)
    }
}