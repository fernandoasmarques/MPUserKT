package br.com.meupedidoapp.meupedidokt.activities

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import br.com.meupedidoapp.meupedidokt.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_principal.*

class PrincipalActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy {FirebaseAuth.getInstance()}
    private val db : FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        /*Handler().post {
            ObjectAnimator.ofFloat(PrincipalActivity_imgToolbar, "translationY",0f).apply {
                duration = 600
                startDelay = 500
                start()
            }

            ObjectAnimator.ofFloat(PrincipalActivity_imgToolbar, "translationX",-20f).apply {
                duration = 600
                startDelay = 500
                start()
            }
        }*/

        Handler().postDelayed({
            ObjectAnimator.ofFloat(PrincipalActivity_imgToolbar, "translationY",0f).apply {
                duration = 600
                start()
            }

            ObjectAnimator.ofFloat(PrincipalActivity_imgToolbar, "translationX",-20f).apply {
                duration = 600
                start()
            }
        }, 1000)

        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        db.firestoreSettings = settings

        with(PrincipalActivity_toolbar){
            background = ResourcesCompat.getDrawable(resources,
                    R.drawable.background_xml_actionbar, null)
            title = ""
        }

        setSupportActionBar(PrincipalActivity_toolbar)
        NavigationUI.setupWithNavController(PrincipalActivity_bottom_navigation, Navigation.findNavController(this, R.id.PrincipalActivity_navHostFragment))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_sair ->{
                mAuth.signOut()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
