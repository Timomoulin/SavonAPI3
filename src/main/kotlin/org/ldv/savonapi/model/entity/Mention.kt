package org.ldv.savonapi.model.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


@Entity
class Mention(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    var label: String,
    var noteMin: Float,
    var noteMax: Float,
    @ManyToOne
    @JoinColumn(name = "caracteristique_id")
    @JsonIgnore
    var caracteristique: Caracteristique? = null,
    @JsonIgnore
    @OneToMany(mappedBy = "mention", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var resultats: MutableList<Resultat> = mutableListOf()


) {


}