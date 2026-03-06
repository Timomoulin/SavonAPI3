package org.ldv.savonapi.service

import org.ldv.savonapi.dto.LigneIngredientDTO
import org.ldv.savonapi.dto.RecetteFormDTO
import org.ldv.savonapi.model.dao.*
import org.ldv.savonapi.model.entity.LigneIngredient
import org.ldv.savonapi.model.entity.Recette
import org.ldv.savonapi.model.entity.Resultat
import org.ldv.savonapi.model.id.LigneIngredientId
import org.ldv.savonapi.model.id.ResultatId
import org.springframework.stereotype.Service

/**
 * Service pour gérer la simulation et la création de recettes de savon.
 *
 * @property caracteristiqueDAO DAO pour accéder aux caractéristiques.
 * @property recetteDAO DAO pour accéder aux recettes.
 * @property ingredientDAO DAO pour accéder aux ingrédients.
 * @property ligneIngredientDAO DAO pour gérer les lignes d'ingrédients.
 * @property mentionDAO DAO pour accéder aux mentions.
 * @property resultatDAO DAO pour accéder aux résultats.
 */
@Service
class SimulateurService(
    val caracteristiqueDAO: CaracteristiqueDAO,
    val recetteDAO: RecetteDAO,
    val ingredientDAO: IngredientDAO,
    val ligneIngredientDAO: LigneIngredientDAO,
    val mentionDAO: MentionDAO,
    val resultatDAO: ResultatDAO
) {

    /**
     * Convertit un DTO de recette en une entité `Recette`, la sauvegarde, et génère les résultats associés.
     *
     * @param recetteFormDTO DTO contenant les informations de la recette.
     * @return La recette sauvegardée avec ses lignes d'ingrédients et résultats associés.
     */
    fun toRecette(recetteFormDTO: RecetteFormDTO): Recette {
        var recette = Recette(
            recetteFormDTO.id,
            recetteFormDTO.titre,
            recetteFormDTO.description,
            recetteFormDTO.surgraissage,
            0f,
            recetteFormDTO.avecSoude,
            recetteFormDTO.concentrationAlcalin,
            0f,

        )
        recette = recetteDAO.save(recette)

        for (ligneDTO in recetteFormDTO.ligneIngredients) {
            val ligne = this.toLigne(ligneDTO, recette)
            recette.ligneIngredients.add(ligne)
        }

        recette.resultats.addAll(this.createResultats(recette))
        recette.calculPondere()
        recette.calculNonPondere()
        recette.calculQteAlcalin()
        recette.calculApportEau()

        ligneIngredientDAO.saveAll(recette.ligneIngredients)
        this.assignMentionsToResults(recette)
        resultatDAO.saveAll(recette.resultats)

        return recetteDAO.save(recette)
    }


    /**
     * Convertit un DTO de ligne d'ingrédient en une entité `LigneIngredient` associée à une recette.
     *
     * @param ligneIngredientDTO DTO contenant les informations de la ligne d'ingrédient.
     * @param recette La recette associée à la ligne d'ingrédient.
     * @return Une entité `LigneIngredient` prête à être sauvegardée.
     */
    fun toLigne(ligneIngredientDTO: LigneIngredientDTO, recette: Recette): LigneIngredient {
        val ingredient = ingredientDAO.findById(ligneIngredientDTO.ingredientId)
        val ligneIngredientId = LigneIngredientId(ligneIngredientDTO.ingredientId, recette.id!!)
        val savedLigne = LigneIngredient(
            ligneIngredientId,
            ligneIngredientDTO.quantite,
            ligneIngredientDTO.pourcentage,
            ingredient.get(),
            recette
        )
        return savedLigne
    }

    /**
     * Crée une liste de résultats pour une recette donnée, associant chaque caractéristique à un score initial de 0.
     *
     * @param recette La recette pour laquelle les résultats sont générés.
     * @return Une liste de résultats associés à la recette.
     */
    fun createResultats(recette: Recette): List<Resultat> {
        val resultats: MutableList<Resultat> = mutableListOf()
        val caracteristiques = caracteristiqueDAO.findAll()

        for (c in caracteristiques) {
            resultats.add(Resultat(resultatId = ResultatId(c.id!!, recette.id!!), 0f, recette, c))
        }
        return resultats
    }

    /**
     * Associe une mention à chaque résultat d'une recette en fonction du score et de la caractéristique.
     *
     * @param recette La recette dont les résultats doivent être mis à jour.
     * @return La recette mise à jour avec les mentions associées à ses résultats.
     */
    fun assignMentionsToResults(recette: Recette): Recette {
        recette.resultats.forEach { resultat ->
            val caracteristique = resultat.caracteristique

            if (caracteristique != null) {
                // Rechercher la mention correspondante directement via MentionRepository
                val mentionCorrespondante = mentionDAO.findMentionByScoreAndCaracteristique(
                    score = resultat.score,
                    caracteristique = caracteristique
                )

                // Assigner la mention trouvée au résultat
                if (mentionCorrespondante != null) {
                    resultat.mention = mentionCorrespondante
                } else {
                    println("Aucune mention trouvée pour le score ${resultat.score} et la caractéristique ${caracteristique.nom}")
                }
            }
        }
        return recette
    }


}