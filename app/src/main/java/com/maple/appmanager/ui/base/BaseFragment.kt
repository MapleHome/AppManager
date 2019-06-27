package com.maple.appmanager.ui.base

import android.content.Context
import androidx.fragment.app.Fragment

/**
 *
 * @author maple
 * @time 2019-06-27
 */
open class BaseFragment : Fragment() {
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    open fun onKeyBackPressed(): Boolean {
        // 是否消耗掉back事件
        return false
    }

}