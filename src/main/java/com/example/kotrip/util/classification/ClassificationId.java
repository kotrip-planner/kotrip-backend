package com.example.kotrip.util.classification;

import java.util.UUID;

public class ClassificationId {

    public static String getID(){
        UUID uuid = UUID.randomUUID();

        return String.valueOf(uuid);
    }
}
