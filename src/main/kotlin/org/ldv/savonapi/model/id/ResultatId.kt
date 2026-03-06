package org.ldv.savonapi.model.id

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class ResultatId (
    var caracteristiqueId:Long,
    var recetteId:Long
): Serializable {
}