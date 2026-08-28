package org.ldv.savonapi.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.rmi.server.UID
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

@Entity
class ResetMdp (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    open val id: Long? = null,
    val key: String,
    val codeSecret:Int= nextInt(100000, 999999),
    val expiration: LocalDateTime = LocalDateTime.now().plusMinutes(30L),
    val email: String,

){

}