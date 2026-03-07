package org.ldv.savonapi.service

import org.ldv.savonapi.model.dao.UtilisateurDAO
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val utilisateurRepository: UtilisateurDAO
) : UserDetailsService {

    override fun loadUserByUsername(identifier: String): UserDetails {

        val utilisateur = utilisateurRepository
            .findByUsernameOrEmail(identifier, identifier)
            ?: throw UsernameNotFoundException("Utilisateur introuvable")

        if (utilisateur.estBanned) {
            throw RuntimeException("Utilisateur banni")
        }

        val authority = SimpleGrantedAuthority(utilisateur.role.nomLogic)

        return User(
            utilisateur.username,
            utilisateur.password,
            listOf(authority)
        )
    }
}