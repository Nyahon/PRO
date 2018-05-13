# Implémentation

## Base de données

Nous avons choisi d'utiliser une base de donnée MySQL car, ayant eu un cours de BDR,  il s'agit du type que l'on maitrise le mieux et il est relativement bien adapté aux besoin de notre projet. 

### Définition des tables

Period : 

	Cette table contient une liste de 15 périodes qui représente les différentes périodes possible dans l'horaire GAPS.
	Une période est identifiée par un numéro unique allant de 0 à 15 et représentant le numéro de la période sur l'horaire journalier de GAPS, se caractérise par une heure de début et une heure de fin.
	Chaque période est reliée à une ou plusieurs salles de classe.

Classroom :

	Cette table contient la liste des salles des campus de Cheseaux et Saint-Roch de l'HEIG-VD.
	Une salle est identifiée par son nom (A01, A02,...), et se caractérise par un boolean qui indique si elle est vérouillée ou non.
	La salle est relié à une ou plusieurs périodes, possède un équipement qui lui est propre, et se trouve dans un étage du batiment.

TakePlace :

	Le numéro d'une période P est relié à une salle de classe S par un `TakePlace`, cette relation se caractérise par une date.

Floor :

	Cette table contient la liste des étages des campus de Cheseaux et Saint-Roch de l'HEIG-VD.
	Une étage est identifiée par son nom (A, B,...), et se caractérise par le campus auquel il appartient.
	Un étage est relié à plusieurs salles et possède un équipement qui lui est propre.

ClassroomEquipments : 

    Cette table permet de spécifier en détails l'équipement présent dans une salle.
    L'équipement de la salle est identifiée par un numéro unique, et se caractérise par des boolean qui indique la présence d'un beamer, de prises éléctrique, d'ordinateurs et d'un tableau blanc ou noir.
    Un équipement est relié à une et une seule salle de classe.

FloorEquipments :

    Cette table permet de spécifier en détails l'équipement présent dans un étage.
    L'équipement d'un étage est identifiée par un numéro unique, et se caractérise par des boolean qui indique la présence de toilette pour homme, de toilette pour femme, d'une machine é cafée, d'un distributeur Selecta ou équivalent et d'un accès à une sortie du bâtiment.
    Un équipement est relié à un et un seul étage.

### Définition des méthodes

addPeriod :

    Permet d'ajouter une période à la table des période selon la définition ci-dessus.

addFloor

    Permet d'ajouter un étage à la table des étages selon la définition ci-dessus.

addClassroom

    Permet d'ajouter une salle à la table des salles selon la définition ci-dessus.

addTakePlace

    Permet d'ajouter un lien entre les salles et les périodes selon la définition ci-dessus.

addClassroomEquipment

    Permet d'ajouter un équipement à une des salles selon la définition ci-dessus.

addFloorEquipment

    Permet d'ajouter un équipement à un des étages selon la définition ci-dessus.

updateClassroomLock

    Permet de changer le fait qu'une salle soit vérouillée ou non.

updateClassroomEquipment 

```
Permet de changer l'équipement d'une salle.
```

updateFloorEquipment 

```
Permet de changer l'équipement d'un étage.
```

### Schéma relationnel



