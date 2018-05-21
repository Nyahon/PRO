# Page de titre
- avec logo HES-SO et HEIG-VD
- titre du projet
- indication nature du rapport (Cahier des charges, Manuel utilisateur, ...)
- nom des auteurs
- nom du destinataire du rapport
- date (par ex "mars 2018")





# Table des matières





# Introduction
Durant le quatrième semestre de la section TIC, nous devons effectuer un projet en groupes de cinq ou six personnes. Le but est de rassembler les connaissances que les étudiants ont aquises jusqu'à maintenant à travers un projet conséquent. Nous devrons prendre conscience des difficultés liées au travail de groupe, apprendre à planifier un projet sur plusieurs mois, gérer les conflits interne et apprendre à avoir une vision globale de l'architecture du projet.
Au terme du semestre, nous devons rendre un programme complet et fonctionnel, avec une documentation adéquate et être capables de le présenter et le défendre.
Le projet dure seize semaines et vaut trois crédits. Le temps de travail est de 540 heures pour toute l’équipe, soit cinq heures par membres du projet, par semaine.
Dans le cadre du projet, l'équipe de développement est composée du chef de projet Yohann Meyer, de son suppléent Loïc Frueh et des membres Aurélien Siu, Dejvid Muaremi, Labinot Rashiti, et Romain Gallay.

Dans ce rapport, nous allons expliquer notre démarche de travail, les principaux choix de conception et d'implémentation. Il sera structuré selon le modèle fournis.

Ce rapport, étant à rendre lors de la semaines treize, il ne contiendra pas les retours de la défense orale.


# Objectifs du projet
L'objectif de notre projet et de proposer un programme, DARYLL, un utilitaire permettant l'obtention des disponiblités des salles de cours de la HEIG. À l'heure actuelle, aucun outil interne de l'HEIG-VD ne permet d'obtenir cette information de manière viable.
Se basant principalement sur les données GAPS, DARYLL proposera une interface claire et rapide
aux utilisateur à la recherche d'une salle libre pour un ou plusieurs horaire(s) donné(s) et l'inverse.
Il permettra également d'imprimer un horaire selon les spécification de l'utilisateur.


# Conception et architecture du projet

## Technologies utilisées

### Java
Parmi les deux langages de haut niveau proposés pour le projet (Java ou C++), nous avons choisi d'utiliser Java pour sa portabilité, grâce à la Java virtual machine, sa sécurité et ses performance dont le garbage collector est particulièrement interessant par le fait qu'il libère la mémoire automatiquement. En effet, cette fonctionnalité nous semble plus que nécessaire quant tenu du fait que nous allons utiliser un transcodeur pour passer d'un plan SVG à une image Java.
De plus notre équipe est plus habile à programmer à l'aide du langage Java, et l'API JavaFX.   

#### JavaFX
Dans le cadre du développement de DARYLL, il nous a été imposé de choisir entre trois API pour l'interface graphique. Il fallait donc choisir entre Swing, JavaFX et Qt.

Notre choix s'est porté pour JavaFX, successeur de Swing en tant que librairie de création d'interface graphiques officielle du langage. Le SDK de JavaFX est intégré au JDK standard Java SE et dispose de plusieurs fonctionnalités dont les plus importantes sont les suivantes :

##### Scene FXML
Le FXML est un langage inspiré du HTML ou XML et indique la définition de l'interface graphique utilisateur. Dans notre cas, nous avons plusieurs fichiers FXML qui contiendront toutes les balises nécessaires à la représentation d'une "view". Chaque balise représentera donc un composant de cette vue. Grâce au langage FXML, il sera possible de personaliser chacune des balises avec des attributs et des valeurs. Cette personalisation des balises peut être comparé au CSS pour les fichiers HTML.

##### Scene Builder
Scene Builder de Gluon permet de manipuler des objets JavaFX graphiquement et d'exporter ceux-ci dans un fichier .fxml interprétable et modifiable par la librairie graphique. Il nous permet également de voir l'interface comme si le programme était en cours d'exécution et de pouvoir y personaliser directement les composants graphiques à l'aide de la souris.
Cette application nous simplifie donc la personalisation car il n'est pas nécessaire de réécrire tout le FXML. Il est à noter que le fichier FXML et Scene Builder sont liés. La modification de l'un met à jour directement la vue de l'autre !

