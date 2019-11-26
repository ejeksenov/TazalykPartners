package kz.nextstep.tazalykpartners.utils

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import kz.nextstep.tazalykpartners.R


class CustomProgressBar(context: Context) : AlertDialog(context) {

    init {
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

    override fun show() {
        super.show()
        setContentView(R.layout.custom_progressbar)
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun dismiss() {
        super.dismiss()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}