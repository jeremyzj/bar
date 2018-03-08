package com.barmall.widget


import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.util.ArrayMap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.barmall.R
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates.observable

/**
 * Created by zisha on 2018/3/8.
 */
class BottomBar(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    /**
     * 监听器.
     */
    var listener: Listener? by observable(null) {
        _, _: Listener?, newListener: Listener? ->
        newListener?.onTabSelected(currentTabView!!.tab)
    }

    var currentTab: Tab
        set(tab) {
            positionToTabView[tab.position]?.takeIf { it.tab == tab }?.let { currentTabView = it }
        }
        get() = currentTabView!!.tab

    /**
     * 当前选中的 Tab.
     */
    private var currentTabView: TabView? by observable(null) {
        _, oldTabView: TabView?, newTabView: TabView? ->
        newTabView!! // 除了初始值，后续值不应该为 null
        if (newTabView != oldTabView) {
            oldTabView?.takeIf { it.plugged }?.deselect()
            newTabView.select()
            listener?.onTabSelected(newTabView.tab)
        }
    }

    /**
     * 位置与 Tab 的对应关系.
     */
    private val positionToTabView: ArrayMap<Int, TabView> = ArrayMap()
    private val update = PublishSubject.create<Unit>()
    private val subscriptions = CompositeDisposable()


    /**
     * 首页 Tab 枚举.
     */
    enum class Tab(val position: Int) {
        HOME(0) {
            override fun applyView(tabView: ImageTextButton) {
                tabView.setButton(R.mipmap.ic_launcher)
                tabView.setText(R.string.tab_home)
            }
        },
        MESSAGE(1) {
            override fun applyView(tabView: ImageTextButton) {
                tabView.setButton(R.mipmap.ic_launcher)
                tabView.setText(R.string.tab_message)
            }
        },
        MALL(2) {
            override fun applyView(tabView: ImageTextButton) {
                tabView.setButton(R.mipmap.ic_launcher)
                tabView.setText(R.string.tab_mall)

            }
        },
        USER(3) {
            override fun applyView(tabView: ImageTextButton) {
                tabView.setButton(R.mipmap.ic_launcher)
                tabView.setText(R.string.tab_user)
            }
        };

        open fun applyView(tabView: ImageTextButton) {}
    }


    data class TabItem(val tab: Tab, var visible: Boolean = true)

    private inner class TabView(val tab: Tab) {
        var plugged = false
            private set
        val container = LayoutInflater.from(context).inflate(R.layout.tab_bottom_bar_home, this@BottomBar, false)!!
        val view: ImageTextButton = container.findViewById(R.id.btn_tab_bottom_bar)

        fun plug() {
            if (plugged) throw IllegalStateException("Already plugged.")
            view.setOnClickListener { currentTabView = this@TabView }
            tab.applyView(view)

            if (this@BottomBar.childCount >= tab.position) {
                this@BottomBar.addView(container, tab.position)
            } else {
                this@BottomBar.addView(container)
            }
            positionToTabView.put(tab.position, this)
            plugged = true
        }

        fun unplug() {
            ensurePlugged()
            // 移除旧 Tab 相关引用并解除其对应的红点订阅
            this@BottomBar.removeView(container)
            positionToTabView.remove(tab.position)
            plugged = false
        }

        fun select() {
            ensurePlugged()
            view.isSelected = true
        }

        fun deselect() {
            ensurePlugged()
            view.isSelected = false
        }

        fun refreshView() {
            tab.applyView(view)
        }

        private fun ensurePlugged() {
            if (!plugged) throw IllegalStateException("Haven't plugged.")
        }

    }

    /**
     * 监听器接口.
     */
    interface Listener {
        /**
         * 当 Tab 被选中时调用.
         */
        fun onTabSelected(tab: Tab)
    }

    init {
        Observable.just(TabItem(Tab.HOME), TabItem(Tab.MESSAGE), TabItem(Tab.MALL), TabItem(Tab.USER))
                .subscribe { updateTab(it) }.let { subscriptions.add(it) }
    }

    fun update() {
        update.onNext(Unit)
        forEachTabViewDo {
            refreshView()
        }
    }

    inline private fun forEachTabViewDo(action: TabView.() -> Unit) =
            (0..positionToTabView.size - 1).map { positionToTabView.valueAt(it) }.forEach(action)

    private fun updateTab(item: TabItem) {
        val oldTabView = positionToTabView[item.tab.position]
        // 移除旧 tab
        oldTabView?.unplug()
        var newTabView: TabView? = null
        if (item.visible) {
            newTabView = TabView(item.tab)
            // 应用新 Tab
            newTabView.plug()
        }

        if (currentTabView == null) {
            if (item.visible) {
                // 默认选中第一个 Tab
                currentTabView = newTabView!!
            }
        } else if (currentTabView!!.tab.position == item.tab.position) {
            if (item.visible) {
                currentTabView = newTabView!!
            } else {
                currentTabView = positionToTabView[0]!!
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, currentTabView!!.tab.position)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        currentTabView = positionToTabView[state.position] ?: positionToTabView.values.first()!!
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.clear()
        forEachTabViewDo { unplug() }
    }

    /**
     * 用于持久化状态的类。
     */
    class SavedState : BaseSavedState {
        var position: Int

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(position)
        }

        override fun toString(): String {
            return "BottomBar.SavedState{position=$position}"
        }

        constructor(superState: Parcelable, position: Int) : super(superState) {
            this.position = position
        }

        constructor(`in`: Parcel) : super(`in`) {
            this.position = `in`.readInt()
        }

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
