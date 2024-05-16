package com.example.lab2

import Post
import User
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {

    private var usersArray = mutableListOf<User>()
    private var postsArray = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.loginButton)
        val createButton: Button = findViewById(R.id.createButton)

        val textInfo : TextView = findViewById(R.id.textInfo)

        val login : EditText = findViewById(R.id.login)
        val password : EditText = findViewById(R.id.password)

        loginButton.setOnClickListener {
            if (login.text == null || login.text.toString() == "" ||
                password.text == null || password.text.toString() == "")
            {
                textInfo.setText("Логин или пароль пусты!")
                return@setOnClickListener
            }
//            if (usersArray.isEmpty()) {
//                textInfo.text = "Такого имени нет!"
//            }
            for (user in usersArray)
            {
                if (user.username != login.text.toString())
                {
                    continue
                }
                if (user.password != password.text.toString()) {
                    textInfo.text = "Неверный пароль"
                    return@setOnClickListener
                } else {
                    val intent = Intent(this, PostsActivity::class.java)
                    intent.putExtra("username", user.username)
                    startActivity(intent)
                    return@setOnClickListener
                }
            }
            textInfo.text = "Неверный пользователь"
        }

        createButton.setOnClickListener {
            if (login.text == null || login.text.toString() == "" ||
                password.text == null || password.text.toString() == "")
            {
                textInfo.setText("Логин или пароль пусты!")
                return@setOnClickListener
            }

            if (usersArray.isNotEmpty()) {
                for (user in usersArray)
                {
                    if (user.username == login.text.toString()) {
                        textInfo.text = "Такое имя уже есть!"
                        return@setOnClickListener
                    }
                }
            }
            usersArray.add(User(login.text.toString(), password.text.toString()))
            textInfo.text = "Добавлен пользователь ${login.text}!"
        }
    }
}