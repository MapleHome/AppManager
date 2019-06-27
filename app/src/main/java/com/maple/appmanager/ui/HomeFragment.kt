package com.maple.appmanager.ui

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.maple.appmanager.R
import com.maple.appmanager.databinding.FragmentHomeBinding
import com.maple.appmanager.ui.base.BaseFragment
import com.maple.appmanager.utils.PackageUtils
import com.maple.appmanager.utils.SPUtils

/**
 *
 * @author maple
 * @time 2019-06-27
 */
class HomeFragment : BaseFragment() {
    lateinit var binding: FragmentHomeBinding
    private val mViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    companion object {
        const val START_TAG = "isStart"
        // Config info
        val appNames = arrayListOf("LeSo", "LeSports", "LeStore", "LeVideo", "StvGallery", "StvWeather")
        const val startAppID = "com.example.androidx.viewpager2"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        subscribeUI()
    }

    private fun initView() {
        // binding.btAdd
        mViewModel.clickRefresh()
        // start other app
        val isStart = SPUtils().getBoolean(START_TAG, false)
        binding.cbStartApp.isChecked = isStart
        if (isStart) {
            doStartAppWithPackageName(startAppID)
        }

        // init Listener
        binding.apply {
            btRemove.setOnClickListener { mViewModel.removeSingleApp(appNames) }
            btAdd.setOnClickListener { mViewModel.addSingleApp(appNames) }
            btMove.setOnClickListener { mViewModel.moveToSystem("") }
            cbStartApp.setOnCheckedChangeListener { _, isChecked ->
                SPUtils().put(START_TAG, isChecked)
            }
        }
    }

    private fun subscribeUI() {
        // subscribe UI
        mViewModel.apply {
            canRemove.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                binding.btRemove.isEnabled = it
                binding.btAdd.isEnabled = !it
            })
            showInfo.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                binding.tvInfo.text = it
            })
        }
    }

    private fun doStartAppWithPackageName(packageName: String) {
        val className = PackageUtils.getStartActivityName(mContext, packageName)
        if (!TextUtils.isEmpty(className)) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.component = ComponentName(packageName, className!!)
            startActivity(intent)
            // finish();
        }
    }
}
