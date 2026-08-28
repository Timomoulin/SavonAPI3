package org.ldv.savonapi.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class ConfirmationUtilisateur (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id:Long?=null,
    val token:String,
    //TODO Expiration du token 1H
    expiration: LocalDateTime = LocalDateTime.now().plusHours(1L),
    @ManyToOne()
    @JoinColumn(name = "utilisateur_id")
    open val utilisateur: Utilisateur? = null

) {

}