package com.barmall.fragment

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by eleven on 2018/3/12.
 */
class UserFragment : ReactFragment() {
    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()
            args.putString("routeName", "user")
            val fragment = UserFragment()
            fragment.arguments = args
            return fragment
        }
    }
}