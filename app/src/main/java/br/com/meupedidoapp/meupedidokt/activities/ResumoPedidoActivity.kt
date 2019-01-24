package br.com.meupedidoapp.meupedidokt.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.adapters.ItensPedidoListAdapter
import br.com.meupedidoapp.meupedidokt.model.ItensPedido
import br.com.meupedidoapp.meupedidokt.model.Tema
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.activity_resumo_pedido.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.textColor
import org.jetbrains.anko.uiThread
import java.math.BigDecimal
import java.util.*

class ResumoPedidoActivity : AppCompatActivity() {

    private val user = FirebaseAuth.getInstance().currentUser
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var itensSelecionados: ArrayList<ItensPedido>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumo_pedido)

        val args = intent.extras
        val tema: Tema? by lazy { args?.getParcelable<Tema>("tema") }
        itensSelecionados = args?.getParcelableArrayList<ItensPedido>("itensSelecionados")!!
        val uidLojista by lazy { args.getString("uidLojista") }

        window.statusBarColor = Color.parseColor(tema?.corStatusBar)
        window.navigationBarColor = Color.parseColor(tema?.corStatusBar)

        var totalProdutos = BigDecimal("0")
        for(itenspedido in LojistaActivity.itensSelecionados){
            totalProdutos = totalProdutos.add(itenspedido.precoTotal)
        }

        with(ResumoPedidoActivity_toolbar) {
            background = ColorDrawable(Color.parseColor(tema?.corPrincipal))
            title = "Concluir pedido"
            //subtitle = "Subtotal: R$${String.format(Locale.getDefault(), "%.2f", subtotal)}"
            setTitleTextAppearance(context, R.style.BebasNeueFont)
            //setSubtitleTextAppearance(context, R.style.GoogleSansFont)
            setTitleTextColor(Color.parseColor(tema?.corFonte))
        }

        setSupportActionBar(ResumoPedidoActivity_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        println("O tamanho da lista é: " + itensSelecionados.size)
        ResumoPedidoActivity_titulo_list_resumo.background.setColorFilter(Color.parseColor(tema?.corPrincipal), PorterDuff.Mode.SRC_IN)
        ResumoPedidoActivity_background_total.backgroundColor = Color.parseColor("#FFC107")
        ResumoPedidoActivity_titulo_list_resumo_background.backgroundColor = Color.parseColor(tema?.corPrincipal)
        ResumoPedidoActivity_btnFinalizarPedido.backgroundColor = Color.parseColor(tema?.corPrincipal)

       GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
               intArrayOf(android.R.color.transparent, Color.parseColor("#FFC107"))).apply {
           ResumoPedidoActivity_background_gradient_total.background = this
       }

        ItensPedidoListAdapter(this, itensSelecionados, tema!!).apply {
            ResumoPedidoActivity_listView_resumopedido.adapter = this
        }

        ResumoPedidoActivity_txt_total_preco.text = totalProdutos.setScale(2).toString() + "R$"
        ResumoPedidoActivity_txt_total_preco_background.text = totalProdutos.setScale(2).toString() + "R$"

        doAsync {
            uiThread {
                Handler().post{
                    ObjectAnimator.ofFloat(ResumoPedidoActivity_txt_total_preco_background, "translationY", 0f).apply {
                        duration = 700
                        start()
                    }
                }
            }
        }

        ResumoPedidoActivity_btnFinalizarPedido.setOnClickListener{
            val intent = Intent(this, FinalizarPedidoActivity::class.java)
            Bundle().apply {
                this.putParcelableArrayList("itensSelecionados",itensSelecionados)
                this.putString(uidLojista, uidLojista)
                this.putParcelable("tema",tema)
                intent.putExtras(this)
            }
            val opts = ActivityOptionsCompat.makeCustomAnimation(it.context, R.anim.slide_in_left, R.anim.slide_out_left)
            ActivityCompat.startActivity(this, intent, opts.toBundle())
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
