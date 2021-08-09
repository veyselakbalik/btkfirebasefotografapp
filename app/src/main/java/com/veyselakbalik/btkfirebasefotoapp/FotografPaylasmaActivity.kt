package com.veyselakbalik.btkfirebasefotoapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fotograf_paylasma.*
import java.util.*

class FotografPaylasmaActivity : AppCompatActivity() {
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


    }

    fun paylas(view: View){
        //depo işlemleri
        //UUID -> universal unique id

        val uuid = UUID.randomUUID()
        val gorselIsmi = "${uuid}.jpg"

        val reference = storage.reference

        val gorselReference = reference.child("images").child(gorselIsmi)

        if (secilenGorsel != null){
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener {
                println("yüklendi")
            }
        }

    }
    fun gorselSec(view: View){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //izin alınmamışken yapılacaklar
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        } else {
            //izin zaten varsa yapılacaklar
            val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(
                galeriIntent,
                2
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verilince yapılacaklar
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(
                    galeriIntent,
                    2
                )
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){

            secilenGorsel = data.data

            if (secilenGorsel != null){
                if (Build.VERSION.SDK_INT >= 28){

                    val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(secilenBitmap)

                } else {

                }

                secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                imageView.setImageBitmap(secilenBitmap)
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}