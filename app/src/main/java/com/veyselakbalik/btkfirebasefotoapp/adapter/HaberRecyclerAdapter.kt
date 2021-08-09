package com.veyselakbalik.btkfirebasefotoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.veyselakbalik.btkfirebasefotoapp.R
import com.veyselakbalik.btkfirebasefotoapp.model.Post
import kotlinx.android.synthetic.main.recycler_row.view.*

class HaberRecyclerAdapter(val postList : ArrayList<Post>) : RecyclerView.Adapter<HaberRecyclerAdapter.PostHolder>(){
    class PostHolder(itemview : View) : RecyclerView.ViewHolder(itemview){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.recycler_row_kullanici_email.text = postList[position].kullaniciEmail
        holder.itemView.recycler_row_kullanici_yorumu.text = postList[position].kullaniciYorum
        Picasso.get().load(postList[position].gorselUrl).into(holder.itemView.recycler_row_imageview)

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}