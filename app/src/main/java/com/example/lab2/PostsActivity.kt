package com.example.lab2

import Post
import User
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import android.widget.TextView

class PostsActivity : ComponentActivity() {
    private var arr = ArrayList<Post?>()
    private lateinit var manualAdapter: ManualAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        val username = intent?.getStringExtra("username")

        val isLiked = false

        val greetingMessage : TextView = findViewById(R.id.greetingMessage)
        val exitButton : Button = findViewById(R.id.exitButton)
        val postText : EditText = findViewById(R.id.postText)
        val sendPostButton : Button = findViewById(R.id.sendPostButton)

        val listOfPosts : ListView = findViewById(R.id.listOfPosts)

        arr.add(Post("Хорошая погодка!", "Dmitr", 33))
        arr.add(Post("Пробежал сегодня 20 км", "IvanSolo", 20))
        arr.add(Post("Сегодня сделал лабораторные по языку Kotlin!!!", "SleepyDog", 1578))

        manualAdapter = ManualAdapter(this@PostsActivity, arr)
        listOfPosts.setAdapter(manualAdapter)

        greetingMessage.text = "Привет, ${username}!"

        sendPostButton.setOnClickListener {
            if (postText.text == null || postText.text.toString() == "")
            {
                return@setOnClickListener
            }
            arr.add(Post(postText.text.toString(), username ?: "undefined ", 0))

            manualAdapter = ManualAdapter(this@PostsActivity, arr)
            listOfPosts.setAdapter(manualAdapter)
        }
        exitButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}