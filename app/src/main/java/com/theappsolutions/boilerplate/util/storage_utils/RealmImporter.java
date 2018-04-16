package com.theappsolutions.boilerplate.util.storage_utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.annimon.stream.function.Function;
import com.theappsolutions.boilerplate.TasBoilerplateSettings;
import com.theappsolutions.boilerplate.util.data_utils.RandomUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmModel;
import timber.log.Timber;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Manger that provides importing Realm database from json
 */
public class RealmImporter {

    private Context context;
    private Resources resources;

    public RealmImporter(Context context) {
        this.context = context;
        this.resources = context.getResources();
    }

    private static String replaceAll(String source, String regex,
                                     Function<String, String> replacement) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            matcher.appendReplacement(sb, replacement.apply(matcher.group(0)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private boolean isDbDoesNotExists(Context context) {
        return !fileFound(TasBoilerplateSettings.REALM_DB_NAME, context.getFilesDir());
    }

    private <E extends RealmModel> void insertJsonInDb(Realm rl, Class<E> clazz,
                                                       InputStream inputStream,
                                                       String jsonWithIds) {
        try {
            rl.createAllFromJson(clazz, jsonWithIds);
            Timber.d("Initial Data importing is done");
        } catch (Exception e) {
            handleImportError(e, rl);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }
    }

    private <E extends RealmModel> void insertJsonInDb(Realm rl, Class<E> clazz,
                                                       String jsonWithIds) {
        try {
            rl.createAllFromJson(clazz, jsonWithIds);
            Timber.d("Initial Data importing is done");
        } catch (Exception e) {
            handleImportError(e, rl);
        }
    }

    private void handleImportError(Exception e, Realm rl) {
        clearDb(rl);
        Timber.e(e);
    }

    private void clearDb(Realm rl) {
        // implement clean up there.
    }

    @NonNull
    private String insertNewlyGeneratedIdentifiers(String input) {
        RandomUtils randomUtils = new RandomUtils();
        return replaceAll(input, "#", s -> randomUtils.nextString());
    }

    @NonNull
    private String getInputJson(InputStream inputStream) {
        BufferedReader r =
                new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }

    private boolean fileFound(String name, File file) {
        File[] list = file.listFiles();
        if (list != null)
            for (File fil : list) {
                if (fil.isDirectory()) {
                    fileFound(name, fil);
                } else if (name.equalsIgnoreCase(fil.getName())) {
                    return true;
                }
            }
        return false;
    }

}
