package com.maple.appmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.maple.appmanager.R
import com.maple.appmanager.databinding.FragmentHomeBinding
import com.maple.appmanager.ui.base.BaseFragment

/**
 *
 * @author maple
 * @time 2019-06-27
 */
class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val mViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
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
        mViewModel.refreshInfo()

        // init Listener
        binding.apply {
            btRemove.setOnClickListener {
                mViewModel.removeSingleApp()
            }
            btAdd.setOnClickListener {
                mViewModel.addSingleApp()
            }
            btMove.setOnClickListener {
                mViewModel.moveToSystem()
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
}
