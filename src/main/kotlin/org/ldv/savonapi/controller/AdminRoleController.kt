package org.ldv.savonapi.controller

import org.ldv.savonapi.model.dao.RoleDAO
import org.ldv.savonapi.model.entity.Role
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api-savon/v1/role")
class AdminRoleController (val roleDao: RoleDAO){
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun index():List<Role>{
        return roleDao.findAll()
    }
}