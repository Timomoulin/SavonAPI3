package org.ldv.savonapi.model.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class Caracteristique (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    var nom:String,
    @JsonIgnore
    @OneToMany(mappedBy = "caracteristique", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var mentions: MutableList<Mention> = mutableListOf(),
    @JsonIgnore
    @OneToMany(mappedBy = "caracteristique", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var resultats: MutableList<Resultat> = mutableListOf()
) {


}