package com.example.lab2

import User
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {
    private lateinit var dbHelper: SQLDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = SQLDatabase(this)

        val loginButton: Button = findViewById(R.id.loginButton)
        val createButton: Button = findViewById(R.id.createButton)

        val textInfo : TextView = findViewById(R.id.textInfo)

        val login : EditText = findViewById(R.id.login)
        val password : EditText = findViewById(R.id.password)

        loginButton.setOnClickListener {
            if (login.text == null || login.text.toString() == "" ||
                password.text == null || password.text.toString() == "")
            {
                textInfo.setText("Логин и/или пароль пусты!")
                return@setOnClickListener
            }
            if (!loginDataCorrect(login.text.toString(), password.text.toString())) {
                textInfo.text = "Неверный пользователь или пароль"
                return@setOnClickListener
            }
            val intent = Intent(this, PostsActivity::class.java)
            intent.putExtra("username", login.text.toString())
            startActivity(intent)
        }

        createButton.setOnClickListener {
            if (login.text == null || login.text.toString() == "" ||
                password.text == null || password.text.toString() == "")
            {
                textInfo.setText("Логин и/или пароль пусты!")
                return@setOnClickListener
            }

            if (findUserInDb(login.text.toString()))
            {
                textInfo.text = "Такое имя уже есть!"
                return@setOnClickListener
            }
            val new_user = User(login.text.toString(), password.text.toString())
            insertUser(new_user)
            textInfo.text = "Добавлен пользователь ${login.text}!"
        }
    }

    fun insertUser(user : User) {
        val db = dbHelper.writableDatabase
        val insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)"
        val statement = db.compileStatement(insertSQL)
        statement.bindString(1, user.username)
        statement.bindString(2, user.password)
        statement.executeInsert()
        statement.close()
        db.close()
    }
    fun findUserInDb(username : String) : Boolean
    {
        val db = dbHelper.readableDatabase
        val selectSQL = "SELECT username FROM users WHERE username = ?"
        val cursor = db.rawQuery(selectSQL, arrayOf(username))
        if (!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return false
        }
        cursor.close()
        db.close()
        return true
    }
    fun loginDataCorrect(username: String, password: String) : Boolean
    {
        val db = dbHelper.readableDatabase
        val selectSQL = "SELECT username FROM users WHERE username = ? AND password = ?"
        val cursor = db.rawQuery(selectSQL, arrayOf(username, password))
        if (!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return false
        }
        cursor.close()
        db.close()
        return true
    }
}