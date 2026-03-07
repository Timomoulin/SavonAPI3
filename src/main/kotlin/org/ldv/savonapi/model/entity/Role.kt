package org.ldv.savonapi.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Role (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,
    var nom: String,
    var nomLogic: String,
    @OneToMany(mappedBy = "role", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    open var utilisateurs: MutableSet<Utilisateur> = mutableSetOf()
) {


}