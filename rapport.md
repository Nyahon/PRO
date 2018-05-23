# Page de titre
- avec logo HES-SO et HEIG-VD
- titre du projet
- indication nature du rapport (Cahier des charges, Manuel utilisateur, ...)
- nom des auteurs
- nom du destinataire du rapport
- date (par ex "mars 2018")





# Table des matières





# Introduction
Durant le quatrième semestre de la section TIC, nous devons effectuer un projet en groupes de cinq ou six personnes. Le but est de rassembler les connaissances que les étudiants ont acquises jusqu'à maintenant à travers un projet conséquent. Nous devrons prendre conscience des difficultés liées au travail de groupe, apprendre à planifier un projet sur plusieurs mois, gérer les conflits internes et apprendre à avoir une vision globale de l'architecture du projet.
Au terme du semestre, nous devons rendre un programme complet et fonctionnel, avec une documentation adéquate et être capables de le présenter et le défendre.
Le projet dure seize semaines et vaut trois crédits. Le temps de travail est de 540 heures pour toute l’équipe, soit cinq heures par membres du projet, par semaine.
Dans le cadre du projet, l'équipe de développement est composée du chef de projet Yohann Meyer, de son suppléent Loïc Frueh et des membres Aurélien Siu, Dejvid Muaremi, Labinot Rashiti, et Romain Gallay.

Dans ce rapport, nous allons expliquer notre démarche de travail, les principaux choix de conception et d'implémentation. Il sera structuré selon le modèle fourni.

Ce rapport, étant à rendre lors de la semaine treize, il ne contiendra pas les retours de la défense orale.


# Objectifs du projet
L'objectif de notre projet et de proposer un programme, DARYLL, un utilitaire permettant l'obtention des disponibilités des salles de cours de la HEIG. À l'heure actuelle, aucun outil interne de l'HEIG-VD ne permet d'obtenir cette information de manière viable.
Se basant principalement sur les données GAPS, DARYLL proposera une interface claire et rapide aux utilisateurs à la recherche d'une salle libre pour un ou plusieurs horaire(s) donné(s) et l'inverse.
Il permettra également d'imprimer un horaire selon les spécifications de l'utilisateur.


# Conception et architecture du projet

## Technologies et outils utilisés

### Java
Parmi les deux langages de haut niveau proposés pour le projet (Java ou C++), nous avons choisi d'utiliser Java pour sa portabilité, grâce à la Java Virtual Machine, sa sécurité et ses performances, dont le garbage collector est particulièrement intéressant par le fait qu'il libère la mémoire automatiquement. En effet, cette fonctionnalité nous semble plus que nécessaire quant tenue du fait que nous allons utiliser un transcodeur pour passer d'un plan SVG à une image Java.
De plus notre équipe est plus habile à programmer à l'aide du langage Java, et l'API JavaFX.   

#### JavaFX
Dans le cadre du développement de DARYLL, il nous a été imposé de choisir entre trois API pour l'interface graphique. Il fallait donc choisir entre Swing, JavaFX et Qt.

Notre choix s'est porté pour JavaFX, successeur de Swing en tant que librairie de création d'interfaces graphiques officielles du langage. Le SDK de JavaFX est intégré au JDK standard Java SE et dispose de plusieurs fonctionnalités dont les plus importantes sont les suivantes :

##### Scène FXML
Le FXML est un langage inspiré du HTML ou XML et indique la définition de l'interface graphique utilisateur. Dans notre cas, nous avons plusieurs fichiers FXML qui contiendront toutes les balises nécessaires à la représentation d'une "view". Chaque balise représentera donc un composant de cette vue. Grâce au langage FXML, il sera possible de personnaliser chacune des balises avec des attributs et des valeurs. Cette personnalisation des balises peut être comparée au CSS pour les fichiers HTML.

