package br.com.meupedidoapp.meupedidokt.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity(){
    private val context: Context get() = this
    private val mAuth by lazy {FirebaseAuth.getInstance()}

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
        finish()
    }

    private fun updateUI(user: FirebaseUser?){
        lateinit var intent: Intent
        if(user != null){
            intent = Intent(context, PrincipalActivity::class.java)
            startActivity(intent)
        }else{
            intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}

