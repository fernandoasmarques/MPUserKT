package br.com.meupedidoapp.meupedidokt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.model.Categoria
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CategoriaAdapter(options: FirestoreRecyclerOptions<Categoria>, uidLojista: String, pagerProduto: ViewPager):
        FirestoreRecyclerAdapter<Categoria, CategoriaAdapter.CategoriaHolder>(options){

    private val pagerProduto: ViewPager = pagerProduto

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaHolder {
        return CategoriaHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_categoria, parent, false))
    }

    override fun onBindViewHolder(holder: CategoriaHolder, position: Int, model: Categoria) {

        holder.categoriaNome.text = model.nome

        Glide.with(holder.categoriaImagem.context)
                .load(model.imagem)
                .into(holder.categoriaImagem)

        holder.relativelayoutCategoria.setOnClickListener{
            pagerProduto.currentItem = position
        }
    }

    class CategoriaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoriaNome = itemView.findViewById<TextView>(R.id.item_categoria_nome)!!
        val categoriaImagem = itemView.findViewById<ImageView>(R.id.item_categoria_imagem)!!
        val relativelayoutCategoria = itemView.findViewById<RelativeLayout>(R.id.relativelayout_categoria)!!
    }
}
