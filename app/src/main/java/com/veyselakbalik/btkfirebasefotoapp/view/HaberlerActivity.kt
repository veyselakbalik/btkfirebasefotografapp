package com.veyselakbalik.btkfirebasefotoapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.veyselakbalik.btkfirebasefotoapp.model.Post
import com.veyselakbalik.btkfirebasefotoapp.R
import com.veyselakbalik.btkfirebasefotoapp.adapter.HaberRecyclerAdapter
import kotlinx.android.synthetic.main.activity_haberler.*

class HaberlerActivity : AppCompatActivity() {
    private lateinit var database : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var recyclerViewAdapter : HaberRecyclerAdapter

    var postListesi = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        verileriAl()

        var layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerViewAdapter = HaberRecyclerAdapter(postListesi)
        recyclerview.adapter = recyclerViewAdapter



    }

    fun verileriAl(){
        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else {
                if (snapshot != null){
                    if (!snapshot.isEmpty){

                        val documents = snapshot.documents

                        postListesi.clear()

                        for (document in documents){
                            val kullaniciEmail = document.get("kullaniciemail") as String
                            val kullaniciYorum = document.get("kullaniciyorum") as String
                            val gorselUrl = document.get("gorselUrl") as String

                            val indirilenPost = Post(kullaniciEmail,kullaniciYorum,gorselUrl)
                            postListesi.add(indirilenPost)
                        }
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }


            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.secenekler_menusu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.fotograf_paylas){
            //foto payla??ma aktivite inflate
            val intent = Intent(this, FotografPaylasmaActivity::class.java)
            startActivity(intent)
        }else if (item.itemId == R.id.cikis_yap){
            auth.signOut()
            val intent = Intent(this, KullaniciActivity::class.java)
            startActivity(intent)
            finish()
        }


        return super.onOptionsItemSelected(item)
    }
}