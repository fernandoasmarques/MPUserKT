package br.com.meupedidoapp.meupedidokt.activities

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import br.com.meupedidoapp.meupedidokt.R
import kotlinx.android.synthetic.main.loading_dialog.*

class LoadingDialogMP(context: Context) : Dialog(context) {
    private var estaAberto: Boolean = false
    private var anim: AnimationDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        anim = AnimationDrawable::class.java.cast(animated_img.drawable)
        with(anim!!){
            setEnterFadeDuration(250)
            setExitFadeDuration(250)
            start()
        }
    }

    fun abrirDialog(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        show()
        estaAberto = true
    }

    fun fecharDialog(){
        super.dismiss()
        estaAberto = false
        anim!!.stop()
    }

    fun estaAberto(): Boolean{
        return this.estaAberto
    }
}
