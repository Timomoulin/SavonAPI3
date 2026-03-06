package org.ldv.savonapi.model.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import org.ldv.savonapi.model.id.ResultatId

@Entity
class Resultat (
    @EmbeddedId
    var resultatId: ResultatId,
    var score: Float,
    @ManyToOne
    @JoinColumn(name = "recette_id")
    @MapsId("recetteId")
    @JsonIgnore
    var recette: Recette? = null,
    @ManyToOne
    @MapsId("caracteristiqueId")
    @JoinColumn(name = "caracteristique_id")
    var caracteristique: Caracteristique? = null,
    @ManyToOne
    @JoinColumn(name = "mention_id")
    var mention: Mention? = null


){



}