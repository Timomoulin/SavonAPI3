package org.ldv.savonapi.model.dao

import org.ldv.savonapi.model.entity.ResetMdp
import org.springframework.data.jpa.repository.JpaRepository

interface ResetMdpDAO : JpaRepository<ResetMdp, Long> {


    fun findByKeyHash(keyHash: String): ResetMdp?


    fun findByUtilisateur_Id(id: Long): List<ResetMdp>
    fun deleteByUtilisateur_Id(id: Long)
}