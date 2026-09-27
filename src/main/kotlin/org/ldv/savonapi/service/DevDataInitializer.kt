package org.ldv.savonapi.service

import org.ldv.savonapi.model.dao.*
import org.ldv.savonapi.model.entity.*
import org.ldv.savonapi.model.id.LigneIngredientId
import org.ldv.savonapi.model.id.ResultatId
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.core.annotation.Order

@Component
@Profile("dev")
@Order(2)
class DevDataInitializer(
    private val ingredientDAO: IngredientDAO,
    private val caracteristiqueDAO: CaracteristiqueDAO,
    private val mentionDAO: MentionDAO,
    private val recetteDAO: RecetteDAO,
    private val resultatDAO: ResultatDAO,
    private val roleDAO: RoleDAO,
    private val utilisateurDAO: UtilisateurDAO,
    private val passwordEncoder: PasswordEncoder,
    private val ligneIngredientDAO: LigneIngredientDAO
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        if (ingredientDAO.count() == 0L) {
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

            ingredientDAO.saveAll(listOf(coco, olive))
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
                ligneIngredients = mutableListOf()
            )

            val recette2 = Recette(
                titre = "Savon Hydratant Reduction 2",
                description = "Un savon doux et hydratant pour la peau sensible.",
                surgraissage = 5.0f,
                avecSoude = true,
                concentrationAlcalin = 30.0f,
                apportEnEau = 379.99997f,
                qteAlcalin = 542.8571f,
                ligneIngredients = mutableListOf()
            )

            recetteDAO.saveAll(listOf(recette1, recette2))

            val ligne1 = LigneIngredient(
                quantite = 500.0f,
                pourcentage = 50.0f,
                ingredient = coco,
                recette = recette1,
                ligneIngredientId = LigneIngredientId(
                    ingredientId = 1L,
                    recetteId = recette1.id!!
                )
            )

            val ligne2 = LigneIngredient(
                quantite = 500.0f,
                pourcentage = 50.0f,
                ingredient = olive,
                recette = recette1,
                ligneIngredientId = LigneIngredientId(
                    ingredientId = 2L,
                    recetteId = recette1.id!!
                )
            )

            val ligne3 = LigneIngredient(
                quantite = 250.0f,
                pourcentage = 25.0f,
                ingredient = olive,
                recette = recette2,
                ligneIngredientId = LigneIngredientId(
                    ingredientId = 2L,
                    recetteId = recette2.id!!
                )
            )

            val ligne4 = LigneIngredient(
                quantite = 750.0f,
                pourcentage = 75.0f,
                ingredient = coco,
                recette = recette2,
                ligneIngredientId = LigneIngredientId(
                    ingredientId = 1L,
                    recetteId = recette2.id!!
                )
            )

            ligneIngredientDAO.saveAll(
                listOf(ligne1, ligne2, ligne3, ligne4)
            )

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

        if (utilisateurDAO.count() == 0L) {
            val admin = Utilisateur(
                username = "Nimda",
                email = "admin@email.com",
                password = passwordEncoder.encode("admin@email.com1"),
                estActif = true,
                role = roleDAO.findById(1).orElseThrow()
            )

            val user = Utilisateur(
                username = "Ruetasilitu",
                email = "utilisateur@email.com",
                password = passwordEncoder.encode("utilisateur@email.com1"),
                estActif = true,
                role = roleDAO.findById(2).orElseThrow()
            )

            val adm = Utilisateur(
                username = "adm",
                email = "adm@email.com",
                password = passwordEncoder.encode("123"),
                estActif = true,
                role = roleDAO.findById(1).orElseThrow()
            )

            utilisateurDAO.saveAll(listOf(admin, user, adm))
        }
    }
}