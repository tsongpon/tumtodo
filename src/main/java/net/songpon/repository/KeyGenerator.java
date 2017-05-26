package net.songpon.repository;

import java.util.UUID;

/**
 *
 */
class KeyGenerator {

    private KeyGenerator() {
        //Intentional, prevent instanciate
    }

    static String generateKey() {
        return UUID.randomUUID().toString();
    }
}
