package org.ldv.savonapi.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.security.SecureRandom
import java.time.LocalDateTime


@Entity
class ResetMdp (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    open val id: Long? = null,
    @Column(nullable = false,unique = true)
    val keyHash: String,
    @Column(nullable = false)
    val codeSecret:String= SecureRandom().nextInt(100000,1000000).toString(),
    @Column(nullable = false)
    val expiration: LocalDateTime = LocalDateTime.now().plusMinutes(30L),
    @ManyToOne(optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    open val utilisateur: Utilisateur,
    var estUtilise: Boolean = false
){

}