package org.ldv.savonapi.model.dao

import org.ldv.savonapi.model.entity.ConfirmationUtilisateur
import org.springframework.data.jpa.repository.JpaRepository

interface ConfirmationUtilisateurDAO : JpaRepository<ConfirmationUtilisateur, Long> {
    fun findBytokenHash(token: String): ConfirmationUtilisateur?
    fun deleteByUtilisateur_Id(id: Long)
}