##### Scène Builder
Scène Builder de Gluon permet de manipuler des objets JavaFX graphiquement et d'exporter ceux-ci dans un fichier .fxml interprétable et modifiable par la librairie graphique. Il nous permet également de voir l'interface comme si le programme était en cours d'exécution et de pouvoir y personnaliser directement les composants graphiques à l'aide de la souris.
Cette application nous simplifie donc la personnalisation, car il n'est pas nécessaire de réécrire tout le FXML. Il est à noter que le fichier FXML et Scene Builder sont liés. La modification de l'un met à jour directement la vue de l'autre !

### Maven
Comme on utilise plusieurs librairies externes, il devenait rapidement compliqué de gérer toutes les dépendances de notre programme. C'est pour cette raison que nous avons choisi d'utiliser l'outil Maven d’Apache, celui-ci permettra de compiler et importer notre projet sur nos nombreux environnements de travail. L'autre avantage que nous offre Maven et qu'il permet de produire un fichier exécutable grâce à des instructions se trouvant dans le fichier pom.xml.

### Git
Git, le gestionnaire de version décentralisé libre, utilisé afin de gérer la totalité du projet ainsi que toutes ses modifications.
Nous avons créé une nouvelle branche par thème, c'est à dire, une branche pour le client, une branche pour le serveur, une pour l'administration et une pour les rendus.
Cet outil nous a été particulièrement utile pour pouvoir travailler en équipe et en même temps tout en nous assurant que nous avions bien tous la même version de DARYLL.

### GitHub
GitHub, le service web permettant de parcourir visuellement l'historique Git de DARYLL et qui va également le stocker.
GitHub offre également de nombreuses fonctionnalités ainsi que des outils de gestion pour le projet.

### MySQL
MySQL, le système de gestion de bases de données relationnelles (SGDBR), faisant partie des logiciels de gestion des bases de données les plus utilisés au monde.
Nous avons choisi de l'utiliser, car c'est le modèle que nous maitrisions le mieux et que Java possède de nombreuses méthodes afin de nous permettre d'interagir avec nos données.

### Inkscape
Inkscape, le logiciel libre de dessin vectoriel sous licence GNU GPL. Il nous a permis d'ouvrir les PDFs des plans du bâtiment afin de les modifier comme s'il s'agissait d'un SVG puis nous pouvons les exporter au format qui nous arrange le plus (SVG simple).

### Docker
Docker, le logiciel libre qui automatise le déploiement d'application dans des conteneurs logiciels. Ces conteneurs sont isolés et peuvent être exécutés sur n'importe quel système qui prend en charges Docker.
Ceci nous permet d'étendre la flexibilité et la portabilité de notre serveur.

## Architecture de la solution

### Définition de la base de données
Period :
Cette table contient une liste de 15 périodes qui représente les différentes périodes possibles dans l'horaire GAPS.
Une période est identifiée par un numéro unique allant de 0 à 15 et représentant le numéro de la période sur l'horaire journalier de GAPS, se caractérise par une heure de début et une heure de fin.
Chaque période est reliée à une ou plusieurs salles de classe.

Classroom :
Cette table contient la liste des salles des campus de Cheseaux et Saint-Roch de l'HEIG-VD.
Une salle est identifiée par son nom (A01, A02, …), et se caractérise par un booléen qui indique si elle est verrouillée ou non.
La salle est reliée à une ou plusieurs périodes, possède un équipement qui lui est propre, et se trouve dans un étage du bâtiment.

TakePlace :
Le numéro d'une période P est relié à une salle de classe S par un `TakePlace`, cette relation se caractérise par une date.

Floor :
Cette table contient la liste des étages des campus de Cheseaux et Saint-Roch de l'HEIG-VD.
Un étage est identifié par son nom (A, B, ...), et se caractérise par le campus auquel il appartient.
Un étage est relié à plusieurs salles et possède un équipement qui lui est propre.

ClassroomEquipments :
Cette table permet de spécifier en détail l'équipement présent dans une salle.
L'équipement de la salle est identifié par un numéro unique, et se caractérise par des booléens qui indique la présence d'un beamer, de prises électriques, d'ordinateurs et d'un tableau blanc ou noir.
Un équipement est relié à une et une seule salle de classe.

