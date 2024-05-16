package com.example.lab2

import Post
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ManualAdapter(context: Context, dataArrayList: ArrayList<Post?>?) :
    ArrayAdapter<Post>(context, R.layout.adapter, dataArrayList!!){

    override fun getView(position: Int, view: View?, parent: ViewGroup) :
            View{
        var view = view
        val customList = getItem(position)

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.adapter,
                parent, false)
        }

        view?.findViewById<TextView>(R.id.postText)?.text = customList?.text.toString()
        if (customList != null) {
            view?.findViewById<TextView>(R.id.username)?.text  = customList.user
        }
        if (customList != null) {
            view?.findViewById<TextView>(R.id.amountOfLikes)?.text  = customList.likes.toString()
        }

        return view!!
    }

}