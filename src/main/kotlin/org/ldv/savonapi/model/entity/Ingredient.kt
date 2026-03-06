package org.ldv.savonapi.model.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    var nom:String,
    var iode:Float,
    var ins:Float,
    var sapo:Float,
    var volMousse:Float,
    var tenueMousse:Float,
    var douceur:Float,
    var lavant:Float,
    var durete:Float,
    var solubilite:Float,
    var sechage:Float,
    var estCorpsGras:Boolean=true,
    @JsonIgnore
    @OneToMany(mappedBy = "ingredient", cascade = [CascadeType.PERSIST,CascadeType.REMOVE], orphanRemoval = true)
    var ligneIngredients: MutableList<LigneIngredient> = mutableListOf()

) {

}