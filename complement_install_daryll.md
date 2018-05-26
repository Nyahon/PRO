## Explications concernant le dossier DARYLL

Le dossier `DARYLL` est créé au lancement du programme.
Au tout départ, il contient uniquement le fichier `daryll.properties` ainsi qu'un dossier `plans` vide.

Lorsque l'utilisateur charge un étage, les plans correspondant à cet étage sont extraits du fichier `Daryll_Client.jar`
dans ce dossier `plans`. Les couleurs sont modifiées en fonctions des disponibilités des salles puis le premier plan est affiché.


Lors d'une requête avancée, ou à la demande de l'horaire d'une salle, un fichier texte (`DARYLL.txt` par défaut) est généré.
Ce fichier texte se trouvera aussi dans le dossier `DARYLL`.
