package org.ldv.savonapi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ldv.savonapi.model.dao.CaracteristiqueDAO
import org.ldv.savonapi.model.entity.*
import org.ldv.savonapi.model.id.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class RecetteTests(
    @Autowired val caracteristiqueDAO: CaracteristiqueDAO
) {
    private lateinit var savon: Recette

    @BeforeEach
    fun setup() {

        /*
         * Les ingrédients nécessaires aux tests sont créés directement ici.
         * Les tests ne dépendent donc plus des données de démonstration
         * du profil DEV ni des identifiants présents en base.
         */
        val huileCoco = Ingredient(
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

        val huileOlive = Ingredient(
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

        /*
         * Les caractéristiques sont des données structurelles de SavApp.
         * Elles sont donc initialisées par DataInitializer, y compris
         * avec le profil TEST.
         */
        val iode = caracteristiqueDAO.findById(1).orElseThrow()
        val ins = caracteristiqueDAO.findById(2).orElseThrow()
        val douceur = caracteristiqueDAO.findById(3).orElseThrow()
        val lavant = caracteristiqueDAO.findById(4).orElseThrow()
        val volMousse = caracteristiqueDAO.findById(5).orElseThrow()
        val tenueMousse = caracteristiqueDAO.findById(6).orElseThrow()
        val durete = caracteristiqueDAO.findById(7).orElseThrow()
        val solubilite = caracteristiqueDAO.findById(8).orElseThrow()
        val sechage = caracteristiqueDAO.findById(9).orElseThrow()

        savon = Recette(
            id = 1,
            titre = "Savon Hydratant Reduction",
            description = "Un savon doux et hydratant pour la peau sensible.",
            surgraissage = 5f,
            apportEnEau = 353.0833f,
            avecSoude = true,
            concentrationAlcalin = 30f,
            qteAlcalin = 504.40475f,
            ligneIngredients = mutableListOf(
                LigneIngredient(
                    LigneIngredientId(ingredientId = 1, recetteId = 1),
                    quantite = 500f,
                    pourcentage = 50f,
                    ingredient = huileOlive
                ),
                LigneIngredient(
                    LigneIngredientId(ingredientId = 2, recetteId = 1),
                    quantite = 500f,
                    pourcentage = 50f,
                    ingredient = huileCoco
                )
            ),
            resultats = mutableListOf(
                Resultat(ResultatId(1, 1), score = 0f, caracteristique = iode),
                Resultat(ResultatId(2, 1), score = 0f, caracteristique = ins),
                Resultat(ResultatId(3, 1), score = 0f, caracteristique = douceur),
                Resultat(ResultatId(4, 1), score = 0f, caracteristique = lavant),
                Resultat(ResultatId(5, 1), score = 0f, caracteristique = volMousse),
                Resultat(ResultatId(6, 1), score = 0f, caracteristique = tenueMousse),
                Resultat(ResultatId(7, 1), score = 0f, caracteristique = durete),
                Resultat(ResultatId(8, 1), score = 0f, caracteristique = solubilite),
                Resultat(ResultatId(9, 1), score = 0f, caracteristique = sechage),
            )
        )
    }

    @Test
    fun `test calculApportEau`() {
        savon.calculApportEau()

        assertEquals(
            353.0833f,
            savon.apportEnEau,
            0.001f,
            "L'apport en eau doit être calculé correctement"
        )
    }

    @Test
    fun `test calculNonPondere`() {
        savon.calculNonPondere()

        val iode = savon.resultats
            .find { it.caracteristique?.nom == "Iode" }?.score!!

        val ins = savon.resultats
            .find { it.caracteristique?.nom == "Indice INS" }?.score!!

        assertEquals(
            43.5f,
            iode,
            0.1f,
            "Le score iode doit être correct"
        )

        assertEquals(
            179.5f,
            ins,
            0.1f,
            "Le score INS doit être correct"
        )
    }

    @Test
    fun `test calculPondere`() {
        savon.calculPondere()

        val douceur = savon.resultats
            .find { it.caracteristique?.nom == "Douceur" }?.score!!

        val lavant = savon.resultats
            .find { it.caracteristique?.nom == "Lavant" }?.score!!

        val volMousse = savon.resultats
            .find { it.caracteristique!!.nom == "Volume de mousse" }?.score!!

        val tenueMousse = savon.resultats
            .find { it.caracteristique!!.nom == "Tenue de mousse" }?.score!!

        val durete = savon.resultats
            .find { it.caracteristique!!.nom == "Dureté" }?.score!!

        val solubilite = savon.resultats
            .find { it.caracteristique!!.nom == "Solubilité" }?.score!!

        val sechage = savon.resultats
            .find { it.caracteristique!!.nom == "Séchage" }?.score!!

        assertEquals(9.138174f, douceur, 0.001f, "Le score douceur doit être correct")
        assertEquals(11.585531f, lavant, 0.001f, "Le score lavant doit être correct")
        assertEquals(11.175472f, volMousse, 0.1f, "Le score volume de mousse ne doit pas être modifié")
        assertEquals(9.831285f, tenueMousse, 0.1f, "Le score tenue de mousse ne doit pas être modifié")
        assertEquals(9.473014f, durete, 0.1f, "Le score dureté ne doit pas être modifié")
        assertEquals(10.379138f, solubilite, 0.1f, "Le score solubilité ne doit pas être modifié")
        assertEquals(10.759419f, sechage, 0.1f, "Le score séchage ne doit pas être modifié")
    }

    @Test
    fun `test calculQteAlcalin`() {
        savon.calculQteAlcalin()

        assertEquals(
            504.40475f,
            savon.qteAlcalin,
            0.001f,
            "La quantité d'alcalin doit être calculée correctement"
        )
    }
}