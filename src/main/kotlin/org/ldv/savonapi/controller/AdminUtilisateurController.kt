package org.ldv.savonapi.controller

import org.ldv.savonapi.dto.UtilisateurDTO
import org.ldv.savonapi.model.dao.RoleDAO
import org.ldv.savonapi.model.dao.UtilisateurDAO
import org.ldv.savonapi.model.entity.Recette
import org.ldv.savonapi.model.entity.Utilisateur
import org.ldv.savonapi.service.UtilisateurService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api-savon/v1/utilisateur")
class AdminUtilisateurController (
    val utilisateurDAO: UtilisateurDAO,
    val utilisateurService: UtilisateurService
){
    /*
      * ======================
      * CRUD ADMIN
      * ======================
      */

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun index(): List<UtilisateurDTO> {
        return utilisateurDAO.findAll()
            .map { utilisateurService.toDTO(it) }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun show(@PathVariable id: Long): ResponseEntity<UtilisateurDTO> {

        val utilisateur = utilisateurDAO.findById(id)

        return if (utilisateur.isPresent) {
            ResponseEntity.ok(utilisateurService.toDTO(utilisateur.get()))
        } else {
            ResponseEntity.notFound().build()
        }
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody dto: UtilisateurDTO): ResponseEntity<UtilisateurDTO> {

        val entity = utilisateurService.toEntity(dto)

        val saved = utilisateurDAO.save(entity)

        return ResponseEntity.ok(utilisateurService.toDTO(saved))
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: UtilisateurDTO
    ): ResponseEntity<UtilisateurDTO> {

        val updatedDto = dto.copy(id = id)

        val entity = utilisateurService.toEntity(updatedDto)

        val saved = utilisateurDAO.save(entity)

        return ResponseEntity.ok(utilisateurService.toDTO(saved))
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {

        if (!utilisateurDAO.existsById(id)) {
            return ResponseEntity.notFound().build()
        }

        utilisateurDAO.deleteById(id)

        return ResponseEntity.noContent().build()
    }


}