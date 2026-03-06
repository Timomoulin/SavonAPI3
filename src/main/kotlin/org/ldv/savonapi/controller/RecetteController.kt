package org.ldv.savonapi.controller

import org.ldv.savonapi.dto.RecetteFormDTO
import org.ldv.savonapi.model.dao.RecetteDAO
import org.ldv.savonapi.model.entity.Recette
import org.ldv.savonapi.service.SimulateurService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Optional

/**
 * Contrôleur REST pour la gestion des recettes de savon.
 * Fournit des points d'accès pour créer, lire, mettre à jour, et gérer les recettes.
 *
 * @property simulateurService Service de simulation pour la gestion des recettes.
 * @property recetteDAO DAO pour accéder aux entités `Recette`.
 */
@RestController
@CrossOrigin
@RequestMapping("/api-savon/v1/recette")
class RecetteController(
    val simulateurService: SimulateurService,
    val recetteDAO: RecetteDAO
) {

    /**
     * Récupère toutes les recettes disponibles.
     *
     * @return Une liste de toutes les recettes existantes.
     */
    @GetMapping
    fun index(): List<Recette> {
        return this.recetteDAO.findAll()
    }

    /**
     * Récupère une recette spécifique à partir de son ID.
     *
     * @param id L'identifiant unique de la recette à récupérer.
     * @return Une réponse HTTP contenant la recette si elle existe,
     * ou un statut 404 si aucune recette n'est trouvée.
     */
    @GetMapping("/{id}")
    fun show(@PathVariable id: Long): ResponseEntity<Recette> {
        val recette = this.recetteDAO.findById(id)
        return if (recette.isPresent) {
            ResponseEntity.ok(recette.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Crée une nouvelle recette à partir des données fournies.
     *
     * @param recetteFormDTO Un objet `RecetteFormDTO` contenant les données de la recette à créer.
     * @return Une réponse HTTP avec la recette nouvellement créée et un statut HTTP 201 (Created).
     */
    @PostMapping
    fun store(@RequestBody recetteFormDTO: RecetteFormDTO): ResponseEntity<Recette> {
        val savedRecette = this.simulateurService.toRecette(recetteFormDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecette)
    }

    /**
     * Met à jour une recette existante identifiée par son ID.
     *
     * Si l'ID fourni dans l'URL ne correspond pas à une recette existante,
     * une nouvelle recette est créée avec cet ID.
     *
     * @param id L'identifiant unique de la recette à mettre à jour.
     * @param recetteFormDTO Un objet `RecetteFormDTO` contenant les nouvelles données de la recette.
     * @return Une réponse HTTP avec la recette mise à jour et un statut HTTP 201 (Created).
     */
    @PutMapping("/{id}")
    fun store(@PathVariable id: Long, @RequestBody recetteFormDTO: RecetteFormDTO): ResponseEntity<Recette> {
        recetteFormDTO.id = id
        val savedRecette = this.simulateurService.toRecette(recetteFormDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecette)
    }

    /**
     * Supprime une recette de la base de données en fonction de son ID.
     *
     * @param id L'identifiant de la recette à supprimer.
     * @return Une réponse HTTP :
     * - `204 No Content` si la suppression a été effectuée avec succès.
     * - `404 Not Found` si aucune recette avec l'ID spécifié n'existe.
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        return if (recetteDAO.existsById(id)) {
            recetteDAO.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
