package org.ldv.savonapi.model.dao

import org.ldv.savonapi.model.entity.Utilisateur
import org.springframework.data.jpa.repository.JpaRepository

interface UtilisateurDAO : JpaRepository<Utilisateur, Long> {
    fun findByUsernameOrEmail(username: String, email: String): Utilisateur?
    fun findByUsername(username: String): Utilisateur?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}