### Maven
Comme on utilise plusieurs libraries externes, il devenait rapidement compliqué de gérer toutes les dépendance de notre programme. C'est pour Cette raison que nous avons choisi d'utiliser l'outil Maven de Apache, celui-ci permettra de complier et importer notre projet sur nos nombreux environnement de travail. L'autre avantages que nous offres Maven et qu'il permet de produire un fichier executable grâce à des instructions se trouvant dans le fichier pom.xml.

### Git
Git, le gestionnaire de version décentralisé libre, utilisé afin de gérer la totalité du projet ainsi que toutes ses modifications.
Nous avons créer une nouvelle branche par thème, c'est à dire, une branche pour le client, une branche pour le serveur, une pour l'adrministration et une pour les rendu.
Cet outil nous a été particulièrement utile pour pouvoir travailler en équipe et en même temps tout en nous assurons que nous avions bien tous la même version de DARYLL.

### GitHub
GitHub, le service web permettant de parcourir visuellement l'historique Git de DARYLL  et qui va également le stoquer.
GitHub offre également de nombreuses fonctionnalité ainsi que des outils de gestion pour le projet.

### MySQL
MySQL, le système de gestion de bases de données relationnelles (SGDBR), faisant partie des logiciels de gestion des bases de données les plus utilisés au monde.
Nous avons choisi de l'utiliser car c'est le modèle que nous maitrisions le mieux et que Java possède de nombreuses méthodes afin de nous permettre d'intéragir avec nos données.

### Inkscape
Inkscape, le logiciels libre de dessin vectoriel sous licence GNU GPL. Il nous a permis d'ouvrir les PFDs des plans du bâtiment afin de les modifier comme s'il s'agissait d'un SVG puis nous pouvons les exporter au format qui nous arranges le plus (SVG).

### Docker
Docker, le logiciel libre qui automatise le déploiement d'application dans des conteneurs logiciels. Ces conteneurs sont isolé et peuvent être executé sur n'importe quel système qui prend en charges Docker.
Ceci nous permet d'étendre la flexibilité et la portabilité de notre serveur.

## Architecture de la solution

### Définition de la base de données
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


Schema et commentaire global
---
Schema et commentaire sur l'interface graphique
---


# Description technique de l'implémentation

## Structure du programme

### L'interface graphique

#### Fonctionnement de JavaFX
Afin de comprendre comment marche JavaFX, il faut imaginer notre programme comme étant une pièce de théatre. JavaFX utilise cette image afin de structurer le programme ainsi que son interface graphique.

Nous nous retrouvons donc avec des termes comme "Stage", "Scene" et différents composants animant cette scène. Nous vous expliquerons nos différentes scènes et composants plus tard dans la documentation.

JavaFX intègre également la notion de MVC (Modèle-vue-contrôleur). Ils sont créés par défaut lors du commencement d'un projet. La classe principale étant le modèle, la vue étant le fichier FXML et le contrôleur étant le fichier Java gérant les interactions avec le fichier FXML.

#### Scène principale
Lors du début du projet, le programme était une simple application JavaFX. Le projet DARYLL possédait donc uniquement un fichier contenant sa classe, une vue vide (fenêtre principal ou scène principal) et le contrôleur de cette vue (donc selon le modèle MVC vu précèdemment).

La première tache a été de choisir le conteneur principal de DARYLL et pour cela, il y a plusieurs choix. Certains ont des avantages que d'autres n'ont pas. En réalité, le choix dépend de l'utilisation de l'application et du rendu final.

Nous avons essayé plusieurs conteneurs tel que le Anchor Pane, le Grid Pane ou le simple Pane mais notre choix a penché pour le Border Pane. Il aurait été possible de réaliser notre programme avec les autres conteneurs mentionnés, mais étant donné que nous avons décidé d'intégrer le redimentionnement de la fenêtre pour les plans des étages, l'implémentation aurait été plus compliquée et le Border Pane nous facilitait beaucoup plus la tâche à ce niveau là.

Le Border Pane est donc plus intéressant car il est déjà séparé en différentes zones (top, bottom, center, left, right), et grâce à ces zones, les composants prennent automatiquement la bonne taille avec le redimentionnement. L'autre aspect également important pour le Border Pane est le fait qu'il respecte le plus possible au mockup défini au début du projet.

#### Scènes secondaires
Par scènes secondaires, nous entendons les autres fenêtres ou pop-ups qui s'ajoute à la scène principale (fenêtre principal) de DARYLL. À la base, il n'y avais qu'une seule scène et un seul contrôleur qui gérait ladite scène.  Les autres scènes étaient créées directement dans le contrôleur principal via du code.

