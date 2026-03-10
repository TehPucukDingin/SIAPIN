package com.android.cobasiapin.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {

    private static FirebaseAuth auth;
    private static FirebaseDatabase database;
    private static FirebaseFirestore firestore;

    public static FirebaseAuth getAuth() {
        if (auth == null) auth = FirebaseAuth.getInstance();
        return auth;
    }

    public static DatabaseReference getDatabaseRef(String path) {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database.getReference(path);
    }

    public static FirebaseFirestore getFirestore() {
        if (firestore == null) firestore = FirebaseFirestore.getInstance();
        return firestore;
    }

    public static String getCurrentUid() {
        if (getAuth().getCurrentUser() != null) {
            return getAuth().getCurrentUser().getUid();
        }
        return null;
    }

    public static boolean isLoggedIn() {
        return getAuth().getCurrentUser() != null;
    }
}