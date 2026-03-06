package org.ldv.savonapi.service
import org.ldv.savonapi.model.dao.*
import org.ldv.savonapi.model.entity.*
import org.ldv.savonapi.model.id.ResultatId
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
@Component
class DataInitializer (val ingredientDAO: IngredientDAO, val caracteristiqueDAO: CaracteristiqueDAO,val mentionDAO: MentionDAO,val recetteDAO: RecetteDAO,val resultatDAO: ResultatDAO) : CommandLineRunner {
    override fun run(vararg args: String?) {
        //Pour importer les ingredients
        if (ingredientDAO.count() == 0L) { // Éviter les doublons
            val coco = Ingredient(
                id = 1,
                nom = "Coco",
                iode = 9f,
                ins = 248f,
                sapo = 257f,
                volMousse = 13.326f,
                tenueMousse = 9.560f,
                lavant = 14.462f,
                douceur = 7.746f,
                durete = 9.390f,
                solubilite = 11.204f,
                sechage = 11.880f,
                estCorpsGras = true
            )

            val olive = Ingredient(
                id = 2,
                nom = "Olive",
                iode = 78f,
                ins = 111f,
                sapo = 189f,
                lavant = 10.192f,
                volMousse = 9.838f,
                tenueMousse = 9.152f,
                douceur = 9.260f,
                durete = 10.144f,
                solubilite = 9.298f,
                sechage = 10.194f,
                estCorpsGras = true
            )
            var ingredients = listOf<Ingredient>(coco,olive)
            ingredientDAO.saveAll(ingredients)
        }
        if (caracteristiqueDAO.count() == 0L) {

            val iode = Caracteristique(
                id = 1,
                nom = "Iode"
            )

            val ins = Caracteristique(
                id = 2,
                nom = "Indice INS"
            )

            val douceur = Caracteristique(
                id = 3,
                nom = "Douceur"
            )

            val lavant = Caracteristique(
                id = 4,
                nom = "Lavant"
            )

            val volMousse = Caracteristique(
                id = 5,
                nom = "Volume de mousse"
            )

            val tenueMousse = Caracteristique(
                id = 6,
                nom = "Tenue de mousse"
            )

            val durete = Caracteristique(
                id = 7,
                nom = "Dureté"
            )

            val solubilite = Caracteristique(
                id = 8,
                nom = "Solubilité"
            )

            val sechage = Caracteristique(
                id = 9,
                nom = "Séchage"
            )
            val liste= mutableListOf(iode,ins,douceur,lavant,volMousse,tenueMousse,durete,solubilite,sechage)
            caracteristiqueDAO.saveAll(liste)
        }
        if (mentionDAO.count() == 0L) {
            // Récupérez les caractéristiques précédemment sauvegardées
            val caracteristiques = caracteristiqueDAO.findAll()

            // Création des mentions pour chaque caractéristique
            val mentions = mutableListOf<Mention>()

            caracteristiques.forEach { caracteristique ->
                when (caracteristique.nom) {
                    "Iode" -> {
                        mentions.add(Mention(label = "Très faible", noteMin = 0f, noteMax = 30f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Faible", noteMin = 30f, noteMax = 70f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Élevé", noteMin = 70f, noteMax = 100f, caracteristique = caracteristique))
                    }
                    "Indice INS" -> {
                        mentions.add(Mention(label = "Faible", noteMin = 0f, noteMax = 100f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Optimal", noteMin = 100f, noteMax = 160f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Trop élevé", noteMin = 160f, noteMax = 200f, caracteristique = caracteristique))
                    }
                    "Douceur" -> {
                        mentions.add(Mention(label = "Insuffisante", noteMin = 0f, noteMax = 5f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Bonne", noteMin = 5f, noteMax = 10f, caracteristique = caracteristique))
                    }
                    "Lavant" -> {
                        mentions.add(Mention(label = "Faible", noteMin = 0f, noteMax = 7f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Excellent", noteMin = 7f, noteMax = 15f, caracteristique = caracteristique))
                    }
                    "Volume de mousse" -> {
                        mentions.add(Mention(label = "Faible", noteMin = 0f, noteMax = 8f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Optimal", noteMin = 8f, noteMax = 15f, caracteristique = caracteristique))
                    }
                    "Tenue de mousse" -> {
                        mentions.add(Mention(label = "Peu stable", noteMin = 0f, noteMax = 5f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Stable", noteMin = 5f, noteMax = 10f, caracteristique = caracteristique))
                    }
                    "Dureté" -> {
                        mentions.add(Mention(label = "Mou", noteMin = 0f, noteMax = 5f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Dur", noteMin = 5f, noteMax = 10f, caracteristique = caracteristique))
                    }
                    "Solubilité" -> {
                        mentions.add(Mention(label = "Faible", noteMin = 0f, noteMax = 5f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Moyenne", noteMin = 5f, noteMax = 10f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Forte", noteMin = 10f, noteMax = 15f, caracteristique = caracteristique))
                    }
                    "Séchage" -> {
                        mentions.add(Mention(label = "Lent", noteMin = 0f, noteMax = 5f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Moyen", noteMin = 5f, noteMax = 10f, caracteristique = caracteristique))
                        mentions.add(Mention(label = "Rapide", noteMin = 10f, noteMax = 15f, caracteristique = caracteristique))
                    }
                }
            }

            // Sauvegardez toutes les mentions
            mentionDAO.saveAll(mentions)
        }
        if (recetteDAO.count() == 0L) {
            val coco = ingredientDAO.findById(1).orElseThrow()
            val olive = ingredientDAO.findById(2).orElseThrow()


            val recette1 = Recette(
                titre = "Savon Hydratant",
                description = "Un savon doux et hydratant pour la peau sensible.",
                surgraissage = 0.0f,
                avecSoude = true,
                concentrationAlcalin = 30.0f,
                apportEnEau = 371.66663f,
                qteAlcalin = 530.95233f,
                ligneIngredients = mutableListOf(
                    LigneIngredient(quantite = 500.0f, pourcentage = 50.0f, ingredient = coco),
                    LigneIngredient(quantite = 500.0f, pourcentage = 50.0f, ingredient = olive)
                )
            )


            val recette2 = Recette(
                titre = "Savon Hydratant Reduction 2",
                description = "Un savon doux et hydratant pour la peau sensible.",
                surgraissage = 5.0f,
                avecSoude = true,
                concentrationAlcalin = 30.0f,
                apportEnEau = 379.99997f,
                qteAlcalin = 542.8571f,
                ligneIngredients = mutableListOf(
                    LigneIngredient(quantite = 250.0f, pourcentage = 25.0f, ingredient = olive),
                    LigneIngredient(quantite = 750.0f, pourcentage = 75.0f, ingredient = coco)
                )
            )

            recetteDAO.saveAll(listOf(recette1, recette2))

            // Ajout des résultats pour chaque recette
            val resultatsRecette1 = listOf(
                Resultat(ResultatId(1, recette1.id!!), 43.5f, recette1, caracteristiqueDAO.findById(1).orElseThrow(), mentionDAO.findById(2).orElseThrow()),
                Resultat(ResultatId(2, recette1.id!!), 179.5f, recette1, caracteristiqueDAO.findById(2).orElseThrow(), mentionDAO.findById(6).orElseThrow()),
                Resultat(ResultatId(3, recette1.id!!), 8.503f, recette1, caracteristiqueDAO.findById(3).orElseThrow(), mentionDAO.findById(8).orElseThrow()),
                Resultat(ResultatId(4, recette1.id!!), 12.327f, recette1, caracteristiqueDAO.findById(4).orElseThrow(), mentionDAO.findById(10).orElseThrow()),
                Resultat(ResultatId(5, recette1.id!!), 11.582f, recette1, caracteristiqueDAO.findById(5).orElseThrow(), mentionDAO.findById(12).orElseThrow()),
                Resultat(ResultatId(6, recette1.id!!), 9.356f, recette1, caracteristiqueDAO.findById(6).orElseThrow(), mentionDAO.findById(14).orElseThrow()),
                Resultat(ResultatId(7, recette1.id!!), 9.767f, recette1, caracteristiqueDAO.findById(7).orElseThrow(), mentionDAO.findById(16).orElseThrow()),
                Resultat(ResultatId(8, recette1.id!!), 10.251f, recette1, caracteristiqueDAO.findById(8).orElseThrow(), mentionDAO.findById(19).orElseThrow()),
                Resultat(ResultatId(9, recette1.id!!), 11.037f, recette1, caracteristiqueDAO.findById(9).orElseThrow(), mentionDAO.findById(22).orElseThrow())
            )

            val resultatsRecette2 = listOf(
                Resultat(ResultatId(1, recette2.id!!), 26.25f, recette2, caracteristiqueDAO.findById(1).orElseThrow(), mentionDAO.findById(1).orElseThrow()),
                Resultat(ResultatId(2, recette2.id!!), 213.75f, recette2, caracteristiqueDAO.findById(2).orElseThrow(), null),
                Resultat(ResultatId(3, recette2.id!!), 8.7314f, recette2, caracteristiqueDAO.findById(3).orElseThrow(), mentionDAO.findById(8).orElseThrow()),
                Resultat(ResultatId(4, recette2.id!!), 12.5888f, recette2, caracteristiqueDAO.findById(4).orElseThrow(), mentionDAO.findById(10).orElseThrow()),
                Resultat(ResultatId(5, recette2.id!!), 12.0169f, recette2, caracteristiqueDAO.findById(5).orElseThrow(), mentionDAO.findById(12).orElseThrow()),
                Resultat(ResultatId(6, recette2.id!!), 9.9385f, recette2, caracteristiqueDAO.findById(6).orElseThrow(), mentionDAO.findById(14).orElseThrow()),
                Resultat(ResultatId(7, recette2.id!!), 9.2902f, recette2, caracteristiqueDAO.findById(7).orElseThrow(), mentionDAO.findById(16).orElseThrow()),
                Resultat(ResultatId(8, recette2.id!!), 10.8616f, recette2, caracteristiqueDAO.findById(8).orElseThrow(), mentionDAO.findById(19).orElseThrow()),
                Resultat(ResultatId(9, recette2.id!!), 11.1703f, recette2, caracteristiqueDAO.findById(9).orElseThrow(), mentionDAO.findById(22).orElseThrow())
            )

            resultatDAO.saveAll(resultatsRecette1 + resultatsRecette2)
        }


        }

}