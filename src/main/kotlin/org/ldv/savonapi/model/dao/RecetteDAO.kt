package org.ldv.savonapi.model.dao;

import org.ldv.savonapi.model.entity.Recette
import org.springframework.data.jpa.repository.JpaRepository


interface RecetteDAO : JpaRepository<Recette, Long> {
}