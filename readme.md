# Gestion des Recettes de Savon - API REST

## Description

Ce projet est une API REST développée en Kotlin avec Spring Boot, conçue pour gérer des recettes de savon artisanal.  
Elle permet de créer, lire, mettre à jour et supprimer des recettes, de gérer les ingrédients associés, et de calculer automatiquement les propriétés des recettes (comme l'INS, l'iode, la douceur, etc.).

---

## Fonctionnalités

- **Gestion des recettes** :
    - Création de recettes avec des ingrédients et des caractéristiques spécifiques.
    - Consultation et mise à jour des recettes existantes.
    - Suppression sécurisée des recettes.

- **Calcul automatique** :
    - Calcul des propriétés pondérées et non pondérées (douceur, INS, iode, etc.).
    - Calcul de la quantité d'agent alcalin nécessaire, ajustée selon le surgraissage et la concentration.

- **Gestion des ingrédients** :
    - Association des ingrédients avec des pourcentages et propriétés spécifiques.
    - Calcul des contributions des ingrédients aux résultats de la recette.

---
**Technologies utilisées** :

- Kotlin : Langage principal pour le développement backend.
- Spring Boot : Framework pour la gestion des requêtes et des services REST.
- JPA/Hibernate : Gestion de la persistance des données.
- MySQL ou MariaDB : Base de données relationnelle (configurable selon vos besoins).
---
## Prérequis

- **Java 21** ou version ultérieure.
- **Kotlin 1.9.25+**.
- **Gradle** pour la gestion des dépendances.
- **Base de données relationnelle** (par exemple, MySQL ou MariaDB).
- Outil comme **Postman** ou **Bruno** pour tester l'API.

---
## Installation

1. **Cloner le projet** :

   ```bash
   git clone https://github.com/votre-repo/recette-savon.git
   cd recette-savon 
   ```
---
2. **Configurer la base de données** :
**Exemple**:
   spring.datasource.url=jdbc:mysql://localhost:3306/recette_savon
   spring.datasource.username=VotreUtilisateur
   spring.datasource.password=VotreMotDePasse
   spring.jpa.hibernate.ddl-auto=update
