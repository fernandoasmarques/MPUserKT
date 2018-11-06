package br.com.meupedidoapp.meupedidokt.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.model.Cliente
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.activity_cad_cliente.*
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import java.lang.Exception
import java.text.DateFormat
import java.util.*

class CadClienteActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, View.OnClickListener, View.OnFocusChangeListener {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val dataPTBR: DateFormat by lazy { DateFormat.getDateInstance(DateFormat.MEDIUM, Locale("pt", "BR")) }
    private var dataNascimentoTimeStamp: Long? = null
    private val loadingDialogMP by lazy { LoadingDialogMP(this) }
    private val alert: AlertDialog.Builder by lazy { AlertDialog.Builder(this) }
    private lateinit var cliente: Cliente
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad_cliente)

        with(supportActionBar) {
            title = "Cadastre-se"
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.background_xml_actionbar, null))
        }

        CadClienteActitivy_txtInputLayoutEdtDtNascimento.setOnClickListener(this)
        CadClienteActitivy_btnCadastrar.setOnClickListener(this)
        CadClienteActitivy_txtInputLayoutSenha.isPasswordVisibilityToggleEnabled = true
        CadClienteActitivy_txtInputLayoutEdtDtNascimento.onFocusChangeListener = this

        CadClienteActitivy_txtInputLayoutEdtTelefone1Celular.addTextChangedListener(object : TextWatcher {
            var isUpdating = false
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }
                var str = s.toString().replace("[)]".toRegex(), "")
                        .replace("[(]".toRegex(), "")
                        .replace("[ ]".toRegex(), "")
                        .replace("[-]".toRegex(), "")

                if (count > before) {
                    when {
                        str.length > 7 ->
                            str = "(" + str.substring(0, 2) + ") " + str.substring(2, 3) + " " + str.substring(3, 7) + "-" + str.substring(7)

                        str.length == 3 ->
                            str = "(" + str.substring(0, 2) + ") " + str.substring(2, 3)

                        str.length in 4..7 ->
                            str = "(" + str.substring(0, 2) + ") " + str.substring(2, 3) + " " + str.substring(3)

                        str.length == 1 ->
                            str = "(" + str.substring(0)

                        str.length == 2 ->
                            str = "(" + str.substring(0, 2) + ") "
                    }
                    isUpdating = true
                    with(CadClienteActitivy_txtInputLayoutEdtTelefone1Celular) {
                        setText(str)
                        text?.length?.let { setSelection(it) }
                    }
                } else {
                    when {
                        str.length > 7 ->
                            str = "(" + str.substring(0, 2) + ") " + str.substring(2, 3) + " " + str.substring(3, 7) + "-" + str.substring(7)

                        str.length == 3 ->
                            str = "(" + str.substring(0, 2) + ") " + str.substring(2, 3)

                        str.length in 4..7 ->
                            str = "(" + str.substring(0, 2) + ") " +
                                    str.substring(2, 3) + " " + str.substring(3)

                        str.length == 1 ->
                            str = "(" + str.substring(0)

                        str.length == 2 ->
                            str = "(" + str.substring(0, 2) + ") "
                    }

                    isUpdating = true
                    with(CadClienteActitivy_txtInputLayoutEdtTelefone1Celular) {
                        setText(str)
                        setSelection(Math.min(start, str.length))
                    }
                }
            }
        })

        CadClienteActitivy_txtInputLayoutEdtTelefone2Fixo.addTextChangedListener(object : TextWatcher {
            var isUpdating = false
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                var str = s.toString().replace("[)]".toRegex(), "")
                        .replace("[(]".toRegex(), "")
                        .replace("[ ]".toRegex(), "")
                        .replace("[-]".toRegex(), "")

                if (count > before) {
                    when {
                        str.length > 6 ->
                            str = "(" + str.substring(0, 2) + ") " + str.substring(2, 6) + "-" + str.substring(6)

                        str.length in 3..6 ->
                            str = "(" + str.substring(0, 2) + ") " + str.substring(2, 3) + str.substring(3)

                        str.length == 1 ->
                            str = "(" + str.substring(0)

                        str.length == 2 ->
                            str = "(" + str.substring(0, 2) + ") "
                    }
                    isUpdating = true
                    with(CadClienteActitivy_txtInputLayoutEdtTelefone2Fixo) {
                        setText(str)
                        text?.length?.let { setSelection(it) }
                    }
                } else {
                    when {
                        str.length > 6 ->
                            str = "(" + str.substring(0, 2) + ") " + str.substring(2, 6) + "-" + str.substring(6)

                        str.length in 3..6 ->
                            str = "(" + str.substring(0, 2) + ") " + str.substring(2, 3) + str.substring(3)

                        str.length == 1 ->
                            str = "(" + str.substring(0)

                        str.length == 2 ->
                            str = "(" + str.substring(0, 2) + ") "
                    }

                    isUpdating = true
                    with(CadClienteActitivy_txtInputLayoutEdtTelefone2Fixo) {
                        setText(str)
                        setSelection(Math.min(start, str.length))
                    }
                }
            }
        })

        CadClienteActitivy_txtInputLayoutSenha.helperText = "Digite uma senha com pelo menos 6 dígitos"

        CadClienteActitivy_txtInputLayoutEdtSenha.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitForm()
                true
            }
            false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.CadClienteActitivy_txtInputLayoutEdtDtNascimento ->
                mostrarData()
            R.id.CadClienteActitivy_btnCadastrar ->
                submitForm()
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.CadClienteActitivy_txtInputLayoutEdtDtNascimento ->
                if (hasFocus) mostrarData()
        }
    }

    private fun submitForm() {
        if (!validarNome()) {
            return
        } else if (!validarSobrenome()) {
            return
        } else if (!validarDtNasc()) {
            return
        } else if (!validarGenero()) {
            return
        } else if (!validarTelefoneCelular()) {
            return
        } else if (!validarTelefoneFixo()) {
            return
        } else if (!validarEmail()) {
            return
        } else if (!validarSenha()) {
            return
        } else {
            val nome = CadClienteActitivy_txtInputLayoutEdtNome.text.toString().trim()
            val sobrenome = CadClienteActitivy_txtInputLayoutEdtSobrenome.text.toString().trim()
            val genero = if (CadClienteActitivy_rbGeneroMasc.isChecked) "M" else "F"
            val email = CadClienteActitivy_txtInputLayoutEdtEmail.text.toString().trim()
            val senha = CadClienteActitivy_txtInputLayoutEdtSenha.text.toString()

            val telefoneCelular = CadClienteActitivy_txtInputLayoutEdtTelefone1Celular.text.toString().replace("[)]".toRegex(), "")
                    .replace("[(]".toRegex(), "")
                    .replace("[ ]".toRegex(), "")
                    .replace("[-]".toRegex(), "")

            val telefoneFixo = if (CadClienteActitivy_txtInputLayoutEdtTelefone2Fixo.text.toString().isEmpty())
                "N"
            else
                CadClienteActitivy_txtInputLayoutEdtTelefone2Fixo.getText().toString().replace("[)]".toRegex(), "")
                        .replace("[(]".toRegex(), "")
                        .replace("[ ]".toRegex(), "")
                        .replace("[-]".toRegex(), "")

            loadingDialogMP.abrirDialog()

            mAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            loadingDialogMP.fecharDialog()
                            user = mAuth.currentUser!!
                            cliente = Cliente(user!!.uid, nome, email, sobrenome, genero, dataNascimentoTimeStamp!!, telefoneCelular, telefoneFixo)
                            saveOnFirestore(cliente)
                            //sendEmailVerification();
                            updateUI(user)
                        } else {
                            if (loadingDialogMP!!.estaAberto()) loadingDialogMP!!.fecharDialog()
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseNetworkException) {
                                with(alert) {
                                    this!!.setTitle("Oops! :(")
                                    setMessage("Não há conexão com a internet.")
                                    setNeutralButton(android.R.string.ok, null)
                                    show()
                                }
                            } catch (e: FirebaseAuthUserCollisionException) {
                                with(alert) {
                                    this!!.setTitle("Oops! :(")
                                    setMessage("Este endereço de e-mail ($email) já existe em uma conta cadastrada. Tente outro e-mail.")
                                    setNeutralButton(android.R.string.ok, null)
                                    show()
                                }
                                CadClienteActitivy_txtInputLayoutEdtEmail.requestFocus()
                                CadClienteActitivy_txtInputLayoutEmail.error = "Digite outro e-mail"
                            } catch (e: Exception) {
                                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
        }
    }

    private fun validarNome(): Boolean {
        if (CadClienteActitivy_txtInputLayoutEdtNome.text.toString().trim().isEmpty()) {
            CadClienteActitivy_txtInputLayoutNome.error = "Digite seu nome"
            CadClienteActitivy_txtInputLayoutEdtNome.requestFocus()
            return false
        } else {
            CadClienteActitivy_txtInputLayoutNome.isErrorEnabled = false
        }
        return true
    }

    private fun validarSobrenome(): Boolean {
        if (CadClienteActitivy_txtInputLayoutEdtSobrenome.text.toString().trim().isEmpty()) {
            CadClienteActitivy_txtInputLayoutSobrenome.error = "Digite seu sobrenome"
            CadClienteActitivy_txtInputLayoutEdtSobrenome.requestFocus()
            return false
        } else {
            CadClienteActitivy_txtInputLayoutSobrenome.isErrorEnabled = false
        }
        return true
    }

    private fun validarDtNasc(): Boolean {
        if (CadClienteActitivy_txtInputLayoutEdtDtNascimento.text.toString().isEmpty()) {
            CadClienteActitivy_txtInputLayoutDtNascimento.error = "Selecione a data de nascimento"
            CadClienteActitivy_txtInputLayoutEdtDtNascimento.requestFocus()
            return false
        } else {
            CadClienteActitivy_txtInputLayoutDtNascimento.error = null
        }
        return true
    }

    private fun validarGenero(): Boolean {
        if (!CadClienteActitivy_rbGeneroMasc.isChecked && !CadClienteActitivy_rbGeneroFem.isChecked) {
            textViewGenero.text = "Selecione o sexo"
            textViewGenero.setTextColor(Color.RED)
            CadClienteActitivy_rbGeneroMasc.requestFocus()
            return false
        } else {
            textViewGenero.text = "Gênero"
            textViewGenero.setTextColor(Color.GRAY)
        }
        return true
    }

    private fun validarTelefoneCelular(): Boolean {
        when {
            CadClienteActitivy_txtInputLayoutEdtTelefone1Celular.text.toString().isEmpty() -> {
                CadClienteActitivy_txtInputLayoutTelefone1Celular.error = "Digite o número do seu celular"
                CadClienteActitivy_txtInputLayoutEdtTelefone1Celular.requestFocus()
                return false
            }
            CadClienteActitivy_txtInputLayoutEdtTelefone1Celular.text.toString().length < 16 -> {
                CadClienteActitivy_txtInputLayoutTelefone1Celular.error = "Número incompleto"
                CadClienteActitivy_txtInputLayoutEdtTelefone1Celular.requestFocus()
                return false
            }
            else -> CadClienteActitivy_txtInputLayoutTelefone1Celular.isErrorEnabled = false
        }
        return true
    }

    private fun validarTelefoneFixo(): Boolean {
        when {
            CadClienteActitivy_txtInputLayoutEdtTelefone2Fixo.text.toString().isEmpty() -> {
                CadClienteActitivy_txtInputLayoutTelefone2Fixo.isErrorEnabled = false
                return true
            }
            CadClienteActitivy_txtInputLayoutEdtTelefone2Fixo.text.toString().length < 14 -> {
                CadClienteActitivy_txtInputLayoutTelefone2Fixo.error = "Número incompleto"
                CadClienteActitivy_txtInputLayoutEdtTelefone2Fixo.requestFocus()
                return false
            }
            else -> CadClienteActitivy_txtInputLayoutTelefone2Fixo.isErrorEnabled = false
        }
        return true
    }

    private fun validarEmail(): Boolean {
        val email = CadClienteActitivy_txtInputLayoutEdtEmail.text.toString().trim()
        if (!email.isValidEmail) {
            CadClienteActitivy_txtInputLayoutEmail.error = "Digite um e-mail válido"
            CadClienteActitivy_txtInputLayoutEdtEmail.requestFocus()
            return false
        } else {
            CadClienteActitivy_txtInputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validarSenha(): Boolean {
        val senha = CadClienteActitivy_txtInputLayoutEdtSenha.text.toString().trim()
        if (senha.isEmpty() || senha.length < 6) {
            CadClienteActitivy_txtInputLayoutSenha.error = "Digite uma senha com pelo menos 6 dígitos"
            CadClienteActitivy_txtInputLayoutEdtSenha.requestFocus()
            return false
        } else {
            CadClienteActitivy_txtInputLayoutSenha.isErrorEnabled = false
        }
        return true
    }

    private val String.isValidEmail: Boolean
        get() = !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = 0
        calendario.set(year, monthOfYear, dayOfMonth)
        val dataEscolhida: Date = calendario.time
        val dataFormatada = dataPTBR.format(dataEscolhida)
        CadClienteActitivy_txtInputLayoutEdtDtNascimento.setText(dataFormatada)
        dataNascimentoTimeStamp = calendario.timeInMillis / 1000
        //A Type notated with ! is called platform type, which is a type coming from Java and thus can most probably be null.
    }

    private fun mostrarData() {
        SpinnerDatePickerDialogBuilder()
                .context(this)
                .callback(this)
                .spinnerTheme(R.style.DatePickerStyle)
                .defaultDate(2000, 0, 1)
                .minDate(1910, 0, 1)
                .maxDate(2007, 0, 1)
                .build()
                .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun sendEmailVerification() {
        user = mAuth.currentUser!!
        user!!.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                mostrarToast("Verification email sent to ${user!!.email}")
            } else {
                mostrarToast("Failed to send verification email.")
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

    private fun saveOnFirestore(cliente: Cliente) {
        db.collection("Cliente").document(cliente.uid).set(cliente)
    }

    private fun mostrarToast(texto: String) = Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
}