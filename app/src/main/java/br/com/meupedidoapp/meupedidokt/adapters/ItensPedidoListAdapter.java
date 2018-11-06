package br.com.meupedidoapp.meupedidokt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import br.com.meupedidoapp.meupedidokt.R;
import br.com.meupedidoapp.meupedidokt.model.ItensPedido;

public final class ItensPedidoListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ItensPedido> itensPedidos;

    public ItensPedidoListAdapter(Context context, ArrayList<ItensPedido> itensPedidos) {
        this.context = context;
        this.itensPedidos = itensPedidos;
    }

    @Override
    public int getCount() {
        return itensPedidos.size();
    }

    @Override
    public Object getItem(int position) {
        return itensPedidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItensPedido itensPedido = itensPedidos.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_itens_selecionados, null);
            holder = new ViewHolder();
            holder.txtItemProdutoQtde = (TextView) convertView.findViewById(R.id.txtItemProdutoQtde);
            holder.txtItemProdutoNome = (TextView) convertView.findViewById(R.id.txtItemProdutoNome);
            holder.txtItemProdutoPreco = (TextView) convertView.findViewById(R.id.txtItemProdutoPreco);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtItemProdutoNome.setText(itensPedido.getNome());
        holder.txtItemProdutoQtde.setText(String.valueOf(itensPedido.getQuantidade()));
        holder.txtItemProdutoPreco.setText(String.valueOf(new BigDecimal(itensPedido.getPrecoTotal())));

        return convertView;
    }

    public final static class ViewHolder {
        TextView txtItemProdutoQtde;
        TextView txtItemProdutoNome;
        TextView txtItemProdutoPreco;
    }
}
