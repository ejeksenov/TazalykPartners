package kz.nextstep.tazalykpartners.ui.addPointsToUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.utils.CustomAddWasteItemDialog
import kz.nextstep.tazalykpartners.utils.CustomProgressBar

class AddPointsToUserActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvAddPointToUserTypeList: RecyclerView
    private lateinit var btnAddPointToUserAdd: ImageButton
    private lateinit var btnAddPointToUserSave: Button
    private lateinit var tvAddPointToUserTotal: TextView

    private lateinit var addPointsToUserViewModel: AddPointsToUserViewModel
    private lateinit var customProgressBar: CustomProgressBar

    private var userId: String? = ""
    private var pinId: String? = ""
    private var wasteIds: String? = ""
    private var wasteItemTypeTotal = ""

    private val wasteItemTotalList: MutableList<String> = ArrayList()
    private lateinit var adapter: PassedWasteTypeTotalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_points_to_user)

        addPointsToUserViewModel = ViewModelProviders.of(this).get(AddPointsToUserViewModel::class.java)
        customProgressBar = CustomProgressBar(this)

        toolbar = findViewById(R.id.toolbar)
        rvAddPointToUserTypeList = findViewById(R.id.rv_add_point_to_user_type_list)
        btnAddPointToUserAdd = findViewById(R.id.btn_add_point_to_user_add)
        btnAddPointToUserSave = findViewById(R.id.btn_add_point_to_user_save)
        tvAddPointToUserTotal = findViewById(R.id.tv_add_point_to_user_total)

        val intentValue = intent
        if (intentValue != null) {
            userId = intentValue.getStringExtra(AppConstants.USER_ID)
            pinId = intentValue.getStringExtra(AppConstants.PIN_ID)
            wasteIds = intentValue.getStringExtra(AppConstants.SELECTED_WASTE_ID)
            onAddWasteItemDialog()
        }

        onSetAdapter()

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.add_points_manually)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btnAddPointToUserAdd.setOnClickListener {
            onAddWasteItemDialog()
        }

        btnAddPointToUserSave.setOnClickListener {
            if (wasteItemTotalList.isNotEmpty() && !userId.isNullOrBlank() && !pinId.isNullOrBlank()) {
                customProgressBar.show()
                addPointsToUserViewModel.onSaveToHistoryPin(userId!!, pinId!!, wasteItemTypeTotal)
            }
        }

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        CustomAddWasteItemDialog.onItemClick = { wasteType, total ->
            if (wasteIds!!.contains(wasteType)) {
                val wasteTypeItem = "$wasteType,$total"
                wasteIds = wasteIds!!.replace("$wasteType;", "")
                wasteItemTotalList.add(wasteTypeItem)
                adapter.notifyDataSetChanged()
                onSetTotal()
            }
        }

        adapter.onChangeOrDeleteItem = { wasteTypeItem ->
            val wasteTypeItemArr = wasteTypeItem.split(",")
            if (wasteTypeItemArr.size >= 4) {
                val wasteIdItem = "${wasteTypeItemArr[0]},${wasteTypeItemArr[1]},${wasteTypeItemArr[2]}"
                if (!wasteIds!!.contains(wasteIdItem))
                    wasteIds += "$wasteIdItem;"
                wasteItemTotalList.remove(wasteTypeItem)
                adapter.notifyDataSetChanged()
                onSetTotal()
            }
        }

        addPointsToUserViewModel.historyPinLiveData.observe(this, Observer {
            customProgressBar.dismiss()
            if (it) {
                addPointsToUserViewModel.showToastMessage(resources.getString(R.string.success_added_total))
                finish()
            } else
                addPointsToUserViewModel.showToastMessage(AppConstants.ERROR_SOMETHING_WENT_WRONG)
        })
    }

    private fun onSetTotal() {
        var totalSum = 0.0
        wasteItemTypeTotal = ""
        for (item in wasteItemTotalList) {
            val itemArr = item.split(",")
            if (itemArr.size >= 4) {
                val total = itemArr[3].toDouble()
                wasteItemTypeTotal += "${itemArr[0]},${itemArr[1]},$total;"
                totalSum += total
            }
        }
        val totalStr = "Итог: $totalSum кг"
        tvAddPointToUserTotal.text = totalStr
        onManageAddButton()
    }

    private fun onManageAddButton() {
        if (wasteIds.isNullOrBlank())
            btnAddPointToUserAdd.visibility = View.GONE
        else
            btnAddPointToUserAdd.visibility = View.VISIBLE
    }

    private fun onSetAdapter() {
        rvAddPointToUserTypeList.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        rvAddPointToUserTypeList.layoutManager = LinearLayoutManager(this)
        adapter = PassedWasteTypeTotalAdapter(wasteItemTotalList)
        rvAddPointToUserTypeList.adapter = adapter
    }

    private fun onAddWasteItemDialog() {
        if (!wasteIds.isNullOrBlank()) {
            val customAddWasteItemDialog = CustomAddWasteItemDialog(this, wasteIds!!)
            customAddWasteItemDialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addPointsToUserViewModel.historyPinLiveData.removeObservers(this)
    }
}
