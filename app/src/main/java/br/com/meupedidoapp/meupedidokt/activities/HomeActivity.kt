package br.com.meupedidoapp.meupedidokt.activities

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity as Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import br.com.meupedidoapp.meupedidokt.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


class HomeActivity : Activity(), View.OnClickListener {
    private lateinit var mCallbackManager: CallbackManager
    private lateinit var mAuth: FirebaseAuth

    companion object {
        private lateinit var homeActivity: HomeActivity
        fun getInstance(): HomeActivity{
            return homeActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_home)
        super.onCreate(savedInstanceState)

        val height = this.resources.displayMetrics.heightPixels/2.3

        Handler().postDelayed({
            ObjectAnimator.ofFloat(HomeActivity_imgLogo, "translationY", -height.toFloat()).apply {
                duration = 1000
                start()
            }
        }, 500)

        HomeActivity_btnEntrar.setOnClickListener(this)
        HomeActivity_btnEntrarFacebook.setOnClickListener(this)
        HomeActivity_btnCadastrar.setOnClickListener(this)
        homeActivity = this

        mAuth = FirebaseAuth.getInstance()
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                result?.accessToken?.let { handleFacebookAccessToken(accessToken = it) }
                //if (result != null) handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                mostrarToast("Login cancelado")
            }

            override fun onError(error: FacebookException?) {
                mostrarToast("Login cancelado!")
                println("Deu erro porque: ${error.toString()}")
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.HomeActivity_btnEntrar ->
                startActivity(Intent(this, LoginClienteActivity::class.java))
            R.id.HomeActivity_btnEntrarFacebook ->
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            R.id.HomeActivity_btnCadastrar ->
                startActivity(Intent(this, CadClienteActivity::class.java))
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken) {
        val loadingDialogMP = LoadingDialogMP(this)
        loadingDialogMP.abrirDialog()

        val credential: AuthCredential = FacebookAuthProvider.getCredential(accessToken.token)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) {
            if (it.isSuccessful){
                updateUI(mAuth.currentUser)
                loadingDialogMP.fecharDialog()
            } else {
                loadingDialogMP.fecharDialog()
                updateUI(null)
                mostrarToast("Login cancelado!")
                try {
                    throw it.exception!!
                } catch (e: FacebookException) {
                    print("A mensagem é: $e")
                }
            }
        }
    }

    private fun mostrarToast(texto: String) = Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()

}

