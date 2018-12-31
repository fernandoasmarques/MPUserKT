package br.com.meupedidoapp.meupedidokt.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import java.math.BigDecimal
import java.util.ArrayList

import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.model.ItensPedido

class ItensPedidoListAdapter(private val context: Context, private val itensPedidos: ArrayList<ItensPedido>) : BaseAdapter() {

    override fun getCount(): Int {
        return itensPedidos.size
    }

    override fun getItem(position: Int): Any {
        return itensPedidos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val itensPedido = itensPedidos[position]
        val holder: ViewHolder

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_itens_selecionados, null)
            holder = ViewHolder()
            holder.txtItemProdutoQtde = convertView.findViewById<View>(R.id.txtItemProdutoQtde) as TextView
            holder.txtItemProdutoNome = convertView.findViewById<View>(R.id.txtItemProdutoNome) as TextView
            holder.txtItemProdutoPreco = convertView.findViewById<View>(R.id.txtItemProdutoPreco) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.txtItemProdutoNome.text = itensPedido.produto.nome
        holder.txtItemProdutoQtde.text = itensPedido.quantidade.toString()
        holder.txtItemProdutoPreco.text = itensPedido.precoTotal.toString()

        return convertView
    }

    class ViewHolder {
        internal lateinit var txtItemProdutoNome: TextView
        internal lateinit var txtItemProdutoQtde: TextView
        internal lateinit var txtItemProdutoPreco: TextView
    }
}
