package org.ldv.savonapi.model.dao

import org.ldv.savonapi.model.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleDAO : JpaRepository<Role, Long> {


    fun findByNomLogic(nomLogic: String): Role?

}