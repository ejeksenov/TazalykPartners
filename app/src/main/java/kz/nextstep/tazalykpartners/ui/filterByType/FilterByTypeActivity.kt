package kz.nextstep.tazalykpartners.ui.filterByType

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity

class FilterByTypeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    private lateinit var gvFilterByType: GridView
    private lateinit var rbFilterByTypeRecycle: RadioButton
    private lateinit var rbFilterByTypeUtilization: RadioButton
    private lateinit var rbFilterByTypeBlago: RadioButton

    lateinit var wasteIdArr: Array<String>

    var typeId = "recycle"
    private var filterWasteIdList: MutableList<String> = ArrayList()
    private val selectedTypeItemList: MutableList<String> = ArrayList()
    var selectedWasteId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_by_type)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        wasteIdArr = resources.getStringArray(R.array.waste_id)
        gvFilterByType = findViewById(R.id.gv_filter_by_type)
        rbFilterByTypeRecycle = findViewById(R.id.rb_filter_by_type_recycle)
        rbFilterByTypeUtilization = findViewById(R.id.rb_filter_by_type_utilization)
        rbFilterByTypeBlago = findViewById(R.id.rb_filter_by_type_blago)

        if (intent != null)
            selectedWasteId = intent.getStringExtra(AppConstants.SELECTED_WASTE_ID)

        if (!selectedWasteId.isNullOrBlank()) {
            selectedTypeItemList.clear()
            for (item in selectedWasteId!!.split(";")) {
                if (!item.isBlank())
                    selectedTypeItemList.add(item)
            }
        }
        onSetToDataType()
        onSetAdapter()
        rbFilterByTypeRecycle.performClick()

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        rbFilterByTypeRecycle.setOnClickListener {
            typeId = "recycle"
            onSetToDataType()
            onSetRadioButtonColor(1)
            onSetAdapter()
        }

        rbFilterByTypeUtilization.setOnClickListener {
            typeId = "utilization"
            onSetToDataType()
            onSetRadioButtonColor(2)
            onSetAdapter()
        }

        rbFilterByTypeBlago.setOnClickListener {
            typeId = "blago"
            onSetToDataType()
            onSetRadioButtonColor(3)
            onSetAdapter()
        }

        gvFilterByType.setOnItemClickListener { parent, view, position, id -> onItemClicked(view, position) }

    }

    private fun onItemClicked(view: View?, position: Int) {
        if (view != null) {
            val layoutFilterByTypeItem: ViewGroup = view.findViewById(R.id.layout_filter_by_type_item)
            val ivFilterByTypeItemInfo: ImageView = view.findViewById(R.id.iv_filter_by_type_item_info)
            val ivFilterByTypeItemIcon: ImageView = view.findViewById(R.id.iv_filter_by_type_item_icon)
            val tvFilterByTypeItemName: TextView = view.findViewById(R.id.tv_filter_by_type_item_name)

            val typeItem = filterWasteIdList[position]
            val selectColor: Int
            if (selectedTypeItemList.contains(typeItem)) {
                selectedTypeItemList.remove(typeItem)
                selectColor = resources.getColor(R.color.not_selected_grid_item_color)
                layoutFilterByTypeItem.background =
                    resources.getDrawable(R.drawable.round_cornered_filter_grid_item)
            } else {
                selectedTypeItemList.add(typeItem)
                selectColor = resources.getColor(R.color.mainBackgroundColor)
                layoutFilterByTypeItem.background =
                    resources.getDrawable(R.drawable.background_rounded_cornered_selected_filter_grid_layout)
            }
            ivFilterByTypeItemIcon.setColorFilter(selectColor)
            ivFilterByTypeItemInfo.setColorFilter(selectColor)
            tvFilterByTypeItemName.setTextColor(selectColor)
        }
        if (selectedTypeItemList.isEmpty())
            onSetAdapter()
    }

    private fun onSetAdapter() {
        val adapter = FilterByTypeAdapter(filterWasteIdList, selectedTypeItemList)
        gvFilterByType.adapter = adapter
    }

    private fun onSetRadioButtonColor(order: Int) {
        val notSelectedColorselectedColor = resources.getColor(R.color.mainBackgroundColor)
        val selectedColor = resources.getColor(R.color.white)
        rbFilterByTypeRecycle.setTextColor(if (order == 1) selectedColor else notSelectedColorselectedColor)
        rbFilterByTypeUtilization.setTextColor(if (order == 2) selectedColor else notSelectedColorselectedColor)
        rbFilterByTypeBlago.setTextColor(if (order == 3) selectedColor else notSelectedColorselectedColor)
    }

    private fun onSetToDataType() {
        /*filterWasteIdList = wasteIdArr.toMutableList()
        filterWasteIdList.sortBy { it.contains(typeId) }*/
        filterWasteIdList.clear()
        for (ids in wasteIdArr) {
            if (ids.split(",")[0] == typeId)
                filterWasteIdList.add(ids)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_accept -> {
                onReturnResult()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onReturnResult() {
        selectedWasteId = ""
        for (item in selectedTypeItemList) {
            selectedWasteId += "$item;"
        }
        NavigationDrawerActivity.selectedWasteId = selectedWasteId!!
        val intent = Intent()
        intent.putExtra(AppConstants.SELECTED_WASTE_ID, selectedWasteId)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_accept, menu)
        return true
    }


}
