package com.example.store

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

class ProductAdapter(private val context: Context, private val list: List<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val row = layoutInflater.inflate(R.layout.items_layout, parent, false)
        val button = row.findViewById<Button>(R.id.button3)
        button.setOnClickListener{
            val intent = Intent(context, Cart::class.java)
            context.startActivity(intent)
        }

        row.findViewById<TextView>(R.id.name).text = list[position]

        return row
    }
}