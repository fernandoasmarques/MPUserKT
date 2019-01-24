package br.com.meupedidoapp.meupedidokt.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.adapters.EnderecoAdapter
import br.com.meupedidoapp.meupedidokt.adapters.LojistaAdapter
import br.com.meupedidoapp.meupedidokt.model.Endereco
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class EscolherEnderecoFragment : Fragment() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var enderecoAdapter: EnderecoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_escolher_endereco, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.EscolherEnderecoFragment_recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity?.baseContext)

        val collectionEndereco: CollectionReference by lazy {
            db.collection("Cliente")
                    .document(mAuth.currentUser!!.uid).collection("Endereco")
        }
        val query: Query by lazy { collectionEndereco.whereEqualTo("enderecoAtivo", true) }


        EnderecoAdapter(FirestoreRecyclerOptions.Builder<Endereco>()
                .setQuery(query, Endereco::class.java)
                .build()).apply {
            this.startListening()
            recyclerView.adapter = this
            enderecoAdapter = this
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        enderecoAdapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        enderecoAdapter.stopListening()
    }
}
