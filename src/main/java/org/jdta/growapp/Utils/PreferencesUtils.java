package org.jdta.growapp.Utils;

import java.util.prefs.Preferences;

public class PreferencesUtils {
    private static final Preferences prefs = Preferences.userRoot().node("grow_app");

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
