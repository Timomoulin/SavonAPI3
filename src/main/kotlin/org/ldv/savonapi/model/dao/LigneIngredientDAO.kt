package org.ldv.savonapi.model.dao

import org.ldv.savonapi.model.entity.LigneIngredient
import org.springframework.data.jpa.repository.JpaRepository

interface LigneIngredientDAO : JpaRepository<LigneIngredient, LigneIngredient> {
}