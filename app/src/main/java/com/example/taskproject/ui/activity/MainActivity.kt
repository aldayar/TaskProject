package com.example.taskproject.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.taskproject.adapter.TaskAdapter
import com.example.taskproject.databinding.ActivityMainBinding
import com.example.taskproject.model.Task
import com.example.taskproject.ui.fragments.AddFragment
import com.example.taskproject.ui.fragments.EditFragment
import com.example.taskproject.viewModel.TaskViewModel

class MainActivity : AppCompatActivity(), AddFragment.OnNoteAddedListener, EditFragment.OnNoteEditedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        Toast.makeText(this, "hold to delete", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "swipe to left or right to edit", Toast.LENGTH_SHORT).show()

        initAdapter()
        addTask()
        filterTask()
        swipeAbleTasks()

        taskViewModel.allTasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
        }
        
    }
    private fun initAdapter() {
        taskAdapter = TaskAdapter(
            onTaskDone = { task -> taskViewModel.updateTask(task) },
            onTaskRemoved = { task -> alertToDelete(task) },
            onTaskEdit = {task ->showEditFragment(task)  }
        )
        binding.recycler.adapter = taskAdapter
    }

    private fun addTask() {
        binding.btnAdd.setOnClickListener {
            showAddDialog()
        }
    }
    private fun filterTask(){
        binding.filterBtn.setOnClickListener {
            filterTasks()
        }
    }
    private fun showAddDialog() {
        val addFragment = AddFragment(this)
        addFragment.show(supportFragmentManager, "AddFragment")
    }

    override fun onNoteAdded(title: String, desc: String) {
        val task = Task(title = title, desc = desc)
        taskViewModel.addTask(task)
    }

    private fun alertToDelete(task: Task) {
        val alertDialog =
            AlertDialog.Builder(this).setTitle("Warning.").setMessage("Confirm to remove")
                .setPositiveButton("Ok") { _, _ ->
                    taskViewModel.deleteTask(task)
                }.setNegativeButton("Cancel", null)
        alertDialog.show()

    }

    private fun filterTasks() {
        val alertDialog = AlertDialog.Builder(this).setTitle("Filter tasks")
            .setPositiveButton("Filter by done") { _, _ ->
                filterTasksByStatus(true)
            }.setNegativeButton("Filter by undone") {_,_->
                filterTasksByStatus(false)
            }.setNeutralButton("Cansel", null)
        alertDialog.show()
    }

    private fun filterTasksByStatus(isCompleted: Boolean) {
        val filteredTasks = if (isCompleted) {
            taskViewModel.getTasksByCompletedStatus(true)
        } else {
            taskViewModel.getTasksByCompletedStatus(false)
        }

        filteredTasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
        }
    }
    private fun showEditFragment(task: Task) {
        val editFragment = EditFragment(task, this)
        editFragment.show(supportFragmentManager, "TAG")
    }

    override fun onNoteEdited(task: Task, newTitle: String, newDesc: String) {
        task.title = newTitle
        task.desc = newDesc
        taskViewModel.updateTask(task)
    }
    private fun  swipeAbleTasks(){
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Swipe directions
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = taskAdapter.currentList[viewHolder.adapterPosition]
                showEditFragment(task)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }
}