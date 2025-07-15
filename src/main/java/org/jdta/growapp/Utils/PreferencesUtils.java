package org.jdta.growapp.Utils;

import java.util.prefs.Preferences;

public class PreferencesUtils {
    private static final Preferences prefs = Preferences.userRoot().node("grow_app");
    private static final Preferences punfp = Preferences.userNodeForPackage(PreferencesUtils.class);

    private static final String KEY_SKIP_DELETE_CONFIRM = "skip_delete_confirm";


    public static void setKeySkipDeleteConfirm(boolean skip) {
        punfp.putBoolean(KEY_SKIP_DELETE_CONFIRM, skip);
    }

    public static boolean isSkipDeleteConfirm() {
        return punfp.getBoolean(KEY_SKIP_DELETE_CONFIRM, false);
    }

    public static void cleanAllPunfp() {
        try {
            punfp.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUser(int userId, String username) {
        prefs.putInt("saved_user_id", userId);
        prefs.put("saved_username", username);
    }

    public static void clearUser() {
        prefs.remove("saved_user_id");
        prefs.remove("saved_username");
    }

    public static int getSavedUserId() {
        return prefs.getInt("saved_user_id", -1);
    }

    public static String getSavedUsername() {
        return prefs.get("saved_username", "");
    }
}
