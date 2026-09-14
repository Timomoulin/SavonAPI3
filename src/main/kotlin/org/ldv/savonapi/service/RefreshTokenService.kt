package org.ldv.savonapi.service

import org.ldv.savonapi.model.dao.RefreshTokenDAO
import org.ldv.savonapi.model.entity.RefreshToken
import org.ldv.savonapi.model.entity.Utilisateur
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenDAO,
    private val tokenService: TokenService
) {

    /**
     * Crée un nouveau refresh token pour un utilisateur.
     *
     * Le token en clair est retourné au client.
     * Seul son hash est enregistré en base.
     */
    fun createRefreshToken(utilisateur: Utilisateur): String {

        val token = tokenService.generateToken()
        val tokenHash = tokenService.hashToken(token)

        val refreshToken = RefreshToken(
            tokenHash = tokenHash,
            expiration = LocalDateTime.now().plusDays(7),
            utilisateur = utilisateur
        )

        refreshTokenRepository.save(refreshToken)

        return token
    }


    /**
     * Recherche un refresh token à partir du token
     * fourni par le client.
     */
    fun findValidRefreshToken(token: String): RefreshToken? {

        val tokenHash = tokenService.hashToken(token)

        val refreshToken =
            refreshTokenRepository.findByTokenHash(tokenHash)
                ?: return null

        if (refreshToken.estRevoque) {
            return null
        }

        if (LocalDateTime.now().isAfter(refreshToken.expiration)) {
            return null
        }

        return refreshToken
    }


    /**
     * Révoque un refresh token.
     */
    fun revoke(refreshToken: RefreshToken) {

        refreshToken.estRevoque = true

        refreshTokenRepository.save(refreshToken)
    }


    /**
     * Révoque tous les refresh tokens d'un utilisateur.
     */
    fun revokeAll(utilisateur: Utilisateur) {

        val tokens =
            refreshTokenRepository.findByUtilisateur_Id(
                utilisateur.id!!
            )

        tokens.forEach {
            it.estRevoque = true
        }

        refreshTokenRepository.saveAll(tokens)
    }
}