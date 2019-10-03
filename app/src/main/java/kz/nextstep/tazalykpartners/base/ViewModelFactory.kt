package kz.nextstep.tazalykpartners.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.ui.AddPinViewModel
import javax.inject.Inject

open class ViewModelFactory (private val mainApplication: MainApplication): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddPinViewModel::class.java)) {
            AddPinViewModel(mainApplication) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}