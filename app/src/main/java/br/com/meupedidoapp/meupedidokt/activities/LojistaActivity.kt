package br.com.meupedidoapp.meupedidokt.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.adapters.CategoriaAdapter
import br.com.meupedidoapp.meupedidokt.adapters.ItensPedidoListAdapter
import br.com.meupedidoapp.meupedidokt.adapters.ProdutoPagerAdapter
import br.com.meupedidoapp.meupedidokt.model.Categoria
import br.com.meupedidoapp.meupedidokt.model.ItensPedido
import br.com.meupedidoapp.meupedidokt.model.Tema
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_lojista.*
import kotlinx.android.synthetic.main.lojistaactivity_bottomsheet_itenspedidos.*
import java.util.ArrayList

class LojistaActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Este é um Singleton reconhecido em todo o sistema
    // Este array é limpo quando a activity se encerra
    // Pelo fato de ser Singleton pode-se adiconar itens de vários lojistas

    companion object {
        val itensSelecionados: ArrayList<ItensPedido> by lazy {ArrayList<ItensPedido>()}
        lateinit var itensPedidoListAdapter: ItensPedidoListAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lojista)

        //itensSelecionados = ArrayList()

        val args = intent.extras
        val tema: Tema? by lazy {args?.getParcelable<Tema>("tema")}
        val nomeFantasia by lazy {args?.getString("nomeFantasia")}
        val uidLojista by lazy {args?.getString("uidLojista")}

        val pagerProduto = findViewById<ViewPager>(R.id.LojistaActivity_pagerProduto)

        window.statusBarColor = Color.parseColor(tema?.corStatusBar)
        window.navigationBarColor = Color.parseColor(tema?.corStatusBar)

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

        val adapterCategorias = CategoriaAdapter(options, uidLojista, LojistaActivity_pagerProduto)
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

            ProdutoPagerAdapter(fm = supportFragmentManager, qtdeCategorias = idCategorias.size, uidLojista = uidLojista,
                    idCategorias = idCategorias, nomeCategorias = categoriasNome, tema = tema!!).apply {
                pagerProduto.adapter = this
                pagerProduto.offscreenPageLimit = idCategorias.size
            }
        }

        BottomSheetBehavior.from(LojistaActivity_bottomsheet_linearLayoutItensPedidos).apply {
            this.isHideable = false
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        ItensPedidoListAdapter(this, itensSelecionados).apply {
            LojistaActivity_bottomsheet_listItensProdutos.adapter = this
            itensPedidoListAdapter = this
        }

        LojistaActivity_bottomsheet_btnFinalizarPedido.setOnClickListener{
            if(itensSelecionados.isEmpty()){
                Toast.makeText(this, "Seu carrinho está vazio", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, FinalizarPedidoActivity::class.java)
                val params = Bundle()
                params.putParcelableArrayList("itensSelecionados", itensSelecionados)
                params.putString("uidLojista", uidLojista)
                intent.putExtras(params)

                val opts = ActivityOptionsCompat.makeCustomAnimation(it.context, R.anim.slide_in_left, R.anim.slide_out_left)
                ActivityCompat.startActivity(this, intent, opts.toBundle())
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
