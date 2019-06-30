package com.jiaoerzhang.platform.ui.activity

import android.annotation.SuppressLint
import android.os.*
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent

import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.jiaoerzhang.platform.R
import com.jiaoerzhang.platform.contract.HomeContract
import com.jiaoerzhang.platform.lib_core.base.mvp.BaseMvpActivity
import com.jiaoerzhang.platform.lib_core.base.mvp.BasePresenter
import com.jiaoerzhang.platform.presenter.HomePresenter
import com.jiaoerzhang.platform.ui.fragment.album.AlbumFragment
import com.jiaoerzhang.platform.ui.fragment.HomeFragment
import com.jiaoerzhang.platform.ui.fragment.MineFragment

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvpActivity<HomeContract.IhomeModel, HomeContract.HomePresenter>(), BottomNavigationBar.OnTabSelectedListener {

    private var lastSelectedPosition: Int = 0


    // Fragment管理器，和执行器
    private var mManager: FragmentManager? = null
    private var mTransaction: FragmentTransaction? = null

    private var mHomeFragment: HomeFragment? = null  // 首页
    private var mAlumFragment: AlbumFragment? = null
    private var mMineFragment: MineFragment? = null

    override fun initView() {


        // TODO 设置模式
        bottom_navigation_bar!!.setMode(BottomNavigationBar.MODE_FIXED)
        // TODO 设置背景色样式
        bottom_navigation_bar!!.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        //        bottom_navigation_bar.setBarBackgroundColor(R.color.background_gray_color);
        bottom_navigation_bar!!
                .addItem(BottomNavigationItem(R.mipmap.button_home_click, "发现").setActiveColorResource(R.color.common_red_txt_color).setInactiveIconResource(R.mipmap.button_home_normal).setInActiveColorResource(R.color.common_hint_txt_color))
                .addItem(BottomNavigationItem(R.mipmap.button_album_click, "规划").setActiveColorResource(R.color.common_red_txt_color).setInactiveIconResource(R.mipmap.button_album_normal).setInActiveColorResource(R.color.common_hint_txt_color))
                .addItem(BottomNavigationItem(R.mipmap.button_album_click, "学习").setActiveColorResource(R.color.common_red_txt_color).setInactiveIconResource(R.mipmap.button_album_normal).setInActiveColorResource(R.color.common_hint_txt_color))
                .addItem(BottomNavigationItem(R.mipmap.button_user_click, "我的").setActiveColorResource(R.color.common_red_txt_color).setInactiveIconResource(R.mipmap.button_user_normal).setInActiveColorResource(R.color.common_hint_txt_color))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise()
        bottom_navigation_bar!!.setTabSelectedListener(this)


    }

    override fun bindLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initPresenter(): BasePresenter<*, *> {

        return HomePresenter()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun init() {


        mManager = supportFragmentManager

        bottom_navigation_bar!!.selectTab(0)


    }

    override fun onTabSelected(position: Int) {
        lastSelectedPosition = position
        //开启事务
        mTransaction = mManager!!.beginTransaction()
        hideFragment(mTransaction!!)

        /**
         * fragment 用 add + show + hide 方式
         * 只有第一次切换会创建fragment，再次切换不创建
         *
         * fragment 用 replace 方式
         * 每次切换都会重新创建
         *
         */
        when (position) {
            0 -> if (mHomeFragment == null) {
                mHomeFragment = HomeFragment()
                mTransaction!!.add(R.id.ll_content,
                        mHomeFragment!!)
            } else {
                mTransaction!!.show(mHomeFragment!!)
            }
            1 -> if (mAlumFragment == null) {
                mAlumFragment = AlbumFragment()
                mTransaction!!.add(R.id.ll_content,
                        mAlumFragment!!)
            } else {
                mTransaction!!.show(mAlumFragment!!)
            }
            2 ->

                if (mMineFragment == null) {
                    mMineFragment = MineFragment()
                    mTransaction!!.add(R.id.ll_content,
                            mMineFragment!!)
                } else {
                    mTransaction!!.show(mMineFragment!!)
                }
        }
        // 事务提交
        mTransaction!!.commit()
        //  mTransaction.commitAllowingStateLoss();
    }

    override fun onTabUnselected(position: Int) {

    }

    override fun onTabReselected(position: Int) {

    }

    /**
     * 隐藏当前fragment
     *
     * @param transaction
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment!!)
        }

        if (mAlumFragment != null) {
            transaction.hide(mAlumFragment!!)
        }

        if (mMineFragment != null) {
            transaction.hide(mMineFragment!!)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()

            return false

        }
        return super.onKeyDown(keyCode, event)
    }


    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            isExit = false
        }
    }

    //定义一个变量，来标识是否退出
    private var isExit = false

    private fun exit() {
        if (!isExit) {
            isExit = true
            showToast(getString(R.string.more_exit))
            //利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 3000)
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 解决重影问题
     */
    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle?) {
        //如果用以下这种做法则不保存状态，再次进来的话会显示默认的tab
//        super.onSaveInstanceState(outState)
    }

}
