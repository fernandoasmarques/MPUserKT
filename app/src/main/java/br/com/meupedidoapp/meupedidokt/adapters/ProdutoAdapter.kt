package br.com.meupedidoapp.meupedidokt.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.activities.LojistaActivity
import br.com.meupedidoapp.meupedidokt.model.ItensPedido
import br.com.meupedidoapp.meupedidokt.model.Produto
import br.com.meupedidoapp.meupedidokt.model.Tema
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.math.BigDecimal
import java.util.ArrayList

class ProdutoAdapter(options: FirestoreRecyclerOptions<Produto>, private val tema: Tema?) : FirestoreRecyclerAdapter<Produto, ProdutoAdapter.ProdutoHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoHolder {
        return ProdutoHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_produto, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProdutoHolder, position: Int, model: Produto) {
        Glide.with(holder.imgImagemProduto.context)
                .load(model.imagem)
                .into(holder.imgImagemProduto)

        holder.txtProdutoNome.text = model.nome
        val ingredientList = ArrayList<String>()
        ingredientList.addAll(model.ingredientes)

        var ingredientes = ""
        when {
            ingredientList.size > 1 -> {
                ingredientList.removeAt(ingredientList.size - 1)
                ingredientes = TextUtils.join(", ", ingredientList) + " e " + model.ingredientes[model.ingredientes.size - 1] + "."
            }
            ingredientList.size == 1 -> ingredientes = model.ingredientes[0] + "."
            ingredientList.size == 0 -> holder.txtProdutoIngredientes.visibility = View.GONE
        }

        holder.txtProdutoIngredientes.text = ingredientes

        val valorProduto = model.preco.toInt()
        val subpreco = model.preco.remainder(BigDecimal.ONE).movePointRight(2).abs().toInt()

        if (model.tipo != "unidade")
            holder.txtProdutoPreco.text = "A partir de " + valorProduto.toString()
        else {
            holder.txtProdutoPreco.text = valorProduto.toString()
            if (model.preco.toDouble() - valorProduto != 0.0)
                holder.txtProdutoSubPreco.text = "." + subpreco.toString()
            else
                holder.txtProdutoSubPreco.text = ".00"
        }


        holder.relativeProdutoFooter.setBackgroundColor(Color.parseColor(tema?.corLight))
        holder.relativeBackgroundProdutoPreco.background.setColorFilter(Color.parseColor(tema?.corPrincipal), PorterDuff.Mode.SRC_IN)

        /*val snapshot = snapshots.getSnapshot(holder.adapterPosition)
        val idProduto = snapshot.id
        val preco = BigDecimal(snapshot.getDouble("preco").toString())
        val nome = snapshot.getString("nome")*/

        holder.btnCounterQtdeProdutoMais.setOnClickListener {
            holder.adicionarProduto(model)

            if (!LojistaActivity.itensSelecionados.contains(holder.itensPedido))
                LojistaActivity.itensSelecionados.add(holder.itensPedido)

            LojistaActivity.itensPedidoListAdapter.notifyDataSetChanged()
        }

        holder.btnCounterQtdeProdutoMenos.setOnClickListener {
            holder.retirarItem()

            if (holder.getQtdeProduto() == 0)
                LojistaActivity.itensSelecionados.remove(holder.itensPedido)

            LojistaActivity.itensPedidoListAdapter.notifyDataSetChanged()
        }
    }

    class ProdutoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtProdutoNome = itemView.findViewById<TextView>(R.id.itemProduto_txtProdutoNome)!!
        val txtProdutoIngredientes = itemView.findViewById<TextView>(R.id.itemProduto_txtProdutoIngredientes)!!
        val txtProdutoPreco = itemView.findViewById<TextView>(R.id.itemProduto_txtProdutoPreco)!!
        val txtProdutoSubPreco = itemView.findViewById<TextView>(R.id.itemProduto_txtProdutoSubPreco)!!
        val imgImagemProduto = itemView.findViewById<ImageView>(R.id.itemProduto_imagemProduto)!!
        val txtCifrao = itemView.findViewById<TextView>(R.id.itemProduto_txtCifrao)!!
        val relativeProdutoFooter = itemView.findViewById<LinearLayout>(R.id.itemProduto_relativeProdutoFooter)!!
        val relativeBackgroundProdutoPreco = itemView.findViewById<RelativeLayout>(R.id.itemProduto_relativeBackgroundProdutoPreco)!!
        val btnCounterQtdeProdutoMenos = itemView.findViewById<Button>(R.id.itemProduto_btnCounterQtdeProdutoMenos)!!
        val btnCounterQtdeProdutoMais = itemView.findViewById<Button>(R.id.itemProduto_btnCounterQtdeProdutoMais)!!
        private val txtCounterQtdeProduto = itemView.findViewById<TextView>(R.id.itemProduto_txtCounterQtdeProduto)!!

        private var qtdeProduto: Int = 0
        val itensPedido = ItensPedido()

        fun adicionarProduto(produto: Produto) {
            setQtdeProduto(getQtdeProduto() + 1)
            itensPedido.produto = produto
            itensPedido.quantidade = getQtdeProduto()
            mostrarQtdeProduto(getQtdeProduto())
        }

        fun retirarItem() {
            setQtdeProduto(getQtdeProduto() - 1)
            itensPedido.quantidade = getQtdeProduto()
            mostrarQtdeProduto(getQtdeProduto())
        }

        private fun setQtdeProduto(qtdeProduto: Int) {
            if (qtdeProduto >= 0) {
                this.qtdeProduto = qtdeProduto
            }
        }

        fun getQtdeProduto(): Int {
            return qtdeProduto
        }

        private fun mostrarQtdeProduto(numero: Int) {
            txtCounterQtdeProduto.text = numero.toString()
        }
    }
}