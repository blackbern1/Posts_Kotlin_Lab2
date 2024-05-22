package com.example.lab2

import Post
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class ManualAdapter(context: Context, dataArrayList: ArrayList<Post?>?, username: String?) :
    ArrayAdapter<Post>(context, R.layout.adapter, dataArrayList!!) {

    private val sqlDatabase = SQLDatabase(context)
    private val user = username

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.adapter, parent, false)

        val post = getItem(position)
        val postTextView = view.findViewById<TextView>(R.id.postText)
        val idPostTextView = view.findViewById<TextView>(R.id.idPost)
        val usernameTextView = view.findViewById<TextView>(R.id.username)
        val amountOfLikesTextView = view.findViewById<TextView>(R.id.amountOfLikes)
        val likeButton = view.findViewById<Button>(R.id.likeButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        post?.let {
            postTextView.text = it.text
            idPostTextView.text = it.id.toString()
            usernameTextView.text = it.user
            amountOfLikesTextView.text = it.likes.toString()

            likeButton.setOnClickListener {
                post.let {
                    if (!it.liked) {
                        it.likes += 1
                        it.liked = true
                        likeButton.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.lightgreen
                            )
                        )
                    } else {
                        it.likes -= 1
                        it.liked = false
                        likeButton.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                    }
                    amountOfLikesTextView.text = it.likes.toString()
                    updateLikesOnPost(it.id, it.likes)
                }
            }

            if (it.liked) {
                likeButton.setBackgroundColor(ContextCompat.getColor(context, R.color.lightgreen))
            } else {
                likeButton.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            if (it.user == user) {
                it.highlighted = true
                usernameTextView.text = "You (" + user + ")"
            } else {
                it.highlighted = false
            }

            if (it.highlighted) {
                usernameTextView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.purple_200))
            }

            deleteButton.setOnClickListener {
                post.let {
                    if (it.user == user) {
                        deletePost(it.id)
                        remove(it)
                        notifyDataSetChanged()
                    }
                }
            }
        }

        return view
    }

    private fun updateLikesOnPost(id: Int, newLike: Int) {
        val db = sqlDatabase.writableDatabase
        val updateSQL = "UPDATE posts SET likes = ? WHERE id = ?"
        val statement = db.compileStatement(updateSQL)
        statement.bindLong(1, newLike.toLong())
        statement.bindLong(2, id.toLong())
        statement.executeUpdateDelete()
        statement.close()
        db.close()
    }

    private fun deletePost(id:Int) {
        val db = sqlDatabase.writableDatabase
        val deleteSQL = "DELETE FROM posts WHERE id = ?"
        val statement = db.compileStatement(deleteSQL)
        statement.bindLong(1, id.toLong())
        statement.executeUpdateDelete()
        statement.close()
        db.close()
    }
}