package org.ldv.savonapi.controller

import org.ldv.savonapi.dto.UtilisateurDTO
import org.ldv.savonapi.model.dao.UtilisateurDAO
import org.ldv.savonapi.service.UtilisateurService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api-savon/v1/profil")
class ProfilController (
    val utilisateurDAO: UtilisateurDAO,
    val utilisateurService: UtilisateurService
){
    /*
   * ======================
   * PROFIL UTILISATEUR
   * ======================
   */

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun profil(authentication: Authentication): UtilisateurDTO {

        val principal = authentication.principal as String

        val utilisateur = utilisateurDAO.findByUsernameOrEmail(principal,principal)
            ?: throw RuntimeException("Utilisateur introuvable")

        return utilisateurService.toDTO(utilisateur)
    }



    @PutMapping
    @PreAuthorize("isAuthenticated()")
    fun updateProfile(
        @RequestBody dto: UtilisateurDTO,
        authentication: Authentication
    ): UtilisateurDTO {

        val username = authentication.principal as String

        val utilisateur = utilisateurDAO.findByUsernameOrEmail(username,username)
            ?: throw RuntimeException("Utilisateur introuvable")

        val dtoWithId = dto.copy(id = utilisateur.id)

        val entity = utilisateurService.toEntity(dtoWithId)

        val saved = utilisateurDAO.save(entity)

        return utilisateurService.toDTO(saved)
    }
}