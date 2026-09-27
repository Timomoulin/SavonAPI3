package org.ldv.savonapi.service

import org.ldv.savonapi.model.dao.CaracteristiqueDAO
import org.ldv.savonapi.model.dao.MentionDAO
import org.ldv.savonapi.model.dao.RoleDAO
import org.ldv.savonapi.model.entity.Caracteristique
import org.ldv.savonapi.model.entity.Mention
import org.ldv.savonapi.model.entity.Role
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.core.annotation.Order

@Component
@Order(1)
class DataInitializer(
    private val caracteristiqueDAO: CaracteristiqueDAO,
    private val mentionDAO: MentionDAO,
    private val roleDAO: RoleDAO
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        if (caracteristiqueDAO.count() == 0L) {
            val caracteristiques = listOf(
                Caracteristique(id = 1, nom = "Iode"),
                Caracteristique(id = 2, nom = "Indice INS"),
                Caracteristique(id = 3, nom = "Douceur"),
                Caracteristique(id = 4, nom = "Lavant"),
                Caracteristique(id = 5, nom = "Volume de mousse"),
                Caracteristique(id = 6, nom = "Tenue de mousse"),
                Caracteristique(id = 7, nom = "Dureté"),
                Caracteristique(id = 8, nom = "Solubilité"),
                Caracteristique(id = 9, nom = "Séchage")
            )

            caracteristiqueDAO.saveAll(caracteristiques)
        }

        if (mentionDAO.count() == 0L) {
            val caracteristiques = caracteristiqueDAO.findAll()
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

            mentionDAO.saveAll(mentions)
        }

        if (roleDAO.count() == 0L) {
            val roleAdmin = Role(
                id = 1,
                nom = "admin",
                nomLogic = "ROLE_ADMIN"
            )

            val roleUser = Role(
                id = 2,
                nom = "utilisateur",
                nomLogic = "ROLE_UTILISATEUR"
            )

            roleDAO.saveAll(listOf(roleAdmin, roleUser))
        }
    }
}