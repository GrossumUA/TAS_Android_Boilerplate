package com.theappsolutions.boilerplate.util.other

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
    fun openLinkInBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
}
