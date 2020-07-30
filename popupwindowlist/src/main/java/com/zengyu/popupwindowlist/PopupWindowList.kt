package com.zengyu.popupwindowlist

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow

/**
 * Created by zengyu.zhan on 2018/1/22.
 */
class PopupWindowList(mContext: Context?) {
    private val mContext: Context
    private var mPopupWindow: PopupWindow? = null

    //the view where PopupWindow lie in
    private var mAnchorView: View? = null

    //ListView item data
    private var mItemData: List<String>? = null

    //the animation for PopupWindow
    private var mPopAnimStyle = 0

    //the PopupWindow width
    private var mPopupWindowWidth = 0

    //the PopupWindow height
    private var mPopupWindowHeight = 0
    private var mItemClickListener: OnItemClickListener? = null
    private var mModal = false
    fun setAnchorView(anchor: View?) {
        mAnchorView = anchor
    }

    fun setItemData(mItemData: List<String>?) {
        this.mItemData = mItemData
    }

    fun setPopAnimStyle(mPopAnimStyle: Int) {
        this.mPopAnimStyle = mPopAnimStyle
    }

    fun setPopupWindowWidth(mPopupWindowWidth: Int) {
        this.mPopupWindowWidth = mPopupWindowWidth
    }

    fun setPopupWindowHeight(mPopupWindowHeight: Int) {
        this.mPopupWindowHeight = mPopupWindowHeight
    }

    /**
     * Set whether this window should be modal when shown.
     *
     *
     * If a popup window is modal, it will receive all touch and key input.
     * If the user touches outside the popup window's content area the popup window
     * will be dismissed.
     *
     * @param modal `true` if the popup window should be modal, `false` otherwise.
     */
    fun setModal(modal: Boolean) {
        mModal = modal
    }

    val isShowing: Boolean
        get() = mPopupWindow != null && mPopupWindow!!.isShowing

    fun hide() {
        if (isShowing) {
            mPopupWindow!!.dismiss()
        }
    }

    /**
     * Sets a listener to receive events when a list item is clicked.
     *
     * @param clickListener Listener to register
     *
     * @see ListView.setOnItemClickListener
     */
    fun setOnItemClickListener(clickListener: OnItemClickListener?) {
        mItemClickListener = clickListener
        if (mPopView != null) {
            mPopView!!.onItemClickListener = mItemClickListener
        }
    }

    private var mPopView: ListView? = null
    fun show() {
        requireNotNull(mAnchorView) { "PopupWindow show location view can  not be null" }
        requireNotNull(mItemData) { "please fill ListView Data" }
        mPopView = ListView(mContext)
        mPopView!!.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
        mPopView!!.isVerticalScrollBarEnabled = false
        mPopView!!.divider = null
        mPopView!!.adapter = ArrayAdapter(mContext,
                R.layout.simple_list_item_1, mItemData)
        if (mItemClickListener != null) {
            mPopView!!.onItemClickListener = mItemClickListener
        }
        mPopView!!.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        if (mPopupWindowWidth == 0) {
            mPopupWindowWidth = mDeviceWidth / 3
        }
        if (mPopupWindowHeight == 0) {
            mPopupWindowHeight = mItemData!!.size * mPopView!!.measuredHeight
            if (mPopupWindowHeight > mDeviceHeight / 2) {
                mPopupWindowHeight = mDeviceHeight / 2
            }
        }
        mPopupWindow = PopupWindow(mPopView, mPopupWindowWidth, mPopupWindowHeight)
        if (mPopAnimStyle != 0) {
            mPopupWindow!!.animationStyle = mPopAnimStyle
        }
        mPopupWindow!!.isOutsideTouchable = true
        mPopupWindow!!.isFocusable = mModal
        mPopupWindow!!.setBackgroundDrawable(BitmapDrawable(mContext.resources, null as Bitmap?))
        val location = locateView(mAnchorView)
        if (location != null) {
            val x: Int
            //view中心点X坐标
            val xMiddle = location.left + mAnchorView!!.width / 2
            x = if (xMiddle > mDeviceWidth / 2) {
                //在右边
                xMiddle - mPopupWindowWidth
            } else {
                xMiddle
            }
            val y: Int
            //view中心点Y坐标
            val yMiddle = location.top + mAnchorView!!.height / 2
            y = if (yMiddle > mDeviceHeight / 2) {
                //在下方
                yMiddle - mPopupWindowHeight
            } else {
                //在上方
                yMiddle
            }
            mPopupWindow!!.showAtLocation(mAnchorView, Gravity.NO_GRAVITY, x, y)
        }
    }

    private fun locateView(v: View?): Rect? {
        if (v == null) return null
        val locInt = IntArray(2)
        try {
            v.getLocationOnScreen(locInt)
        } catch (npe: NullPointerException) {
            //Happens when the view doesn't exist on screen anymore.
            return null
        }
        val location = Rect()
        location.left = locInt[0]
        location.top = locInt[1]
        location.right = location.left + v.width
        location.bottom = location.top + v.height
        return location
    }

    private var mDeviceWidth = 0
    private var mDeviceHeight = 0
    private fun setHeightWidth() {
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //API 13才允许使用新方法
        val outSize = Point()
        wm.defaultDisplay.getSize(outSize)
        if (outSize.x != 0) {
            mDeviceWidth = outSize.x
        }
        if (outSize.y != 0) {
            mDeviceHeight = outSize.y
        }
    }

    init {
        requireNotNull(mContext) { "context can not be null" }
        this.mContext = mContext
        setHeightWidth()
    }
}