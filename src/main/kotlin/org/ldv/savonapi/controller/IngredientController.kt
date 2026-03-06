package org.ldv.savonapi.controller

import org.ldv.savonapi.model.dao.IngredientDAO
import org.ldv.savonapi.model.entity.Ingredient
import org.ldv.savonapi.model.entity.Recette
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Contrôleur REST pour la gestion des ingrédients.
 *
 * Fournit des endpoints pour effectuer des opérations CRUD (Create, Read, Update, Delete).
 */
@RestController
@CrossOrigin
@RequestMapping("/api-savon/v1/ingredient")
class IngredientController (val ingredientDAO: IngredientDAO) {

    /**
     * Récupère la liste de tous les ingrédients disponibles.
     *
     * @return Liste d'ingrédients.
     */
    @GetMapping
    fun index(): List<Ingredient> {
        return this.ingredientDAO.findAll()
    }

    /**
     * Récupère un ingrédient spécifique par son ID.
     *
     * @param id Identifiant de l'ingrédient.
     * @return L'ingrédient s'il est trouvé, sinon retourne `404 Not Found`.
     */
    @GetMapping("/{id}")
    fun show(@PathVariable id: Long): ResponseEntity<Ingredient> {
        val ingredient = ingredientDAO.findById(id)
        return if (ingredient.isPresent) {
            ResponseEntity.ok(ingredient.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Enregistre un nouvel ingrédient dans la base de données.
     *
     * @param ingredient L'ingrédient à enregistrer (données envoyées en JSON).
     * @return L'ingrédient créé avec un code HTTP 201 Created.
     */
    @PostMapping
    fun store(@RequestBody ingredient: Ingredient): ResponseEntity<Ingredient> {
        val savedIngredient = ingredientDAO.save(ingredient)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredient)
    }

    /**
     * Met à jour un ingrédient existant.
     *
     * @param id Identifiant de l'ingrédient à mettre à jour.
     * @param updatedIngredient Nouvelles valeurs de l'ingrédient.
     * @return L'ingrédient mis à jour ou un code 404 si l'ingrédient n'existe pas.
     */
    @PutMapping("/{id}")
    fun store(
        @PathVariable id: Long,
        @RequestBody updatedIngredient: Ingredient
    ): ResponseEntity<Ingredient> {
        return ingredientDAO.findById(id).map { existingIngredient ->
            existingIngredient.nom = updatedIngredient.nom
            existingIngredient.iode = updatedIngredient.iode
            existingIngredient.ins = updatedIngredient.ins
            existingIngredient.sapo = updatedIngredient.sapo
            existingIngredient.volMousse = updatedIngredient.volMousse
            existingIngredient.tenueMousse = updatedIngredient.tenueMousse
            existingIngredient.douceur = updatedIngredient.douceur
            existingIngredient.lavant = updatedIngredient.lavant
            existingIngredient.durete = updatedIngredient.durete
            existingIngredient.solubilite = updatedIngredient.solubilite
            existingIngredient.sechage = updatedIngredient.sechage
            existingIngredient.estCorpsGras = updatedIngredient.estCorpsGras

            ResponseEntity.ok(ingredientDAO.save(existingIngredient))
        }.orElse(ResponseEntity.notFound().build())

    }

    /**
     * Supprime un ingrédient de la base de données.
     *
     * @param id Identifiant de l'ingrédient à supprimer.
     * @return Code HTTP 204 No Content si la suppression réussit, sinon 404 Not Found.
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        return if (ingredientDAO.existsById(id)) {
            ingredientDAO.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Supprime tous les ingrédients de la base de données.
     *
     * @return Code HTTP 204 No Content si la suppression réussit.
     */
    @DeleteMapping("/all")
    fun deleteAll(): ResponseEntity<Void> {
        ingredientDAO.deleteAll()
        return ResponseEntity.noContent().build()
    }
}