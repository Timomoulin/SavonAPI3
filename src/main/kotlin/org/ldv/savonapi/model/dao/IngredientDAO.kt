package org.ldv.savonapi.model.dao

import org.ldv.savonapi.model.entity.Ingredient
import org.springframework.data.jpa.repository.JpaRepository


interface IngredientDAO : JpaRepository<Ingredient, Long> {
}