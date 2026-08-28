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
    @Column(nullable = false, unique = true)
    val tokenHash:String,
    //TODO Expiration du token 24H
    @Column(nullable = false)
    val expiration: LocalDateTime = LocalDateTime.now().plusDays(1L),
    @ManyToOne( optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    open val utilisateur: Utilisateur

) {

}