Cette solution fonctionnait bien mais n'e donnait pas un rendu comme nous l'avi

ons désiré. De plus, le contrôleur de la fenêtre principale devenait facilement énorme vu les nombreuses pop-ups que nous avons rajoutés pour les options du programme. Donc au final, cette solution ne donnait pas un bon rendu et n'était pas très évolutif car il est difficile de retrouver la bonne vue dans un seul fichier.

Après des recherches et reflexions, nous avons décidé de dispatcher le FXML en plusieurs parties. Dorénavant, chaque fenêtre graphique aura son propre fichier FXML et son propre contrôleur gérant les intéractions avec l'utilisateur de ladite fenêtre.

Cette nouvelle solution permet d'avoir un rendu nettement meilleur à l'affichage et rend le code beaucoup plus propre. En effet, lorsqu'il faudra mettre à jour ou dépanner une fenêtre du programme, il suffira d'aller dans son fichier FXML et contrôler pour effectuer les changements !

#### Gestion des évènements selon le composant ciblé
Cette aspect du projet mérite son propre titre de part la démarche réalisée afin d'optimiser le code.

Si nous regardons le mockup de base, nous aperçevons que la zone de gauche contient tous les boutons des étages. La démarche basique aurait été de créer une fonction pour chaque bouton d'étage dans le contrôleur de la vue et ensuite de les assigner une à une via Scene Builder. Cela veut dire que pour X boutons nous auront X fonctions.

Alors évidemment, cela fonctionnera, mais n'oublions pas que le contrôleur est déjà très conséquent en terme de code. Si il faut encore rajouter une fonction par étage en sachant que Cheseaux en a environ dix, le code deviendrait illisible et donc moins maintenable.

La nouvelle solution adopté a été de factoriser ce code. En effet, les boutons d'étages ont le même but : Afficher le plan de l'étage avec ses salles disponibles au moment X.

L'idée est donc de faire une fonction permettant de faire ce changement pour chaque bouton d'étage. Idéalement la fonction devra prendre le bouton ciblé en paramètre mais malheureusement Scene Builder ne le permet pas...

Après plusieurs recherches, nous avons trouvé une alternative que nous avons utilisée tout au long du projet pour l'interface graphique. En fait, cette alternative permet de retrouver le composant ciblé via un objet "Event". Cet objet "Event" nous permet de retrouver la scène où se trouve le composant ciblé puis de récupérer le composant via son ID.

Cela veut dire que la fonction gérant le changement de plan (showFloor) ne va pas prendre un bouton en paramètre mais un objet "Event" qui comprend le clic de la souris. C'est grâce à cette méthode que nous avons pu factoriser le code et le rendre plus propre.

#### Redimensionnement
L'interface graphique de DARYLL affiche les plans des étages de Cheseaux ou de Saint-Roch. Ces plans ont une certaine taille et cela nous amène à une question problématique sur l'affichage des plans sous différentes résolutions. Si nous imaginons notre application dans un cas concret d'utilisation, alors il se pourrait que l'utilisateur ait une mauvaise qualité d'image à cause de sa résolution d'écran.

C'est pour cela que DARYLL offre la possbilité de d'aggrandir ou de rapetissir la fenêtre afin que l'affichage du plan soit en adéquation avec la résolution de l'utilisateur. Alors bien sûr, cette fonctionnalité paraît toute simple mais en réalité la mise en place est très difficile. Nous allons vous expliquer la démarche que nous avons eu tout au long du projet dans les paragraphes qui suivent.

Suite à des réunions concernant cette question, le groupe s'est mis d'accord sur le fait qu'il était préférable d'intégrer le redimentionnement. L'idée initiale était d'afficher le plan sur un conteneur puis d'incruster des composants au dessus de chaque salle de l'étage afin d'indiquer le niveau de disponibilité de la salle avec un teint de vert. L'implémentation fut difficile car il fallait faire attention à ce que les composants restent correctement placés sur les étages lorsque l'utilisateur aggrandissait la fenêtre.

