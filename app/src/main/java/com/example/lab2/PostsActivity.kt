package com.example.lab2

import Post
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.ComponentActivity
import android.widget.TextView
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView

class PostsActivity : ComponentActivity() {
    private lateinit var dbHelper: SQLDatabase
    private var arr = ArrayList<Post?>()
    private lateinit var manualAdapter: ManualAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        dbHelper = SQLDatabase(this)

        val username = intent?.getStringExtra("username")

        val greetingMessage: TextView = findViewById(R.id.greetingMessage)
        val exitButton: Button = findViewById(R.id.exitButton)
        val postText: EditText = findViewById(R.id.postText)
        val sendPostButton: Button = findViewById(R.id.sendPostButton)
        val listOfPosts: ListView = findViewById(R.id.listOfPosts)
        val scrollView: ScrollView = findViewById(R.id.scrollView)

        val searchNameField: EditText = findViewById(R.id.findNameField)
        val searchButton: Button = findViewById(R.id.findButton)
        val sortBelowButton: Button = findViewById(R.id.sortBelowButton)
        val sortAboveButton: Button = findViewById(R.id.sortAboveButton)
        val stashButton: Button = findViewById(R.id.stashButton)

        arr = showPosts()
        manualAdapter = ManualAdapter(this@PostsActivity, arr, username)
        listOfPosts.adapter = manualAdapter

        greetingMessage.text = "Привет, ${username}!"

        hideKeyboard(searchNameField)

        searchButton.setOnClickListener {
            hideKeyboard(searchButton)
            if (searchNameField.text.toString().isEmpty()) {
                arr.clear()
                arr += showPosts()
                manualAdapter.notifyDataSetChanged()
                return@setOnClickListener
            }
            arr.clear()
            arr += findByUsername(searchNameField.text.toString())
            manualAdapter.notifyDataSetChanged()
        }

        sortBelowButton.setOnClickListener {
            arr.clear()
            arr += sortByLikes(-1)
            manualAdapter.notifyDataSetChanged()
        }

        sortAboveButton.setOnClickListener {
            arr.clear()
            arr += sortByLikes(1)
            manualAdapter.notifyDataSetChanged()
        }

        stashButton.setOnClickListener {
            arr.clear()
            arr += sortByLikes(0)
            manualAdapter.notifyDataSetChanged()
        }

        sendPostButton.setOnClickListener {
            if (postText.text.isNullOrEmpty()) {
                return@setOnClickListener
            }
            val post = Post(postText.text.toString(), username ?: "undefined", 0)
            post.id = insertPost(post)
            arr.add(post)
            manualAdapter.notifyDataSetChanged()

            postText.setText("")
            hideKeyboard(sendPostButton)
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }

        exitButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun insertPost(post: Post): Int {
        val db = dbHelper.writableDatabase
        val insertSQL = "INSERT INTO posts (username, text, likes) VALUES (?, ?, ?)"
        val statement = db.compileStatement(insertSQL)
        statement.bindString(1, post.user)
        statement.bindString(2, post.text)
        statement.bindLong(3, post.likes.toLong())
        statement.executeInsert()
        val selectIdSQL = "SELECT id FROM posts ORDER BY id DESC LIMIT 1"
        val cursor = db.rawQuery(selectIdSQL, null)
        var id = 0
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        statement.close()
        db.close()
        return id
    }

    private fun showPosts(): ArrayList<Post?> {
        val list = ArrayList<Post?>()
        val db = dbHelper.readableDatabase
        val selectSQL = "SELECT * FROM posts"
        val cursor = db.rawQuery(selectSQL, null)
        if (cursor.moveToFirst()) {
            do {
                val post = Post(
                    cursor.getString(cursor.getColumnIndexOrThrow("text")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("likes"))
                )
                post.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                list.add(post)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    private fun findByUsername(username:String) : ArrayList<Post?> {
        val list = ArrayList<Post?>()
        val db = dbHelper.readableDatabase
        val selectSQL = "SELECT * FROM posts WHERE username = ?"
        val cursor = db.rawQuery(selectSQL, arrayOf(username))
        if (cursor.moveToFirst()) {
            do {
                val post = Post(
                    cursor.getString(cursor.getColumnIndexOrThrow("text")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("likes"))
                )
                post.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                list.add(post)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    private fun sortByLikes(option: Int) : ArrayList<Post?> {
        if      (option == 0) {
            return showPosts()
        }
        val list = ArrayList<Post?>()
        val db = dbHelper.readableDatabase
        val selectSQL =
            if (option < 0) {
                "SELECT * FROM posts ORDER BY likes"
            } else {
                "SELECT * FROM posts ORDER BY likes DESC"
        }
        val cursor = db.rawQuery(selectSQL, null)
        if (cursor.moveToFirst()) {
            do {
                val post = Post(
                    cursor.getString(cursor.getColumnIndexOrThrow("text")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("likes"))
                )
                post.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                list.add(post)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}