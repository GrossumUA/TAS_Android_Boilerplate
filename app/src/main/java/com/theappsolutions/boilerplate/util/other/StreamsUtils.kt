package com.theappsolutions.boilerplate.util.other

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */

@Throws(IOException::class)
fun convertInputStreamToString(inputStream: InputStream): String {
    val r = BufferedReader(InputStreamReader(inputStream))
    val total = StringBuilder()
    var line: String
    while (true) {
        line = r.readLine() ?: break
        total.append(line).append('\n')
    }
    return total.toString()
}

