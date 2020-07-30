package com.nesger.wechatpopupwindow

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import com.zengyu.popupwindowlist.PopupWindowList
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun rightTop(v: View) {
        Log.e(TAG, "rightTop")
        showPopWindows(v)
    }

    fun middle(v: View) {
        Log.e(TAG, "middle")
        showPopWindows(v)
    }

    fun leftBottom(v: View) {
        Log.e(TAG, "leftBottom")
        showPopWindows(v)
    }

    private var mPopupWindowList: PopupWindowList? = null
    private fun showPopWindows(view: View) {
        val dataList: MutableList<String> = ArrayList()
        for (i in 0..12) {
            dataList.add(i.toString())
        }
        if (mPopupWindowList == null) {
            mPopupWindowList = PopupWindowList(view.context)
        }
        mPopupWindowList!!.setAnchorView(view)
        mPopupWindowList!!.setItemData(dataList)
        mPopupWindowList!!.setModal(true)
        mPopupWindowList!!.show()
        mPopupWindowList!!.setOnItemClickListener(OnItemClickListener { _, _, position, _ ->
            Log.e(TAG, "click position=$position")
            mPopupWindowList!!.hide()
        })
    }

    companion object {
        private val TAG = MainActivity::class.java.name
    }
}