Cette idée a été mise de côté car les tests n'ont pas été concluant et que le rendu final n'était pas propre. Nous avons donc eu une autre idée qui était d'utiliser les propriétés CSS du FXML. En effet, il suffisait d'aller dans les propriétés CSS du conteneur, de mettre la CSS correspondante en indiquant le lien du plan ainsi d'autres propriétés CSS et le plan affiché devenait redimentionnable. Malheureusement nous avons du laissé tomber cette idée car elle nous limitait à simplement afficher un plan alors que nous étions partis du principe d'afficher un plan avec des salles colorés en vert pour indiquer le niveau de disponibilité de la salle. Chose qui était impossible avec le CSS car nous ne pouvions pas indiquer quel salle colorier avec quel teint de vert.

Après multiples recherches, nous avons eu l'idée de contourner le problème. En effet, au lieu de rajouter des éléments sur le plan puis de les modifier au fur et à mesure selon les disponibilités, nous allons modifier directement le plan lui-même, colorier chaque salle et afficher le résultat. La différence étant que nous ne travaillons plus avec des formats d'images ordinaire mais bel et bien avec le format SVG.

Le format SVG nous permet de définir des zones sur un plan. Chaque zone définissant un étage contiendra un ID permettant de l'identifier dans le programme. Les plans nous ont été fournis en PDF, mais grâce à l'outil Inkscape (éditeur d'images opensource), il est possible de créer des fichiers SVG à partir de ces PDFs. Il faudra donc, pour chaque étage de chaque établissement, définir les zones de chaque salle... Énorme tâche mais pour un rendu final convainquant !

### Transformation ICS

## Difficultées rencontrées
### Interface graphique
Il y a de fortes chances que suite aux informations données dans les précédents points du rapport, les points critiques ont déjà été remarqués.  

Pour l'interface graphique, la plus grosse difficulté a été la mise en place de l'affichage des plans des étages en gérant le redimentionnement et le fait que les salles doivent être coloré pour indiquer leur niveau de disponibilité. Cela a été vraiment le plus gros soucis dans cette partie. Nous voulions offrir à l'utilisateur cette fonctionnalité afin que l'utilisation de DARYLL soit des plus agréables et c'est chose faîte.

Bien sûr, nous sommes passés par plusieurs chemins pour avoir ce rendu. Nous nous sommes tout de même dit que, dans les pires des cas, nous afficherons un simple tableau avec les salles et les informations nécessaires si l'affichage des plans étaient trop compliqués. Cependant, il faut avouer que cela aurait été moins remarquable.

Nous avons eu d'autres difficultés comme le fait de gérer les interactions entre vues et contrôleurs, certains composants graphiques qui ne répondaient pas à nos demandes mais au final ce n'est rien de très grave.

### Communication Java - MySQL

### Génération du fichier imprimable




# Conclusion

## Niveau projet à proprement parler

### Problèmes connus dans le programme final


## Niveau fonctionnement du groupe


## Avis personnel de chacun des membres du groupe



## Améliorations possibles





# Bibliographie / webographie

## Java
Documentation officielle,
https://docs.oracle.com/javase/8/docs/


## Base de donnée
Documentation officielle,
https://dev.mysql.com/doc/refman/5.7/en/



## Interface graphique
Oracle, 2013/08, "JavaFX Scene Builder User Guide", https://docs.oracle.com/javafx/scenebuilder/1/user_guide/library-panel.htm
Stackoverflow, 2013/08, "How to find an element with an ID in JavaFX?",  https://stackoverflow.com/questions/12201712/how-to-find-an-element-with-an-id-in-javafx
Oracle, 2013/09, "Getting Started with JavaFX", https://docs.oracle.com/javafx/2/get_started/form.htm
Stackoverflow, 2014/09, "JavaFX getting scene from a controller", https://stackoverflow.com/questions/26060859/javafx-getting-scene-from-a-controller
Oracle, 2014/01, "Designing GUI with the librarie panel (release 2)", https://docs.oracle.com/javase/8/scene-builder-2/user-guide/library-panel.htm
Labri.fr, 2014/08, "Introduction à JavaFX", http://www.labri.fr/perso/johnen/pdf/IUT-Bordeaux/UMLCours/IntroductionJavaFX-V1.pdf



## Communication client serveur
Cours de RES de l'HEIG-VD par Olivier Liechti,
https://github.com/wasadigi/Teaching-HEIGVD-RES


# Table des figures





# Glossaire





# Annexes

## Cahier des charges initial du projet



## Planification du projet

### Initiale

### Finale

### Discussion planification initiale vs finale



## Journal de travail

### Dejvid Muaremi

### Aurélien Siu

### Romain Gallay

### Yohann Meyer

### Labinot Rashiti

### Loïc Frueh
