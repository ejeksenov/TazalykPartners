package kz.nextstep.tazalykpartners.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.*
import kz.nextstep.tazalykpartners.R

class CustomAddWasteItemDialog(context: Context, val wasteId: String) : Dialog(context) {

    companion object {
        var onItemClick: ((String, Double) -> Unit)? = null
    }

    private val wasteIdList = wasteId.split(";").toMutableList()
    var wasteTypeList: MutableList<String> = ArrayList()

    lateinit var btnAddWasteItemDialogClose: ImageButton
    lateinit var spAddWasteItemDialogPassedTrashId: Spinner
    lateinit var edtAddWasteItemDialogPassedTrashTotal: EditText
    lateinit var btnAddWasteItemDialogAdd: Button

    var selectedWasteTypeIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_waste_item_dialog)

        btnAddWasteItemDialogAdd = findViewById(R.id.btn_add_waste_item_dialog_add)
        btnAddWasteItemDialogClose = findViewById(R.id.btn_add_waste_item_dialog_close)
        spAddWasteItemDialogPassedTrashId = findViewById(R.id.sp_add_waste_item_dialog_passed_trash_id)
        edtAddWasteItemDialogPassedTrashTotal = findViewById(R.id.edt_add_waste_item_dialog_passed_trash_total)

        edtAddWasteItemDialogPassedTrashTotal.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(6, 3))

        for (item in wasteIdList) {
            val itemArr = item.split(",")
            if (itemArr.size >= 3)
                wasteTypeList.add(itemArr[1])
        }

        val wasteTypeArr = wasteTypeList.toTypedArray()
        val adapterId = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, wasteTypeArr)
        adapterId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spAddWasteItemDialogPassedTrashId.adapter = adapterId

        spAddWasteItemDialogPassedTrashId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedWasteTypeIndex = position
            }

        }

        btnAddWasteItemDialogAdd.setOnClickListener {
            edtAddWasteItemDialogPassedTrashTotal.error = null
            val totalStr = edtAddWasteItemDialogPassedTrashTotal.text.toString()
            if (totalStr.isNotBlank() && totalStr.toDouble() > 0) {
                val total = totalStr.toDouble()
                onItemClick!!.invoke(wasteIdList[selectedWasteTypeIndex], total)
                dismiss()
            } else
                edtAddWasteItemDialogPassedTrashTotal.error =
                    context.resources.getString(R.string.enter_total_amount_of_kg)
        }

        btnAddWasteItemDialogClose.setOnClickListener {
            dismiss()
        }
    }
}