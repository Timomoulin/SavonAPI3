package org.ldv.savonapi.model.dao

import org.ldv.savonapi.model.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenDAO : JpaRepository<RefreshToken, Long> {


    fun findByTokenHash(tokenHash: String): RefreshToken?


    fun findByUtilisateur_Id(id: Long): List<RefreshToken>
    fun deleteByUtilisateur_Id(id: Long)

}