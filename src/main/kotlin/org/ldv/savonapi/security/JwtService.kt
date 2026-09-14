package org.ldv.savonapi.security

import org.springframework.stereotype.Service
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.ldv.savonapi.model.dao.UtilisateurDAO
import org.springframework.beans.factory.annotation.Value
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService (
    val utilisateurDAO: UtilisateurDAO,
    @param:Value("\${jwt.secret}")
    val APP_S: String = "SavonApi"
) {

    private val secret: SecretKey = Keys.hmacShaKeyFor(
        APP_S.toByteArray()
    )

    fun generateToken(username: String): String {
        val user = utilisateurDAO.findByUsernameOrEmail(username,username)
        return Jwts.builder()
            .subject(username)
            .claim("role",user?.role?.nomLogic )
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

    fun extractClaim(token: String): String {
        val claims = Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims["role"].toString()
    }

    fun isTokenValid(token: String, username: String): Boolean {
        return extractUsername(token) == username
    }
}