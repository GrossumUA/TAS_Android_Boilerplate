package com.theappsolutions.boilerplate.util.storage_utils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Base Realm operations wrapper
 */
public class RealmUtils {

    public static <E extends RealmModel> E copyFromRealm(E obj, Realm realm) {
        if (obj == null) return null;
        return realm.copyFromRealm(obj);
    }

    public static <E extends RealmModel> List<E> copyFromRealm(
            Iterable<E> obj, Realm realm) {
        if (obj == null) return null;
        return realm.copyFromRealm(obj);
    }

    public static <E extends RealmModel> void saveToRealm(E obj) {
        Realm realm = getRealm();
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(obj));
        realm.close();
    }

    public static <E extends RealmModel> void saveToRealm(List<E> list) {
        Realm realm = getRealm();
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(list));
        realm.close();
    }

    public static <E extends RealmModel> void onlySaveToRealm(E obj) {
        Realm realm = getRealm();
        realm.executeTransaction(realm1 -> realm1.copyToRealm(obj));
        realm.close();
    }

    public static void clearRealmTable(Class<? extends RealmModel> realmClass) {
        Realm realm = getRealm();
        realm.executeTransaction(realm1 -> realm1.delete(realmClass));
        realm.close();
    }

    public static Realm getRealm() {
        return Realm.getDefaultInstance();
    }
}
