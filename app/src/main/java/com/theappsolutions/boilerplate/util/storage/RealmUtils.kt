package com.theappsolutions.boilerplate.util.storage

import io.realm.Realm
import io.realm.RealmModel

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Base Realm operations wrapper
 */
object RealmUtils {

    fun getRealm(): Realm = Realm.getDefaultInstance()

    fun <E : RealmModel> copyFromRealm(obj: E?, realm: Realm): E? {
        return when (obj) {
            null -> null
            else -> realm.copyFromRealm(obj)
        }
    }

    fun <E : RealmModel> copyFromRealm(
            obj: Iterable<E>?, realm: Realm): List<E>? {
        return when (obj) {
            null -> null
            else -> realm.copyFromRealm(obj)
        }
    }

    fun <E : RealmModel> saveToRealm(obj: E) {
        val realm = getRealm()
        realm.executeTransaction { it.copyToRealmOrUpdate(obj) }
        realm.close()
    }

    fun <E : RealmModel> saveToRealm(list: List<E>) {
        val realm = getRealm()
        realm.executeTransaction { it.copyToRealmOrUpdate(list) }
        realm.close()
    }

    fun <E : RealmModel> onlySaveToRealm(obj: E) {
        val realm = getRealm()
        realm.executeTransaction { it.copyToRealm(obj) }
        realm.close()
    }

    fun clearRealmTable(realmClass: Class<out RealmModel>) {
        val realm = getRealm()
        realm.executeTransaction { realm1 -> realm1.delete(realmClass) }
        realm.close()
    }
}
