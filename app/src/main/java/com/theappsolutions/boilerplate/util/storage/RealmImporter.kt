package com.theappsolutions.boilerplate.util.storage

import android.content.Context
import android.content.res.Resources

import com.annimon.stream.function.Function
import com.theappsolutions.boilerplate.TasBoilerplateSettings
import com.theappsolutions.boilerplate.util.data.RandomUtils

import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Matcher
import java.util.regex.Pattern

import io.realm.Realm
import io.realm.RealmModel
import timber.log.Timber

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Manger that provides importing Realm database from json
 */
class RealmImporter(private val context: Context) {
    private val resources: Resources = context.resources

    private fun replaceAll(source: String, regex: String,
                           replacement: Function<String, String>): String {
        val sb = StringBuffer()
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(source)
        while (matcher.find()) {
            matcher.appendReplacement(sb, replacement.apply(matcher.group(0)))
        }
        matcher.appendTail(sb)
        return sb.toString()
    }

    private fun isDbDoesNotExists(context: Context): Boolean {
        return !fileFound(TasBoilerplateSettings.REALM_DB_NAME, context.filesDir)
    }

    private fun <E : RealmModel> insertJsonInDb(rl: Realm, clazz: Class<E>,
                                                inputStream: InputStream?,
                                                jsonWithIds: String) {
        try {
            rl.createAllFromJson(clazz, jsonWithIds)
            Timber.d("Initial Data importing is done")
        } catch (e: Exception) {
            handleImportError(e, rl)
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    Timber.e(e)
                }

            }
        }
    }

    private fun <E : RealmModel> insertJsonInDb(rl: Realm, clazz: Class<E>,
                                                jsonWithIds: String) {
        try {
            rl.createAllFromJson(clazz, jsonWithIds)
            Timber.d("Initial Data importing is done")
        } catch (e: Exception) {
            handleImportError(e, rl)
        }

    }

    private fun handleImportError(e: Exception, rl: Realm) {
        clearDb(rl)
        Timber.e(e)
    }

    private fun clearDb(rl: Realm) {
        // implement clean up there.
    }

    private fun insertNewlyGeneratedIdentifiers(input: String): String {
        val randomUtils = RandomUtils()
        return replaceAll(input, "#", Function { randomUtils.nextString() })
    }

    private fun getInputJson(inputStream: InputStream): String {
        val r = BufferedReader(InputStreamReader(inputStream))
        val total = StringBuilder()
        var line: String
        try {
            while (true) {
                line = r.readLine() ?: break
                total.append(line).append('\n')
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return total.toString()
    }

    private fun fileFound(name: String, file: File): Boolean {
        val list = file.listFiles()
        if (list != null)
            for (fil in list) {
                if (fil.isDirectory) {
                    fileFound(name, fil)
                } else if (name.equals(fil.name, ignoreCase = true)) {
                    return true
                }
            }
        return false
    }

}
