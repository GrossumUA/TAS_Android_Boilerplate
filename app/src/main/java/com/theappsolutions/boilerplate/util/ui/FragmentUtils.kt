package com.theappsolutions.boilerplate.util.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

import org.javatuples.Quartet

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
object FragmentUtils {

    fun isBackStackEmpty(fragmentAct: FragmentActivity): Boolean {
        val fm = fragmentAct.supportFragmentManager
        return fm.backStackEntryCount == 0
    }

    fun addFragment(contId: Int, fg: Fragment,
                    fragmentAct: FragmentActivity) {
        val transaction = fragmentAct
                .supportFragmentManager.beginTransaction()
        transaction.add(contId, fg)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun addFragmentWithoutBackStack(contId: Int, fg: Fragment,
                                    fragmentAct: FragmentActivity) {
        val transaction = fragmentAct
                .supportFragmentManager.beginTransaction()
        transaction.add(contId, fg)
        transaction.commit()
    }

    fun clearBackStackAndAddFragment(contId: Int, fg: Fragment,
                                     fragmentAct: FragmentActivity) {
        clearBackStack(fragmentAct)
        addFragment(contId, fg, fragmentAct)
    }

    fun clearBackStackAndAddFragmentWithoutIt(
            contId: Int, fg: Fragment, fragmentAct: FragmentActivity) {
        addFragmentWithoutBackStack(contId, fg, fragmentAct)
    }

    fun clearBackStack(fragmentAct: FragmentActivity) {
        val manager = fragmentAct.supportFragmentManager
        val backStackCount = manager.backStackEntryCount
        for (i in 0 until backStackCount) {
            val backStackId = manager.getBackStackEntryAt(i).id
            manager.popBackStack(backStackId,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun replaceWithoutBackStack(contId: Int, fg: Fragment,
                                fragmentAct: FragmentActivity) {
        val transaction = fragmentAct
                .supportFragmentManager.beginTransaction()
        transaction.replace(contId, fg)
        transaction.commit()
    }

    fun replaceWithoutBackStackWithTransition(contId: Int,
                                              fg: Fragment, fragmentAct: FragmentActivity) {
        val tr = fragmentAct
                .supportFragmentManager.beginTransaction()
        tr.setCustomAnimations(android.R.anim.slide_in_left, 0)
        tr.replace(contId, fg)
        tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        tr.commit()
    }

    fun replaceWithoutBackStackWithTransition(contId: Int,
                                              animId: Int, fg: Fragment, fragmentAct: FragmentActivity) {
        val tr = fragmentAct
                .supportFragmentManager.beginTransaction()
        tr.setCustomAnimations(animId, 0)
        tr.replace(contId, fg)
        tr.commit()
    }

    fun replaceWithBackStackWithTransition(contId: Int,
                                           animId: Int, fg: Fragment, fragmentAct: FragmentActivity) {
        val tr = fragmentAct
                .supportFragmentManager.beginTransaction()
        tr.setCustomAnimations(animId, 0)
        tr.replace(contId, fg)
        tr.addToBackStack(null)
        tr.commit()
    }

    fun replaceWithBackStackWithAnimation(contId: Int,
                                          animQuartet: Quartet<Int, Int, Int, Int>,
                                          fg: Fragment,
                                          fragmentAct: FragmentActivity) {
        val tr = fragmentAct
                .supportFragmentManager.beginTransaction()
        tr.setCustomAnimations(animQuartet.value0,
                animQuartet.value1,
                animQuartet.value2,
                animQuartet.value3)
        tr.replace(contId, fg)
        tr.addToBackStack(null)
        tr.commit()
    }

    fun replaceWithBackStack(contId: Int, fg: Fragment, fragmentAct: FragmentActivity) {
        val tr = fragmentAct
                .supportFragmentManager.beginTransaction()
        tr.replace(contId, fg)
        tr.addToBackStack(null)
        tr.commit()
    }

    fun getTopFragment(fragmentAct: FragmentActivity, contId: Int): Fragment {
        return fragmentAct.supportFragmentManager.findFragmentById(contId)
    }
}
