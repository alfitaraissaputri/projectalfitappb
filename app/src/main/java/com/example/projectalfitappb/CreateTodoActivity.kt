package com.example.projectalfitappb

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.projectalfitappb.antity.Todo
import com.example.projectalfitappb.databinding.ActivityCreateTodoBinding
import com.example.projectalfitappb.usecase.TodoUseCase
import kotlinx.coroutines.launch

class CreateTodoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTodoBinding
    private lateinit var todoUseCase: TodoUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCreateTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        todoUseCase = TodoUseCase()
        registerEvents()
    }

    fun registerEvents() {
        binding.tombolTambah.setOnClickListener {
            saveDataToFirestore()
        }
    }

    private fun saveDataToFirestore() {
        val todo = Todo(
            id = "",
            title = binding.title.text.toString(),
            description = binding.description.text.toString()
        )

        lifecycleScope.launch {
            todoUseCase.createTodo(todo)

            Toast.makeText(this@CreateTodoActivity, "Data berhasil ditambahkan", Toast.LENGTH_LONG).show()


            toTodoListActivity()
        }
    }

    private fun toTodoListActivity() {
        val intent = Intent(this, Textactivity::class.java)
        startActivity(intent)
        finish()
    }
}