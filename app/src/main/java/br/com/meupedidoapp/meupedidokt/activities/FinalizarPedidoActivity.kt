package br.com.meupedidoapp.meupedidokt.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.fragments.EscolherPagamentoFragment
import br.com.meupedidoapp.meupedidokt.model.*
import br.com.meupedidoapp.meupedidokt.utils.AndroidUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_finalizar_pedido.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import java.math.BigDecimal
import java.util.ArrayList

class FinalizarPedidoActivity : AppCompatActivity() {

    private lateinit var itensSelecionados: ArrayList<ItensPedido>
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    companion object {
        lateinit var pedidoRealizado: Pedido
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalizar_pedido)

        val args = intent.extras
        //val tema: Tema? by lazy { args?.getParcelable<Tema>("tema") }
        itensSelecionados = args?.getParcelableArrayList<ItensPedido>("itensSelecionados")!!
        val uidLojista by lazy { args.getString("uidLojista") }

        pedidoRealizado = Pedido(FirebaseAuth.getInstance().currentUser?.uid, uidLojista, itensSelecionados)

        window.statusBarColor = ContextCompat.getColor(this@FinalizarPedidoActivity, R.color.colorPrimaryDark)
        //window.navigationBarColor = Color.parseColor(tema?.corStatusBar)

        with(FinalizarPedidoActivity_toolbar) {
            background = ResourcesCompat.getDrawable(resources, R.drawable.background_xml_actionbar, null)
            title = "Endereço e Pagamento"
            setTitleTextAppearance(context, R.style.BebasNeueFont)
            setTitleTextColor(ContextCompat.getColor(this@FinalizarPedidoActivity, R.color.colorAccent))
        }

        setSupportActionBar(FinalizarPedidoActivity_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        /*GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#FFFFFF"), Color.parseColor("#FFC107"))).apply {
            FinalizarPedidoActivity_btnFinalizarPedidoBackground.background = this
        }*/

        FinalizarPedidoActivity_btnFinalizarPedido.setOnClickListener {
            if (EscolherPagamentoFragment.viewSwitcherPagamento.displayedChild == 1) {
                //dinheiro
                val edtTroco = EscolherPagamentoFragment.viewSwitcherPagamento.getChildAt(1).findViewById<TextInputEditText>(R.id.item_pagamento_dinheiro_txtInputLayoutEdtTroco)
                val checkBoxTroco = EscolherPagamentoFragment.viewSwitcherPagamento.getChildAt(1).findViewById<CheckBox>(R.id.item_pagamento_dinheiro_checkboxTroco)

                val pagamentoDinheiro = Pagamento("DINHEIRO", "A_VISTA", BigDecimal(edtTroco.text.toString()))
                val fatura = Fatura(LojistaActivity.somarPrecosItensSelecionados(), LojistaActivity.somarPrecosItensSelecionados(),
                        BigDecimal(0), pagamentoDinheiro)
                pedidoRealizado.fatura = fatura

                saveOnFirestore(pedidoRealizado)

            } else if (EscolherPagamentoFragment.viewSwitcherPagamento.displayedChild == 2) {
                println("O selecionado é Cartão")
            }
        }
    }

    private fun saveOnFirestore(pedido: Pedido){
        if (AndroidUtils.isNetworkAvailable(this)) {
            val loadingDialogMP = LoadingDialogMP(this)
            loadingDialogMP.abrirDialog()
            db.collection("Pedidos").document().set(pedido).addOnCompleteListener {
                if (it.isSuccessful) {
                    loadingDialogMP.fecharDialog()
                } else{
                    toast("Ainda tá dando erro!")
                }
            }
        } else {
            toast("Sem conexão com a internet.")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}
