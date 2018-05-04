package com.theappsolutions.boilerplate.util.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.javatuples.Quartet;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class FragmentUtils {

    public static boolean isBackStackEmpty(FragmentActivity fragmentAct) {
        FragmentManager fm = fragmentAct.getSupportFragmentManager();
        return fm.getBackStackEntryCount() == 0;
    }

    public static void addFragment(int contId, Fragment fg,
                                   FragmentActivity fragmentAct) {
        FragmentTransaction transaction = fragmentAct
            .getSupportFragmentManager().beginTransaction();
        transaction.add(contId, fg);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void addFragmentWithoutBackStack(int contId, Fragment fg,
                                                   FragmentActivity fragmentAct) {
        FragmentTransaction transaction = fragmentAct
            .getSupportFragmentManager().beginTransaction();
        transaction.add(contId, fg);
        transaction.commit();
    }

    public static void clearBackStackAndAddFragment(int contId, Fragment fg,
                                                    FragmentActivity fragmentAct) {
        clearBackStack(fragmentAct);
        addFragment(contId, fg, fragmentAct);
    }

    public static void clearBackStackAndAddFragmentWithoutIt(
        int contId, Fragment fg, FragmentActivity fragmentAct) {
        addFragmentWithoutBackStack(contId, fg, fragmentAct);
    }

    public static void clearBackStack(FragmentActivity fragmentAct) {
        FragmentManager manager = fragmentAct.getSupportFragmentManager();
        int backStackCount = manager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = manager.getBackStackEntryAt(i).getId();
            manager.popBackStack(backStackId,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static void replaceWithoutBackStack(int contId, Fragment fg,
                                               FragmentActivity fragmentAct) {
        FragmentTransaction transaction = fragmentAct
            .getSupportFragmentManager().beginTransaction();
        transaction.replace(contId, fg);
        transaction.commit();
    }

    public static void replaceWithoutBackStackWithTransition(int contId,
                                                             Fragment fg, FragmentActivity fragmentAct) {
        FragmentTransaction tr = fragmentAct
            .getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(android.R.anim.slide_in_left, 0);
        tr.replace(contId, fg);
        tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        tr.commit();
    }

    public static void replaceWithoutBackStackWithTransition(int contId,
                                                             int animId, Fragment fg, FragmentActivity fragmentAct) {
        FragmentTransaction tr = fragmentAct
            .getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(animId, 0);
        tr.replace(contId, fg);
        tr.commit();
    }

    public static void replaceWithBackStackWithTransition(int contId,
                                                          int animId, Fragment fg, FragmentActivity fragmentAct) {
        FragmentTransaction tr = fragmentAct
            .getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(animId, 0);
        tr.replace(contId, fg);
        tr.addToBackStack(null);
        tr.commit();
    }

    public static void replaceWithBackStackWithAnimation(int contId,
                                                         Quartet<Integer, Integer, Integer, Integer> animQuartet,
                                                         Fragment fg,
                                                         FragmentActivity fragmentAct) {
        FragmentTransaction tr = fragmentAct
            .getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(animQuartet.getValue0(),
            animQuartet.getValue1(),
            animQuartet.getValue2(),
            animQuartet.getValue3());
        tr.replace(contId, fg);
        tr.addToBackStack(null);
        tr.commit();
    }

    public static void replaceWithBackStack(int contId, Fragment fg, FragmentActivity fragmentAct) {
        FragmentTransaction tr = fragmentAct
            .getSupportFragmentManager().beginTransaction();
        tr.replace(contId, fg);
        tr.addToBackStack(null);
        tr.commit();
    }

    public static Fragment getTopFragment(FragmentActivity fragmentAct, int contId) {
        return fragmentAct.getSupportFragmentManager().findFragmentById(contId);
    }
}
