package com.barmall.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.barmall.MainApplication
import com.facebook.react.*
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.modules.core.DeviceEventManagerModule

/**
 * Created by eleven on 2018/3/8.
 */
abstract class ReactFragment : Fragment(), DefaultHardwareBackBtnHandler {
    private lateinit var reactRootView: ReactRootView
    private val reactNativeHost by lazy { (activity.application as MainApplication).reactNativeHost }

    private val mainComponentName = "barMall"

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        reactRootView = ReactRootView(activity)
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return reactRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reactRootView.startReactApplication(reactNativeHost.reactInstanceManager, mainComponentName, arguments)
    }


    override fun onResume() {
        super.onResume()
        if (this.reactNativeHost.hasInstance()) {
            this.reactNativeHost.reactInstanceManager.onHostResume(activity, this)
        }
    }

    override fun onPause() {
        super.onPause()
        if (this.reactNativeHost.hasInstance()) {
            this.reactNativeHost.reactInstanceManager.onHostPause(activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.reactRootView.unmountReactApplication()

        if (reactNativeHost.hasInstance()) {
            reactNativeHost.reactInstanceManager.onHostDestroy()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (reactNativeHost.hasInstance()) {
            reactNativeHost.reactInstanceManager.onActivityResult(activity, requestCode, resultCode, data)
        }

    }

    override fun invokeDefaultOnBackPressed() {
        //        super.onBackPressed();
    }

    /***
     * 发送消息到RN
     * @param eventName
     * @param params
     */
    @JvmOverloads protected fun sendEvent(eventName: String, params: WritableMap? = Arguments.createMap()) {
        val reactContext = reactNativeHost.reactInstanceManager.currentReactContext ?: return

        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
    }
}

