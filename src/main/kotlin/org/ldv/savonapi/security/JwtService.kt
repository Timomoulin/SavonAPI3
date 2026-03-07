package org.ldv.savonapi.security

import org.springframework.stereotype.Service
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    private val secret: SecretKey = Keys.hmacShaKeyFor(
        "super-secret-api-savon:3Z-a5b-@-42".toByteArray()
    )

    fun generateToken(username: String): String {
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(secret)
            .compact()
    }

    fun extractUsername(token: String): String {
        val claims = Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.subject
    }

    fun isTokenValid(token: String, username: String): Boolean {
        return extractUsername(token) == username
    }
}