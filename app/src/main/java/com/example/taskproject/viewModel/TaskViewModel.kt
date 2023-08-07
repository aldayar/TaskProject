    package com.example.taskproject.viewModel

    import android.app.Application
    import androidx.lifecycle.AndroidViewModel
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.viewModelScope
    import com.example.taskproject.model.Task
    import com.example.taskproject.room.TaskDao
    import com.example.taskproject.room.TaskDataBase
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch

    class TaskViewModel(application: Application) : AndroidViewModel(application) {
        private val taskDao: TaskDao = TaskDataBase.getInstance(application).taskDao()

        val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

        fun addTask(task: Task) {
            viewModelScope.launch(Dispatchers.IO) {
                taskDao.insert(task)
            }
        }

        fun updateTask(task: Task) {
            viewModelScope.launch(Dispatchers.IO) {
                taskDao.update(task)
            }
        }

        fun deleteTask(task: Task) {
            viewModelScope.launch(Dispatchers.IO) {
                taskDao.delete(task)
            }
        }

        fun getTasksByCompletedStatus(isCompleted: Boolean): LiveData<List<Task>> {
            return taskDao.getTasksByCompletedStatus(isCompleted)
        }

    }