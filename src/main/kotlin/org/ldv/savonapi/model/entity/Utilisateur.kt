package org.ldv.savonapi.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
  class Utilisateur(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    open var id: Long? = null,
    open var username: String,
    open var email: String,
    open var password: String,
    var estBanned: Boolean = false,
    @ManyToOne
    @JoinColumn(name = "role_id")
    open var role: Role
){
 var dateCreation: LocalDateTime = LocalDateTime.now()



}