FloorEquipments :
Cette table permet de spécifier en détail l'équipement présent dans un étage.
L'équipement d'un étage est identifié par un numéro unique, et se caractérise par des booléens qui indique la présence de toilette pour hommes, de toilette pour femmes, d'une machine à café, d'un distributeur Selecta ou équivalent et d'un accès à une sortie du bâtiment.
Un équipement est relié à un et un seul étage.


Schémas et commentaires
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Description technique de l'implémentation

## Structure du programme
### l’interface graphique
#### Fonctionnement de JavaFX
Afin de comprendre comment marche JavaFX, il faut imaginer notre programme comme étant une pièce de théâtre. JavaFX utilise cette image afin de structurer le programme ainsi que son interface graphique.

Nous nous retrouvons donc avec des termes comme "Stage", "Scene" et différents composants animant cette scène. Nous vous expliquerons nos différents composants et scènes plus tard dans la documentation.

JavaFX intègre également la notion de MVC (Modèle-vue-contrôleur). Ils sont créés par défaut lors du commencement d'un projet. La classe principale étant le modèle, la vue étant le fichier FXML et le contrôleur étant le fichier Java gérant les interactions avec le fichier FXML.

#### Scène principale
Lors du début du projet, le programme était une simple application JavaFX. Le projet DARYLL possédait donc uniquement un fichier contenant sa classe, une vue vide (fenêtre principal ou scène principale) et le contrôleur de cette vue (donc selon le modèle MVC vu précédemment).

La première tâche a été de choisir le conteneur principal de DARYLL et pour cela, il y a plusieurs choix. Certains ont des avantages que d'autres n'ont pas. En réalité, le choix dépend de l'utilisation de l'application et du rendu final.

Nous avons essayé plusieurs conteneurs tel que le Anchor Pane, le Grid Pane ou le simple Pane, mais notre choix a penché pour le Border Pane. Il aurait été possible de réaliser notre programme avec les autres conteneurs mentionnés, mais étant donné que nous avons décidé d'intégrer le redimensionnement de la fenêtre pour les plans des étages, l'implémentation aurait été plus compliquée et le Border Pane nous facilitait beaucoup plus la tâche à ce niveau-là.

Le Border Pane est donc plus intéressant, car il est déjà séparé en différentes zones (top, bottom, center, left, right), et grâce à ces zones, les composants prennent automatiquement la bonne taille avec le redimensionnement. L'autre aspect également important pour le Border Pane est le fait qu'il respecte le plus possible au mockup défini au début du projet.

#### Scènes secondaires
Par scènes secondaires, nous entendons les autres fenêtres ou pop-up qui s'ajoutent à la scène principale (fenêtre principale) de DARYLL. À la base, il n'y avait qu'une seule scène et un seul contrôleur qui gérait ladite scène.  Les autres scènes étaient créées directement dans le contrôleur principal via du code.

Cette solution fonctionnait bien, mais ne donnait pas un rendu comme nous l'avions désiré. De plus, le contrôleur de la fenêtre principale devenait facilement énorme vu les nombreux pop-up que nous avons rajoutés pour les options du programme. Donc au final, cette solution ne donnait pas un bon rendu et n'était pas très évolutive, car il est difficile de retrouver la bonne vue dans un seul fichier.

Après des recherches et réflexions, nous avons décidé de dispatcher le FXML en plusieurs parties. Dorénavant, chaque fenêtre graphique aura son propre fichier FXML et son propre contrôleur gérant les interactions avec l'utilisateur de ladite fenêtre.

Cette nouvelle solution permet d'avoir un rendu nettement meilleur à l'affichage et rend le code beaucoup plus propre. En effet, lorsqu'il faudra mettre à jour ou dépanner une fenêtre du programme, il suffira d'aller dans son fichier FXML et contrôler pour effectuer les changements !

#### Gestion des évènements selon le composant ciblé
Cet aspect du projet mérite son propre titre de par la démarche réalisée afin d'optimiser le code.

