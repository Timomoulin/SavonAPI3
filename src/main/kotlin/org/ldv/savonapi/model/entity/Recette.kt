package org.ldv.savonapi.model.entity

import jakarta.persistence.*

@Entity
class Recette(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    var titre: String,
    var description: String,
    var surgraissage: Float,
    var apportEnEau: Float,
    var avecSoude: Boolean,
    var concentrationAlcalin: Float,
    var qteAlcalin: Float,


    @OneToMany(mappedBy = "recette", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var ligneIngredients: MutableList<LigneIngredient> = mutableListOf(),
    @OneToMany(mappedBy = "recette", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var resultats: MutableList<Resultat> = mutableListOf()

) {
    /**
     * Calcule les scores non pondérés pour les caractéristiques INS et Iode, en fonction des ingrédients de la recette.
     *
     * - Le score INS est calculé en faisant la somme des INS de chaque ingrédient pondérée par son pourcentage dans la recette.
     * - Le score Iode est calculé de manière similaire, basé sur l'indice d'iode des ingrédients.
     *
     * Les scores calculés sont ensuite affectés aux caractéristiques correspondantes dans les résultats de la recette.
     */
    fun calculNonPondere() {
        val ins: Double = this.ligneIngredients.sumOf { it.ingredient!!.ins * it.pourcentage / 100.toDouble() }
        val iode: Double = this.ligneIngredients.sumOf { it.ingredient!!.iode * it.pourcentage / 100.toDouble() }
        this.resultats.find { it.caracteristique!!.nom == "Iode" }!!.score = iode.toFloat()
        this.resultats.find { it.caracteristique!!.nom == "Indice INS" }!!.score = ins.toFloat()
    }

    /**
     * Calcule les scores pondérés pour plusieurs caractéristiques de la recette, en prenant en compte le surgraissage.
     *
     * - Les scores sont calculés en fonction des propriétés des ingrédients et de leur pourcentage dans la recette.
     * - Les caractéristiques calculées incluent : Douceur, Lavant, Volume de mousse, Tenue de mousse, Dureté, Solubilité et Séchage.
     * - Le surgraissage modifie les scores selon des coefficients spécifiques pour chaque caractéristique.
     *
     * Les scores modifiés sont affectés aux caractéristiques correspondantes dans les résultats de la recette.
     **/
    fun calculPondere() {
        //Calcul des scores
        var douceur: Double = this.ligneIngredients.sumOf { it.ingredient!!.douceur * it.pourcentage / 100.toDouble() }
        var lavant: Double = this.ligneIngredients.sumOf { it.ingredient!!.lavant * it.pourcentage / 100.toDouble() }
        var volMousse: Double =
            this.ligneIngredients.sumOf { it.ingredient!!.volMousse * it.pourcentage / 100.toDouble() }
        var tenueMousse: Double =
            this.ligneIngredients.sumOf { it.ingredient!!.tenueMousse * it.pourcentage / 100.toDouble() }
        var durete: Double = this.ligneIngredients.sumOf { it.ingredient!!.durete * it.pourcentage / 100.toDouble() }
        var solubilite: Double =
            this.ligneIngredients.sumOf { it.ingredient!!.solubilite * it.pourcentage / 100.toDouble() }
        var sechage: Double = this.ligneIngredients.sumOf { it.ingredient!!.sechage * it.pourcentage / 100.toDouble() }

        //Modification avec le surgraissage
        douceur = douceur * (1 + 0.01494 * this.surgraissage)
        lavant = lavant * (1 + -0.01203 * this.surgraissage)
        volMousse = volMousse * (1 + -0.00702 * this.surgraissage)
        tenueMousse = tenueMousse * (1 + 0.01016 * this.surgraissage)
        durete = durete * (1 + -0.00602 * this.surgraissage)
        solubilite = solubilite * (1 + 0.00250 * this.surgraissage)
        sechage = sechage * (1 + -0.00503 * this.surgraissage)

        //Affectation aux resultats
        this.resultats.find { it.caracteristique!!.nom == "Douceur" }!!.score = douceur.toFloat()
        this.resultats.find { it.caracteristique!!.nom == "Lavant" }!!.score = lavant.toFloat()
        this.resultats.find { it.caracteristique!!.nom == "Volume de mousse" }!!.score = volMousse.toFloat()
        this.resultats.find { it.caracteristique!!.nom == "Tenue de mousse" }!!.score = tenueMousse.toFloat()
        this.resultats.find { it.caracteristique!!.nom == "Dureté" }!!.score = durete.toFloat()
        this.resultats.find { it.caracteristique!!.nom == "Solubilité" }!!.score = solubilite.toFloat()
        this.resultats.find { it.caracteristique!!.nom == "Séchage" }!!.score = sechage.toFloat()
    }

    /**
     * Calcule la quantité d'agent alcalin nécessaire pour la recette,
     * en tenant compte des ingrédients (la quantite et indice de sapo), du type d'alcalin, et du surgraissage.
     *
     * - Si l'alcalin est de la soude (NaOH), une formule spécifique est utilisée.
     * - Si l'alcalin est de la potasse (KOH), une autre formule est utilisée.
     * - La quantité finale est ajustée en fonction de la concentration de l'agent alcalin et du surgraissage.
     *
     * Le résultat est stocké dans la propriété `qteAlcalin` de la recette.
     **/
    fun calculQteAlcalin() {

        var qteAlcalinNormal = 0.0
        if (this.avecSoude) {
            qteAlcalinNormal = this.ligneIngredients.sumOf { (it.quantite * it.ingredient!!.sapo) * (40.0 / 56 / 1000) }

        } else {
            qteAlcalinNormal = this.ligneIngredients.sumOf { ((it.quantite * it.ingredient!!.sapo) / 1000.0) }
        }

        var qteAlcalin = qteAlcalinNormal / (concentrationAlcalin / 100)
        qteAlcalin = qteAlcalin - qteAlcalin * (surgraissage / 100)
        this.qteAlcalin = qteAlcalin.toFloat()

    }

    /**
     * Calcule l'apport en eau fourni par l'agent alcalin dans la recette.
     *
     * L'apport en eau est déterminé en fonction de la concentration d'alcalin et de sa quantité.
     */
    fun calculApportEau() {
        val concentrationEau = (100 - this.concentrationAlcalin) / 100
        val apportEau = this.qteAlcalin * concentrationEau
        this.apportEnEau = apportEau
    }

}