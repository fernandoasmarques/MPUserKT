package br.com.meupedidoapp.meupedidokt.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.adapters.ProdutoAdapter
import br.com.meupedidoapp.meupedidokt.model.Produto
import br.com.meupedidoapp.meupedidokt.model.Tema
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class ProdutoFragment : Fragment() {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val uidLojista by lazy { arguments?.getString("uidLojista") }
    private val idCategoria by lazy { arguments?.getString("idCategoria") }
    private val tema by lazy { arguments?.getParcelable<Tema>("tema") }

    companion object {
        fun newInstance(uidLojista: String, idCategoria: String, tema: Tema): ProdutoFragment {
            val fragment by lazy { ProdutoFragment() }
            val args by lazy { Bundle() }
            with(args) {
                putString("uidLojista", uidLojista)
                putString("idCategoria", idCategoria)
                putParcelable("tema", tema)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_produto, container, false)

        val recyclerViewProdutos = rootView.findViewById<RecyclerView>(R.id.ProdutoFragment_recyclerView_produtos)
        recyclerViewProdutos.setHasFixedSize(true)
        recyclerViewProdutos.layoutManager = LinearLayoutManager(activity?.baseContext)

        val query = db.collection("Lojista")
                .document(uidLojista!!)
                .collection("Cardapio")
                .document(idCategoria!!)
                .collection("Produto").whereEqualTo("estaAtivo", true)

        val options: FirestoreRecyclerOptions<Produto> by lazy {
            FirestoreRecyclerOptions.Builder<Produto>()
                    .setQuery(query, Produto::class.java)
                    .build()
        }

        ProdutoAdapter(options, tema).apply {
            this.startListening()
            recyclerViewProdutos.adapter = this
        }

        return rootView
    }
}
