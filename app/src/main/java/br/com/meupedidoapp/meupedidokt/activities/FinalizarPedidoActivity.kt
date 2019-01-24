package br.com.meupedidoapp.meupedidokt.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.model.ItensPedido
import br.com.meupedidoapp.meupedidokt.model.Tema
import kotlinx.android.synthetic.main.activity_finalizar_pedido.*
import java.util.ArrayList

class FinalizarPedidoActivity : AppCompatActivity() {

    private lateinit var itensSelecionados: ArrayList<ItensPedido>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalizar_pedido)

        val args = intent.extras
        val tema: Tema? by lazy { args?.getParcelable<Tema>("tema") }
        itensSelecionados = args?.getParcelableArrayList<ItensPedido>("itensSelecionados")!!
        val uidLojista by lazy { args.getString("uidLojista") }

        window.statusBarColor = Color.parseColor(tema?.corStatusBar)
        window.navigationBarColor = Color.parseColor(tema?.corStatusBar)

        with(FinalizarPedidoActivity_toolbar) {
            background = ColorDrawable(Color.parseColor(tema?.corPrincipal))
            title = "Pagamento e Endereço"
            setTitleTextAppearance(context, R.style.BebasNeueFont)
            setTitleTextColor(Color.parseColor(tema?.corFonte))
        }

        setSupportActionBar(FinalizarPedidoActivity_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
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
