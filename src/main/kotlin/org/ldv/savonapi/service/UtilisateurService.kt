package org.ldv.savonapi.service

import jakarta.transaction.Transactional
import org.ldv.savonapi.dto.UtilisateurDTO
import org.ldv.savonapi.model.dao.ConfirmationUtilisateurDAO
import org.ldv.savonapi.model.dao.RefreshTokenDAO
import org.ldv.savonapi.model.dao.ResetMdpDAO
import org.ldv.savonapi.model.dao.RoleDAO
import org.ldv.savonapi.model.dao.UtilisateurDAO
import org.ldv.savonapi.model.entity.Utilisateur
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UtilisateurService (
    val utilisateurDAO: UtilisateurDAO,
    val passwordEncoder: PasswordEncoder,
    val refreshTokenRepository: RefreshTokenDAO,
    val confirmationUtilisateurRepository: ConfirmationUtilisateurDAO,
    val resetMdpRepository: ResetMdpDAO,
){
    fun toDTO(utilisateur: Utilisateur): UtilisateurDTO{
        val dto = UtilisateurDTO(id = utilisateur.id,username = utilisateur.username,email = utilisateur.email,role = utilisateur.role,estBanned = utilisateur.estBanned, recettes = utilisateur.recettes, estActif = utilisateur.estActif, nouveauMotDePasse = null)
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
            entity.estActif = if(dto.estActif == null) entity.estActif else dto.estActif

        }
        else{
            if (dto.nouveauMotDePasse == null) {
                throw RuntimeException("Le nouveau mot de passe est requis")
            }
            entity = Utilisateur(username = dto.username, email = dto.email, role = dto.role, estBanned = dto.estBanned, estActif = false, recettes = mutableListOf(),password = passwordEncoder.encode(dto.nouveauMotDePasse!!))
        }
        return entity

    }

    @Transactional
    fun supprimerUtilisateur(id: Long) {

        refreshTokenRepository.deleteByUtilisateur_Id(id)
        confirmationUtilisateurRepository.deleteByUtilisateur_Id(id)
        resetMdpRepository.deleteByUtilisateur_Id(id)

        utilisateurDAO.deleteById(id)
    }
}