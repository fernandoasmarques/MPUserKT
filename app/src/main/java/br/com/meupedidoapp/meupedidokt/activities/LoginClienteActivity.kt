package br.com.meupedidoapp.meupedidokt.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import br.com.meupedidoapp.meupedidokt.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login_cliente.*
import java.lang.Exception

class LoginClienteActivity : AppCompatActivity(), View.OnClickListener {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val loadingDialog: LoadingDialogMP by lazy { LoadingDialogMP(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_cliente)

        with(supportActionBar) {
            title = "Entrar"
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.background_xml_actionbar, null))
        }

        LoginClienteActitivy_txtInputLayoutSenha.isPasswordVisibilityToggleEnabled = true
        LoginClienteActitivy_txtInputLayoutEdtSenha.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitForm()
                true
            }
            false
        }
        LoginClienteActitivy_btnCadastrar.setOnClickListener(this)
        LoginClienteActitivy_txtResetarSenha.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.LoginClienteActitivy_btnCadastrar ->
                submitForm()
            R.id.LoginClienteActitivy_txtResetarSenha -> {
                val builder = AlertDialog.Builder(this)
                val layout = LinearLayout(this)
                layout.layoutParams = (LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT))
                layout.setPadding(30, 0, 30, 0)

                val edittext = EditText(this)
                with(edittext) {
                    inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    hint = "E-mail"
                    textSize = 15f
                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                layout.addView(edittext)

                with(builder) {
                    setTitle("Digite seu e-mail")
                    setMessage("Será enviado um e-mail de redefinição de senha.")
                    setView(layout)

                    setNegativeButton(android.R.string.cancel, null)
                    setPositiveButton(android.R.string.ok) { _, _ ->
                        resetarSenha(edittext.text.toString().trim())
                    }
                }

                val dialog = builder.create()
                dialog.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                edittext.requestFocus()

                edittext.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled)
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
                        else
                            edittext.error = "Digite seu e-mail"
                        true
                    }
                    false
                }

                edittext.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        edittext.error = null
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = s.toString().trim().isValidEmail
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
        }
    }

    private fun submitForm() {
        if (!validarEmail()) {
            return
        } else if (!validarSenha()) {
            return
        } else {
            val email = LoginClienteActitivy_txtInputLayoutEdtEmail.text.toString().trim()
            val senha = LoginClienteActitivy_txtInputLayoutEdtSenha.text.toString()

            loadingDialog.abrirDialog()

            mAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            loadingDialog.fecharDialog()
                            val user: FirebaseUser? = mAuth.currentUser
                            updateUI(user)
                        } else {
                            if (loadingDialog.estaAberto())
                                loadingDialog.fecharDialog()

                            try {
                                throw it.exception!!
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                mostrarAlerta("Oops!", "Sua senha está incorreta.")
                                LoginClienteActitivy_txtInputLayoutEdtSenha.requestFocus()
                                LoginClienteActitivy_txtInputLayoutSenha.error = "Senha incorreta. Digite novamente."
                            } catch (e: FirebaseAuthInvalidUserException) {
                                mostrarAlerta("Conta inexistente",
                                        "Este e-mail ($email) não está cadastrado no aplicativo Meu Pedido")
                                LoginClienteActitivy_txtInputLayoutEdtEmail.requestFocus()
                            } catch (e: FirebaseNetworkException) {
                                mostrarAlerta("Oops! :(", "Não há conexão com a internet.")
                            } catch (e: Exception) {
                                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
        }
    }

    private fun validarEmail(): Boolean {
        val email = LoginClienteActitivy_txtInputLayoutEdtEmail.text.toString().trim()
        if (!email.isValidEmail) {
            LoginClienteActitivy_txtInputLayoutEmail.error = "Digite um e-mail válido"
            LoginClienteActitivy_txtInputLayoutEdtEmail.requestFocus()
            return false
        } else {
            LoginClienteActitivy_txtInputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validarSenha(): Boolean {
        val senha = LoginClienteActitivy_txtInputLayoutEdtSenha.text.toString().trim()
        if (senha.isEmpty() || senha.length < 6) {
            LoginClienteActitivy_txtInputLayoutSenha.error = "Digite uma senha com pelo menos 6 dígitos"
            LoginClienteActitivy_txtInputLayoutEdtSenha.requestFocus()
            return false
        } else {
            LoginClienteActitivy_txtInputLayoutSenha.isErrorEnabled = false
        }
        return true
    }

    private fun resetarSenha(email: String) {
        val loading = LoadingDialogMP(this)
        loading.abrirDialog()
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        loading.fecharDialog()
                        mostrarAlerta("E-mail enviado",
                                "Entre no seu e-mail e siga as instruções para a redefinição da senha.")
                    } else {
                        if (loading.estaAberto()) loading.fecharDialog()
                        try {
                            throw it.exception!!
                        } catch (e: FirebaseAuthInvalidUserException) {
                            mostrarAlerta("Conta inexistente!",
                                    "Este e-mail ($email) não está cadastrado no aplicativo Meu Pedido")
                        } catch (e: FirebaseNetworkException) {
                            mostrarAlerta("Oops! :(", "Não há conexão com a internet.")
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            mostrarAlerta("E-mail errado",
                                    "O email foi digitado incorretamente. Digite novamente.")
                        } catch (e: Exception) {
                            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            HomeActivity.getInstance().finish()
            finish()
        }
    }

    private fun mostrarAlerta(titulo: String, mensagem: String) {
        val alert = AlertDialog.Builder(this)
        with(alert) {
            setTitle(titulo)
            setMessage(mensagem)
            setNeutralButton(android.R.string.ok, null)
            show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private val String.isValidEmail: Boolean
        get() = !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
