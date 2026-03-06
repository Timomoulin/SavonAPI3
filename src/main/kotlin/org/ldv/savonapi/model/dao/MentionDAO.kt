package org.ldv.savonapi.model.dao

import org.ldv.savonapi.model.entity.Caracteristique
import org.ldv.savonapi.model.entity.Mention
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MentionDAO : JpaRepository<Mention, Long> {
    @Query("SELECT m FROM Mention m WHERE m.caracteristique = :caracteristique AND :score BETWEEN m.noteMin AND m.noteMax")
    fun findMentionByScoreAndCaracteristique(
        @Param("score") score: Float,
        @Param("caracteristique") caracteristique: Caracteristique
    ): Mention?
}