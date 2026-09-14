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
class RefreshToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    open val id: Long? = null,

    @Column(nullable = false, unique = true)
    open val tokenHash: String,

    @Column(nullable = false)
    open val expiration: LocalDateTime,

    @Column(nullable = false)
    open var estRevoque: Boolean = false,

    @ManyToOne(optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    open val utilisateur: Utilisateur
)