package kz.nextstep.tazalykpartners.ui.userInteractivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.domain.utils.AppConstants.REQUEST_CODE
import kz.nextstep.domain.utils.ChangeDateFormat
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.base.BaseNavigationViewActivity.Companion.filterDateDays
import kz.nextstep.tazalykpartners.base.BaseNavigationViewActivity.Companion.selectedDates
import kz.nextstep.tazalykpartners.base.BaseNavigationViewActivity.Companion.selectedFilterType
import kz.nextstep.tazalykpartners.base.BaseNavigationViewActivity.Companion.selectedWasteId
import kz.nextstep.tazalykpartners.databinding.UserInteractivityFragmentBinding
import kz.nextstep.tazalykpartners.ui.filterByDate.FilterByDateActivity
import kz.nextstep.tazalykpartners.utils.CustomProgressBar
import kz.nextstep.tazalykpartners.utils.data.PassedUserPinItem
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.Exception


class UserInteractivityFragment : Fragment() {

    companion object {
        fun newInstance() = UserInteractivityFragment()
    }

    private lateinit var viewModel: UserInteractivityViewModel
    private lateinit var binding: UserInteractivityFragmentBinding
    private lateinit var customProgressBar: CustomProgressBar

    var selectedfilterDateType = "За месяц"
    var selectedfilterDateDays = 30
    var selectedFilterDates = ""

    private val statisticsFileName = "Statistics.json"
    private val gson = Gson()

    var kwrFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.user_interactivity_fragment,
            container,
            false
        )

        onAssignData()
        if (selectedFilterDates.isBlank())
            selectedFilterDates = ChangeDateFormat.onGetFilterDate(filterDateDays)

        viewModel = ViewModelProviders.of(this).get(UserInteractivityViewModel::class.java)
        customProgressBar = CustomProgressBar(context!!)

        binding.tvUserInteractivityDateFilterType.text = selectedFilterType

        binding.rvUserInteractivityList.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )
        binding.rvUserInteractivityList.layoutManager = LinearLayoutManager(context)

        viewModel.getPassedUserList(selectedWasteId, selectedFilterDates)
        customProgressBar.show()

        viewModel.customProgressBarLiveData.observe(this, Observer {
            customProgressBar.dismiss()
        })

        binding.tvUserInteractivityDateFilterType.setOnClickListener {
            val intent = Intent(activity, FilterByDateActivity::class.java)
            intent.putExtra(AppConstants.SELECTED_DATES, selectedFilterDates)
            intent.putExtra(AppConstants.SELECTED_FILTER_TYPE, selectedfilterDateType)
            activity?.startActivityForResult(intent, REQUEST_CODE)
        }

        viewModel.passedPinUserListDatas.observe(this, Observer {
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                onSaveJsonFile(it)
            } else
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100
                )
        })

        binding.fbUserInteractivityShare.setOnClickListener {
            if (kwrFile != null) {
                val path =
                    FileProvider.getUriForFile(context!!, context?.resources?.getString(R.string.fileprovider_authorities)!!, kwrFile!!)
                val i = Intent(Intent.ACTION_SEND)
                i.putExtra(Intent.EXTRA_TEXT, "Statistics")
                i.putExtra(Intent.EXTRA_STREAM, path)
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                i.type = "plain/*"
                startActivity(i)
            } else
                viewModel.showToastMessage(context?.resources?.getString(R.string.file_not_saved))
        }

        binding.viewModel = viewModel

        return binding.root
    }

    private fun onSaveJsonFile(it: MutableList<PassedUserPinItem>?) {
        try {
            kwrFile =
                File(context?.filesDir, statisticsFileName)
            val fileWriter = FileWriter(kwrFile!!)
            val bufferedWriter = BufferedWriter(fileWriter)
            val list = gson.toJson(it)
            bufferedWriter.write(list)
            bufferedWriter.close()

        } catch (e: Exception) {
            viewModel.showToastMessage(e.message)
        }
    }


    private fun onAssignData() {
        selectedfilterDateType = selectedFilterType
        selectedFilterDates = selectedDates
        selectedfilterDateDays = filterDateDays
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE) {
                selectedDates = data.getStringExtra(AppConstants.SELECTED_DATES)!!
                selectedFilterType = data.getStringExtra(AppConstants.SELECTED_FILTER_TYPE)!!
                filterDateDays = data.getIntExtra(AppConstants.FILTER_DATE_DAYS, 30)
                binding.tvUserInteractivityDateFilterType.text = selectedFilterType
                onAssignData()
            }
            viewModel.getPassedUserList(selectedWasteId, selectedFilterDates)
        }
    }

}
