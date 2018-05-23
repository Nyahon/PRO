# Page de titre avec logo HES-SO et HEIG-VD titre du projet indication nature du rapport (Cahier des charges, Manuel utilisateur...) nom des auteurs nom du destinataire du rapport date (par ex « mars 2018 »)
# Table des matières
# Introduction
## DARYLL client
DARYLL est un utilitaire de gestion d’horaire simple et intuitif. Il permet à des utilisateurs de facilement obtenir les horaires libres d’une ou plusieurs salles de l’HEIG-VD. Le résultat sera affiché soit sur le plan du bâtiment intégré à l’application soit dans un fichier texte externe imprimable selon le type de recherche.
## Prérequis
Étant un logiciel Java, DARYLL nécessite que l’utilisateur dispose de Java 8 ou plus sur sa machine afin de le faire fonctionner. Si ce n’est pas votre cas, vous pouvez télécharger et installer la dernière version de Java ici : <https://www.java.com/fr/download>/
# Interface
## Menu principal
**Photo de l’Interface principale numérotée.**
- 1) Plan du bâtiment (si une recherche a été faite au préalable)
- 2) Permet de voir la partie gauche du plan (si disponible)
- 3) Permet de voir la partie droite du plan (si disponible)
- 4) Choix du bâtiment
- 5) Affiche, sur le plan (1), l’horaire d’un étage selon la date et l’heure (8) sélectionnées.
- 6) Console d’information et légende
- 7) Définition de la position courante
- 8) Définition de la date et de l’heure pour la prochaine recherche
- 9) Permet de lancer une recherche.
- a) Par défaut, l’horaire de l’étage G de Cheseaux.
- b) Si la position courante (7) est définie, affichera toujours l’étage et la salle libre la plus proche.
- 10) Affiche le menu pour les modes de recherche avancée
- 11) Affiche le menu d’aide

## Menu mode de recherche ### Photo du menu Mode ### Mode horaire d’une salle
Pour avoir accès à l’horaire d’une salle en particulier pour la journée courante, il faut ouvrir le menu déroulant « Mode » (10) et sélectionner « Horaire d’une salle ».
**Photo de l’interface horaire de la salle**
- 12) Choix du numéro de la salle selon le format de l’HEIG-VD, par exemple H01.
- 13) Ouvre un fichier texte temporaire qui contient les disponibilités de la salle dans l’intervalle des périodes GAPS pour la journée courante.
### Mode créneaux horaire
Pour avoir accès à la recherche avancée, il faut ouvrir le menu déroulant « Mode » (10) et sélectionner « Recherche avancée ».
**Photo de l’interface Créneaux horaires numérotés**
- 14) Choix de la date de début de la plage horaire
- 15) Choix de la date de fin de la plage horaire
- 16) Choix de l’heure de début de la plage horaire
- 17) Choix de l’heure de fin de la plage horaire
- 18) Choix du bâtiment
- 19) Choix de l’étage
- 20) Choix de la salle
- 21) Ajout et suppression de formulaire, permettant d’effectuer une recherche multiple.
- 22) Ouvre un fichier texte temporaire qui contient les disponibilités des salles qui correspondent aux critères de recherche avancée.
## Menu d’aide ### Photo du menu Aide **À propos de DARYLL**
Pour afficher des informations relatives à DARYLL, il faut ouvrir le menu déroulant « Aide » (11) et sélectionner « À propos ».
# Table des figures

# Glossaire
