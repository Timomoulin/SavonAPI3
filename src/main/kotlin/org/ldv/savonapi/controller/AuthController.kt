package org.ldv.savonapi.controller
import org.ldv.savonapi.dto.RequeteInscription
import org.ldv.savonapi.dto.RequeteLogin
import org.ldv.savonapi.model.dao.RoleDAO
import org.ldv.savonapi.model.dao.UtilisateurDAO
import org.ldv.savonapi.model.entity.Utilisateur
import org.ldv.savonapi.security.JwtService
import org.springframework.security.access.prepost.PreAuthorize

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val utilisateurRepository: UtilisateurDAO,
    private val roleRepository: RoleDAO,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    fun login(@RequestBody request: RequeteLogin): Map<String, String> {

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.identifier,
                request.password
            )
        )

        val token = jwtService.generateToken(request.identifier)

        return mapOf("token" to token)
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    fun register(@RequestBody request: RequeteInscription): Map<String, String> {

        if (utilisateurRepository.existsByUsername(request.username)) {
            throw RuntimeException("Username déjà utilisé")
        }

        if (utilisateurRepository.existsByEmail(request.email)) {
            throw RuntimeException("Email déjà utilisé")
        }

        val roleUser = roleRepository.findByNomLogic("ROLE_UTILISATEUR")
            ?: throw RuntimeException("Role introuvable")

        val utilisateur = Utilisateur(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = roleUser
        )

        utilisateurRepository.save(utilisateur)

        val token = jwtService.generateToken(utilisateur.username)

        return mapOf("token" to token)
    }
}