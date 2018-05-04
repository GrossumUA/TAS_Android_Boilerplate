package com.theappsolutions.boilerplate.additionalusefulclasses.ui.menu;

import android.support.annotation.IntDef;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
@IntDef(value = {
    MenuStates.M_ID_CLAUSE_1,
    MenuStates.M_ID_CLAUSE_2,
})
public @interface MenuStates {

    int M_ID_CLAUSE_1 = 0;
    int M_ID_CLAUSE_2 = 1;

}