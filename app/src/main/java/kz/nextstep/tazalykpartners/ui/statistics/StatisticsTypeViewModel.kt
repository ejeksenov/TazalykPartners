package kz.nextstep.tazalykpartners.ui.statistics

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.utils.data.WasteItem

class StatisticsTypeViewModel: ViewModel() {
    private val selectedWasteType = MutableLiveData<String>()
    private val selectedWasteId = MutableLiveData<String>()
    private val logoWasteId = MutableLiveData<String>()
    private val passedTotal = MutableLiveData<String>()

    fun bind(wasteItem: WasteItem, context: Context) {
        selectedWasteType.value = wasteItem.selected_waste_type
        selectedWasteId.value = wasteItem.selected_waste_id
        passedTotal.value =  "${wasteItem.passed_total} кг"
        logoWasteId.value = onGetDrawableId(wasteItem.selected_waste_id, context)

    }

    private fun onGetDrawableId(selectedWasteId: String?, context: Context): String? {
        var logoWasteImageId = ""
        val wasteIdArray = context.resources.getStringArray(R.array.waste_id)
        for (wasteIdItem in wasteIdArray) {
            if (wasteIdItem.contains(selectedWasteId!!)) {
                val wasteIdItemArr = wasteIdItem.split(",")
                if (wasteIdItemArr.size >= 5) {
                    logoWasteImageId = wasteIdItemArr[3]
                }
            }
        }
        return logoWasteImageId
    }

    fun getSelectedWasteId() = selectedWasteId
    fun getPassedTotal() = passedTotal
    fun getLogoWasteId() = logoWasteId
}