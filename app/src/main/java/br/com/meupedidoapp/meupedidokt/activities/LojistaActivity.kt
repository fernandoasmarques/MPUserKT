package br.com.meupedidoapp.meupedidokt.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.adapters.CategoriaAdapter
import br.com.meupedidoapp.meupedidokt.adapters.ItensPedidoListAdapter
import br.com.meupedidoapp.meupedidokt.adapters.ProdutoPagerAdapter
import br.com.meupedidoapp.meupedidokt.model.Categoria
import br.com.meupedidoapp.meupedidokt.model.ItensPedido
import br.com.meupedidoapp.meupedidokt.model.Tema
import br.com.meupedidoapp.meupedidokt.utils.ItensPedidoListener
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_lojista.*
import kotlinx.android.synthetic.main.lojistaactivity_bottomsheet_itenspedidos.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.function.BiFunction

class LojistaActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    // Este é um Singleton reconhecido em todo o sistema
    // Este array é limpo quando a activity se encerra
    // Pelo fato de ser Singleton pode-se adiconar itens de vários lojistas

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var bottomsheet_txtSubtotal : TextView
        val itensSelecionados by lazy { ObservableArrayList<ItensPedido>() }
        @SuppressLint("StaticFieldLeak")
        lateinit var itensPedidoListAdapter: ItensPedidoListAdapter

        fun somarPrecosItensSelecionados() : BigDecimal{
            var soma = BigDecimal("0")
            for(itenspedido in itensSelecionados){
                soma = soma.add(itenspedido.precoTotal)
            }
            return soma
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lojista)
        bottomsheet_txtSubtotal = findViewById<TextView>(R.id.LojistaActivity_bottomsheet_txtItemProdutoSubTotal)

        val args = intent.extras
        val tema: Tema? by lazy { args?.getParcelable<Tema>("tema") }
        val nomeFantasia by lazy { args?.getString("nomeFantasia") }
        val uidLojista by lazy { args?.getString("uidLojista") }

        val pagerProduto = findViewById<ViewPager>(R.id.LojistaActivity_pagerProduto)

        window.statusBarColor = Color.parseColor(tema?.corStatusBar)
        window.navigationBarColor = Color.parseColor(tema?.corStatusBar)

        //LojistaActivity_bottomsheet_btnFinalizarPedido.background =

        with(LojistaActivity_toolbar) {
            background = ColorDrawable(Color.parseColor(tema?.corPrincipal))
            title = nomeFantasia
            setTitleTextAppearance(context, R.style.BebasNeueFont)
            setSubtitleTextAppearance(context, R.style.GoogleSansFont)
            setTitleTextColor(Color.parseColor(tema?.corFonte))
        }

        setSupportActionBar(LojistaActivity_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        LojistaActivity_recyclerView_categorias.setBackgroundColor(Color.parseColor(tema?.corLight))
        LojistaActivity_recyclerView_categorias.setHasFixedSize(true)
        LojistaActivity_recyclerView_categorias.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)

        val cardapioRef = db.collection("Lojista").document(uidLojista!!).collection("Cardapio")
        val ordemCategoria = cardapioRef.orderBy("ordem", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Categoria>()
                .setQuery(ordemCategoria, Categoria::class.java)
                .build()

        val adapterCategorias = CategoriaAdapter(options, uidLojista!!, LojistaActivity_pagerProduto)
        LojistaActivity_recyclerView_categorias.adapter = adapterCategorias
        adapterCategorias.startListening()


        ordemCategoria.get().addOnCompleteListener {
            val idCategorias = ArrayList<String>()
            val categoriasNome = ArrayList<String>()
            if (it.isSuccessful) {
                for (document in it.result!!) {
                    idCategorias.add(document.id)
                    categoriasNome.add(document.get("nome")!!.toString())
                }
            }

            ProdutoPagerAdapter(fm = supportFragmentManager, qtdeCategorias = idCategorias.size, uidLojista = uidLojista!!,
                    idCategorias = idCategorias, nomeCategorias = categoriasNome, tema = tema!!).apply {
                pagerProduto.adapter = this
                pagerProduto.offscreenPageLimit = idCategorias.size
            }
        }


        ItensPedidoListAdapter(this, itensSelecionados, tema!!).apply {
            LojistaActivity_bottomsheet_listItensProdutos.adapter = this
            itensPedidoListAdapter = this
        }


        val bottomSheetBehavior = BottomSheetBehavior.from(LojistaActivity_bottomsheet_linearLayoutItensPedidos)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
                LojistaActivity_bottomsheet_imgseta.rotation = p1 * 180f
                //LojistaActivity_bottomsheet_btnVisualizarProdutos.animate().rotationXBy(180f).start()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                println("onStateChanged: $newState")
            }
        })

        // callback para verificar a atividade do ArrayList itensSelecionados
        itensSelecionados.addOnListChangedCallback(ItensPedidoListener(itensSelecionados, bottomSheetBehavior))

        LojistaActivity_bottomsheet_btnFinalizarPedido.setOnClickListener {
            if (itensSelecionados.isEmpty()) {
                Toast.makeText(this, "Seu carrinho está vazio", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ResumoPedidoActivity::class.java)
                Bundle().apply {
                    this.putParcelableArrayList("itensSelecionados", itensSelecionados)
                    this.putString("uidLojista", uidLojista)
                    this.putParcelable("tema", tema)
                    intent.putExtras(this)
                }

                val opts = ActivityOptionsCompat.makeCustomAnimation(it.context, R.anim.slide_in_left, R.anim.slide_out_left)
                ActivityCompat.startActivity(this, intent, opts.toBundle())
            }
        }

        LojistaActivity_bottomsheet_txtItemProdutoSubTotal.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        itensSelecionados.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_lojista, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnInstagramLojista -> {
                val uri = Uri.parse("http://instagram.com/_u/totalburgeripora")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.instagram.android")
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/totalburgeripora")))
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
