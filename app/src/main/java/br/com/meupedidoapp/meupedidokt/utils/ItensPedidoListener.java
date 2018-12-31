package br.com.meupedidoapp.meupedidokt.utils;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.util.ArrayList;
import androidx.databinding.ObservableList;
import br.com.meupedidoapp.meupedidokt.model.ItensPedido;

public class ItensPedidoListener extends ObservableList.OnListChangedCallback {

    private ArrayList<ItensPedido> itensSelecionados;
    private BottomSheetBehavior bottomSheetBehavior;

    public ItensPedidoListener(ArrayList<ItensPedido> itens, BottomSheetBehavior bottomSheet) {
        this.itensSelecionados = itens;
        this.bottomSheetBehavior = bottomSheet;
    }

    @Override
    public void onChanged(ObservableList sender) {
    }

    @Override
    public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
    }

    @Override
    public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
        bottomSheetBehavior.setHideable(false);
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
    }

    @Override
    public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
        if (itensSelecionados.size() == 0) {
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }
}