Si nous regardons le mockup de base, nous apercevons que la zone de gauche contient tous les boutons des étages. La démarche basique aurait été de créer une fonction pour chaque bouton d'étage dans le contrôleur de la vue et ensuite de les assigner une à une via Scene Builder. Cela veut dire que pour x boutons nous aurons x fonctions.

Alors évidemment, cela fonctionnera, mais n'oublions pas que le contrôleur est déjà très conséquent en termes de code. S’il faut encore rajouter une fonction par étage en sachant que Cheseaux en a environ dix, le code deviendrait illisible et donc plus difficile à maintenir.

La nouvelle solution adoptée a été de factoriser ce code. En effet, les boutons d'étages ont le même but : afficher le plan de l'étage avec ses salles disponibles au moment X.

L'idée est donc de faire une fonction permettant de faire ce changement pour chaque bouton d'étage. Idéalement la fonction devra prendre le bouton ciblé en paramètre, mais malheureusement Scene Builder ne le permet pas...

Après plusieurs recherches, nous avons trouvé une alternative que nous avons utilisée tout au long du projet pour l'interface graphique. En fait, cette alternative permet de retrouver le composant ciblé via un objet "Event". Cet objet "Event" nous permet de retrouver la scène où se trouve le composant ciblé puis de récupérer le composant via son ID.

Cela veut dire que la fonction gérant le changement de plan (showFloor) ne va pas prendre un bouton en paramètre, mais un objet "Event" qui comprend le clic de la souris. C'est grâce à cette méthode que nous avons pu factoriser le code et le rendre plus propre.

#### Redimensionnement
L'interface graphique de DARYLL affiche les plans des étages de Cheseaux ou de Saint-Roch. Ces plans ont une certaine taille et cela nous amène à une question problématique sur l'affichage des plans sous différentes résolutions. Si nous imaginons notre application dans un cas concret d'utilisation, alors il se pourrait que l'utilisateur ait une mauvaise qualité d'image à cause de sa résolution d'écran.

C'est pour cela que DARYLL offre la possibilité d'agrandir ou de rapetisser la fenêtre afin que l'affichage du plan soit en adéquation avec la résolution de l'utilisateur. Alors bien sûr, cette fonctionnalité paraît toute simple, mais en réalité la mise en place est très difficile. Nous allons vous expliquer la démarche que nous avons eue tout au long du projet dans les paragraphes qui suivent.

Suite à des réunions concernant cette question, le groupe s'est mis d'accord sur le fait qu'il était préférable d'intégrer le redimensionnement. L'idée initiale était d'afficher le plan sur un conteneur puis d'incruster des composants au-dessus de chaque salle de l'étage afin d'indiquer le niveau de disponibilité de la salle avec un teint de vert. L'implémentation fut difficile, car il fallait faire attention à ce que les composants restent correctement placés sur les étages lorsque l'utilisateur agrandissait la fenêtre.

Cette idée a été mise de côté, car les tests n'ont pas été concluants et que le rendu final n'était pas propre. Nous avons donc eu une autre idée qui était d'utiliser les propriétés CSS du FXML. En effet, il suffisait d'aller dans les propriétés CSS du conteneur, de mettre la CSS correspondante en indiquant le lien du plan ainsi d'autres propriétés CSS et le plan affiché devenait redimensionnable. Malheureusement nous avons dû laisser tomber cette idée, car elle nous limitait à simplement afficher un plan alors que nous étions partis du principe d'afficher un plan avec des salles colorées en vert pour indiquer le niveau de disponibilité de la salle. Chose qui était impossible avec le CSS, car nous ne pouvions pas indiquer quelle salle colorier avec quel teint de vert.

Après multiples recherches, nous avons eu l'idée de contourner le problème. En effet, au lieu de rajouter des éléments sur le plan puis de les modifier au fur et à mesure selon les disponibilités, nous allons modifier directement le plan lui-même, colorier chaque salle et afficher le résultat. La différence étant que nous ne travaillons plus avec des formats d'images ordinaires, mais bel et bien avec le format SVG.

