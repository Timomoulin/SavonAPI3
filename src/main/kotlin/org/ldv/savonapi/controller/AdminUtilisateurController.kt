package org.ldv.savonapi.controller

import org.ldv.savonapi.model.dao.RoleDAO
import org.ldv.savonapi.model.dao.UtilisateurDAO
import org.ldv.savonapi.model.entity.Recette
import org.ldv.savonapi.model.entity.Utilisateur
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api-savon/v1/utilisateur")
class AdminUtilisateurController (
    val utilisateurDAO: UtilisateurDAO,
    val roleDAO: RoleDAO
){
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun index():List<Utilisateur>{
        return utilisateurDAO.findAll()
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun show(id:Long):ResponseEntity<Utilisateur>{
        val utilisateur = utilisateurDAO.findById(id)
        if (utilisateur.isPresent){
            return ResponseEntity.ok(utilisateur.get())
        }
      return ResponseEntity.notFound().build()
    }
}