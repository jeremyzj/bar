package com.barmall.fragment

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by eleven on 2018/3/12.
 */
class MallFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()
            val fragment = MallFragment()
            fragment.arguments = args
            return fragment
        }
    }
}