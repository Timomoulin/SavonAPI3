package org.ldv.savonapi.dto

import org.ldv.savonapi.model.entity.Recette
import org.ldv.savonapi.model.entity.Role

data class UtilisateurDTO(
    val id: Long?,
    val username: String,
    val email: String,
    val nouveauMotDePasse: String?,
    val role: Role,
    val estBanned: Boolean,
    val recettes: MutableList<Recette>?
    )