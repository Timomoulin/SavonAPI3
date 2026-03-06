package org.ldv.savonapi.model.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import org.ldv.savonapi.model.id.LigneIngredientId

@Entity
class LigneIngredient (

    @EmbeddedId
    var ligneIngredientId: LigneIngredientId? = null,
    var quantite:Float,
    var pourcentage:Float,
    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    var ingredient: Ingredient? = null,
    @ManyToOne
    @MapsId("recetteId")
    @JoinColumn(name = "recette_id")
    @JsonIgnore
    var recette: Recette? = null

) {



}