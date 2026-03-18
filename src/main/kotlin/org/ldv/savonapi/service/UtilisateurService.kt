package org.ldv.savonapi.service

import org.ldv.savonapi.dto.UtilisateurDTO
import org.ldv.savonapi.model.dao.RoleDAO
import org.ldv.savonapi.model.dao.UtilisateurDAO
import org.ldv.savonapi.model.entity.Utilisateur
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UtilisateurService (
    val utilisateurDAO: UtilisateurDAO,
    val passwordEncoder: PasswordEncoder
){
    fun toDTO(utilisateur: Utilisateur): UtilisateurDTO{
        val dto = UtilisateurDTO(id = utilisateur.id,username = utilisateur.username,email = utilisateur.email,role = utilisateur.role,estBanned = utilisateur.estBanned, recettes = utilisateur.recettes, nouveauMotDePasse = null)
        return dto
    }

    fun toEntity(dto: UtilisateurDTO): Utilisateur{

        var entity: Utilisateur
        if (dto.id != null) {
            val exist = utilisateurDAO.findById(dto.id)
            if (exist.isEmpty) throw RuntimeException("L'utilisateur n'existe pas")
            entity = exist.get()
            val mdp = if(dto.nouveauMotDePasse == null) entity.password else passwordEncoder.encode(dto.nouveauMotDePasse!!)

            entity.username = dto.username
            entity.email = dto.email
            entity.role = dto.role
            entity.estBanned = dto.estBanned
            entity.password = mdp

        }
        else{
            if (dto.nouveauMotDePasse == null) {
                throw RuntimeException("Le nouveau mot de passe est requis")
            }
            entity = Utilisateur(username = dto.username, email = dto.email, role = dto.role, estBanned = dto.estBanned, recettes = mutableListOf(),password = passwordEncoder.encode(dto.nouveauMotDePasse!!))
        }
        return entity

    }
}