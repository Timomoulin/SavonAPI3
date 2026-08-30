
package org.ldv.savonapi.service

import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

@Service
class TokenService {

    private val secureRandom = SecureRandom()

    /**
     * Génère un token aléatoire cryptographiquement sécurisé.
     */
    fun generateToken(): String {

        val bytes = ByteArray(32)

        secureRandom.nextBytes(bytes)

        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)
    }

    /**
     * Hash un token avec SHA-256.
     */
    fun hashToken(token: String): String {

        val digest = MessageDigest.getInstance("SHA-256")

        val hash = digest.digest(token.toByteArray(Charsets.UTF_8))

        return hash.joinToString("") {
            "%02x".format(it)
        }
    }
}

