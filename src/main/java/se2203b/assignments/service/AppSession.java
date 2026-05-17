package se2203b.assignments.service;

import se2203b.assignments.domain.UserAccount;

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
