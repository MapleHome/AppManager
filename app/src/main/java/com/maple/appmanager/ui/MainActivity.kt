package com.maple.appmanager.ui

import android.os.Bundle
import com.maple.appmanager.R
import com.maple.appmanager.ui.base.BaseFragmentActivity

/**
 * @author maple
 * @time 2019-06-27
 */
class MainActivity : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addView(HomeFragment())
    }

}
