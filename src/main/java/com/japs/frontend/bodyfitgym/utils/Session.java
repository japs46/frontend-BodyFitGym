package com.japs.frontend.bodyfitgym.utils;

import com.japs.frontend.bodyfitgym.models.AuthResponse;

public final class Session {

    private static AuthResponse currentUser;

    private Session() {
    }

    public static void start(AuthResponse authResponse) {
        currentUser = authResponse;
    }

    public static void clear() {
        currentUser = null;
    }

    public static boolean isAuthenticated() {
        return currentUser != null && currentUser.getToken() != null;
    }

    public static String getToken() {
        return currentUser == null ? null : currentUser.getToken();
    }

    public static String getAuthorizationHeader() {
        String token = getToken();
        return token == null ? null : "Bearer " + token;
    }

    public static AuthResponse getCurrentUser() {
        return currentUser;
    }
}
