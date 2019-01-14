package br.com.meupedidoapp.meupedidokt.activities

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.adapters.ItensPedidoListAdapter
import br.com.meupedidoapp.meupedidokt.model.ItensPedido
import br.com.meupedidoapp.meupedidokt.model.Tema
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_resumo_pedido.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
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

        var subtotal = BigDecimal(0)
        for (itensPedido in itensSelecionados) {
            subtotal = subtotal.add(itensPedido.precoTotal)
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
        //ResumoPedidoActivity_titulo_list_resumo.textColor = Color.parseColor(tema?.corLight)

        ResumoPedidoActivity_background_subtotal.backgroundColor = Color.parseColor(tema?.corPrincipal)


        ItensPedidoListAdapter(this, itensSelecionados, tema!!).apply {
            ResumoPedidoActivity_listView_resumopedido.adapter = this
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