Le format SVG nous permet de définir des zones sur un plan. Chaque zone définissant un étage contiendra un ID permettant de l'identifier dans le programme. Les plans nous ont été fournis en PDF, mais grâce à l'outil Inkscape (éditeur d'images open source), il est possible de créer des fichiers SVG à partir de ces PDF. Il faudra donc, pour chaque étage de chaque établissement, définir les zones de chaque salle... Énorme tâche, mais pour un rendu final convainquant !

### Transformation ICS
À partir du calendrier ICS de GAPS, nous avons dû implémenter un parseur afin de pouvoir parcourir ce fichier et récupérer la liste des salles occupées. Cette liste a été ensuite ajoutée à la base de données afin que l'on puisse y effectuer des requêtes dessus.

### Transformation des plans des bâtiments
Les plans fournis n'étaient pas parfaitement adaptés à notre application, ils étaient au format PDF et il y avait beaucoup d'informations superflues qui rendaient leur lecture très difficile. Par conséquent, il a fallu les modifier à la main un par un afin d'obtenir des fichiers SVG que l'on utilisera. Les modifications ont dû être faites sur chaque bâtiment, chaque étage, chaque salle et surtout sur les fragments de salles qui ont rendu la manipulation particulièrement difficile.

### Communication client - serveur
Nous avons appliqué ce que nous avons appris lors du cours de RES. Nous avons donc défini un paquet "network" qui va effectuer la sérialisation et l'envoie des données par le réseau en utilisant des sockets TCP.
Nous avons mis en place un protocole de communication entre le client et le serveur pour l'échange d'informations, à savoir le type, le contenu, et la fin des requêtes.

### Génération du fichier imprimable
L'implémentation d'une commande "imprimer" est extrêmement compliquée par conséquent nous avons choisi de créer un fichier texte temporaire et imprimable, qui sera ouvert automatiquement par l'éditeur de texte par défaut de l'utilisateur. Celui-ci devra ensuite décider s’il veut sauver ou imprimer ledit fichier. Si l'utilisateur ne sauvegarde pas le fichier, celui-ci sera écrasé lors de la prochaine requête.
Cette méthode nous permet de répondre à deux demandes à la fois, la possibilité de sauver un fichier contenant les disponibilités ainsi que la possibilité d'imprimer un fichier.

## Difficultés rencontrées
### Interface graphiques
Il y a de fortes chances que suite aux informations données dans les précédents points du rapport, les points critiques ont déjà été remarqués.
Pour l'interface graphique, la plus grosse difficulté a été la mise en place de l'affichage des plans des étages en gérant le redimensionnement et le fait que les salles doivent être colorées pour indiquer leur niveau de disponibilité.
Cela a été vraiment le plus gros souci dans cette partie. Nous voulions offrir à l'utilisateur cette fonctionnalité afin que l'utilisation de DARYLL soit des plus agréables et c'est chose faîte.
Bien sûr, nous sommes passés par plusieurs chemins pour avoir ce rendu.
Nous nous sommes tout de même dit que, dans les pires des cas, nous afficherons un simple tableau avec les salles et les informations nécessaires si l'affichage des plans était trop compliqué. Cependant, il faut avouer que cela aurait été moins remarquable.
Nous avons eu d'autres difficultés comme le fait de gérer les interactions entre vues et contrôleurs, certains composants graphiques ne répondant pas à nos demandes, mais au final ce n'est rien de très grave.
### Restructurer les plans
Les salles étant représentées par des triangles sur le plan, il a fallu les regrouper afin d'obtenir une salle dont on pourra modifier sa couleur en temps réel.
La modification n'est pas particulièrement difficile, mais elle prend énormément de temps, mais le résultat final est très beau.
### Gestion des dates
Malheureusement, les dates SQL ne sont pas parfaitement supportées par Java, ceci nous a donné beaucoup de fil à retordre afin de générer une requête SQL, qui sera envoyée à la base de données.


# Test réalisé
## Problèmes connu dans le programme final

# Conclusion
## Niveau projet à proprement parler
## Niveau fonctionnement du groupe
## Avis personnel de chacun des membres du groupe
Dejvid Muaremi :
Ce projet fut très intéressant à faire, j'ai pu découvrir de nouvelles technologies et mettre en pratique tout ce que j'ai appris jusqu'à maintenant. Parfois, j'ai pris du retard sur mes tâches, mais j'ai pu le rattraper au final.
Aurélien Siu :
Romain Gallay :
Yohann Meyer :
Labinot Rashiti :

Je pense qu'on peut retenir une bonne expérience de ce projet. Le groupe s'entendait bien et les différentes réunions de groupe chaque semaine nous ont permis de diriger un cap, d'attribuer des tâches pour chacun et enfin avancer dans le projet.

Il est vrai que le début était un peu poussif, car nous n'avons pas eu les informations nécessaires concernant les plans des bâtiments ou les horaires GAPS. Heureusement, nous avons été intelligents dans la manière de gérer ces retards et nous nous retrouvons donc avec un projet terminé. 

Pour ma part, j'ai pu travailler dès le début sur l'interface graphique vu que je n'étais pas atteint par ces retards et j'ai continué sur ce thème jusqu'à la fin du projet. Par la suite, Aurélien m'a rejoint pour l'interface graphique et notamment pour le redimensionnement des fenêtres. Je tiens à le féliciter pour son idée sur l'utilisation des SVGs pour les plans qui était une très bonne idée ainsi que son implication durant tout le projet.

Loïc Frueh :

## Améliorations possibles





# Bibliographie / Webographie
## Java
Documentation officielle, https://docs.oracle.com/javase/8/docs/
## Maven
Site officiel, https://maven.apache.org/
## Git
Site officiel, https://git-scm.com/
## GitHub
Site officiel, https://github.com/
## MySQL
Documentation officielle, https://dev.mysql.com/doc/refman/5.7/en/
## Inkscape
Site officiel, https://inkscape.org/fr/
## Docker
Site officiel, https://www.docker.com/
## Interface graphique
Oracle, 2013/08, "JavaFX Scene Builder User Guide", https://docs.oracle.com/javafx/scenebuilder/1/user_guide/library-panel.htm
Stackoverflow, 2013/08, "How to find an element with an ID in JavaFX?",  https://stackoverflow.com/questions/12201712/how-to-find-an-element-with-an-id-in-javafx
Oracle, 2013/09, "Getting Started with JavaFX", https://docs.oracle.com/javafx/2/get_started/form.htm
Stackoverflow, 2014/09, "JavaFX getting scene from a controller", https://stackoverflow.com/questions/26060859/javafx-getting-scene-from-a-controller
Oracle, 2014/01, "Designing GUI with the librarie panel (release 2)", https://docs.oracle.com/javase/8/scene-builder-2/user-guide/library-panel.htm
Labri.fr, 2014/08, "Introduction à JavaFX", http://www.labri.fr/perso/johnen/pdf/IUT-Bordeaux/UMLCours/IntroductionJavaFX-V1.pdf
## Communication client-serveur
Cours de RES de l'HEIG-VD par Olivier Liechti, https://github.com/wasadigi/Teaching-HEIGVD-RES
# Sources externes
Calendrier ICS, GAPS
Plan des bâtiments, Philippe Moser

# Table des figures

# Glossaire

# Annexes
## Cahier des charges initial du projet
## Planification du projet
### Initial
### Final
### Discussion planification initiale vs finale
Notre planification initiale était très mauvaise, il a fallu que l'on prenne une semaine de plus au début du projet pour la revoir entièrement. Au final, le temps perdu en replanification a été gagné en clarté et le déroulement de nos tâches a pu être fait correctement.
## Journal de travail
### Dejvid Muaremi
### Aurélien Siu
### Romain Gallay
### Yohann Meyer
### Labinot Rashiti
### Loïc Frueh
