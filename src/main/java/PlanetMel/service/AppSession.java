package PlanetMel.service;

import PlanetMel.domain.UserAccount;

public final class AppSession {
    private static UserAccount currentUser;

    private AppSession() {}

    public static UserAccount getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserAccount user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}
