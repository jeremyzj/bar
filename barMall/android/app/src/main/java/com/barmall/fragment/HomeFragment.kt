package com.barmall.fragment

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by eleven on 2018/3/12.
 */
class HomeFragment : ReactFragment() {

    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()
            args.putString("routeName", "home")
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}