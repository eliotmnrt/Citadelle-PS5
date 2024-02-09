# Rapport projet citadelle - Groupe U
## Point d’avancement
### 1. Fonctionnalités réalisées

### 2. Affichage 

Nous avons 2 classes d’affichage. 
Une classe liée à un bot pour qu’il affiche ses propres actions et l’autre liée à Tour pour afficher des informations plus générales du jeu. 
Tous les logs sont faits dans ces classes. 
Pour les logs, ils sont activés par défaut. 
Quand on exécute *mvn exec:java -Dexec.args='--2thousands'* ou *mvn exec:java -Dexec.args='csv'* ils sont désactivés. 
Nous avons modifié l’affichage qui était par défaut en rouge grâce à un Formatter.

Nous affichons pour chaque tour de chaque Bot :
* le numéro du tour actuel
* le nom de chaque bot avec son rôle, le nombre de pièces d’ors qu’il possède et son score
* les actions que chaque bot réalise
* les quartiers dans la main de chaque bot et leurs quartiers construits avec leur coût
* les bonus à la fin de la partie
* Les quartiers sont affichés avec leurs couleurs correspondantes.

### 3. CSV

L'exécution de *mvn exec:java -Dexec.args='--csv'* permet de mettre des statistiques sur 4 bots dans un fichier csv. 

Il est facilement possible de changer les bots qui s'affrontent, deux bots identiques peuvent s'affronter. 

Si le fichier n’est pas déjà créé dans le dossier, il le crée. 

Quand le fichier est déjà créé et qu’on relance la commande (et les bots sont les mêmes) les valeurs sont mises à jour dans le fichier. 
Le total des parties lancées augmente et les statistiques sont réévaluées en fonction du nombre de simulations qu’on avait et du nombre de nouvelles simulations. 

Si on change les bots qui sont testés, le fichier est remis à 0 (seulement les nouvelles simulations sont prises en compte).

Il est également possible d'exécuter *mvn exec:java -Dexec.args='-csv 50'*, cela permet d’améliorer le fichier csv avec un nombre de nouvelles simulations données (ici 50), si le nombre n’est pas valide (négatif) l’option n’est pas prise en compte, une partie normale est lancée.

### 4. Bot Richard et meilleure Bot

Nous avons implémenté quasiment toutes les fonctionnalités demandées pour le BotRichard:

* concernant le choix du rôle en fonction de l’état de la partie, selon si des joueurs menacent de finir en fonction de leur rôle potentiel, de leur nombre de quartiers construits et de leur ordre dans le choix des rôles
  * prendre l’assassin pour contrer un joueur qui menace de finir avec l’architecte en tuant l’Architecte
  * prendre (dans l’ordre) le Roi, l’Assassin, le Condottiere et l'Évêque si un adversaire a 6 quartiers construits
* ciblage des Bots les plus avancés selon l’état de la partie
  * pour reprendre l’exemple juste au dessus, si un joueur a 6 quartiers construits et qu’on récupère le rôle d’assassin, on va tuer le rôle le plus probable que l’adversaire ai choisi
  * empêcher un joueur qui a 7 quartiers construits de finir la partie:
    * Si le joueur menaçant choisit son rôle en deuxième, le BotRichard choisit l’assassin et tue le prêtre ou le condottiere si il peut.
    * Si le joueur menaçant choisit son rôle en 3ème ou 4ème, les premiers joueurs (1er et 2ème et pas 3ème)  BotRichard vont le cibler. Par exemple, si le joueur en passe de gagner est 3ème, le premier va choisir l’assassin et tuer le condottiere et le deuxième va choisir le prêtre.

Pour estimer cela nous avons notamment fait une fonction qui prend en paramètre un Bot donné  et cette fonction détermine le rôle le plus probable que ce bot a pu prendre, en fonction de l’ordre dans lequel il a choisi son rôle (avant ou après nous), les cartes que notre Bot a vu en choisissant son rôle, les rôles visible au début du tour, son nombre de carte dans la main, son nombre de quartier construit, le nombre de quartier par couleur qu’il possède et le nombre d’or qu’il a.

Lorsqu’on lance la simulation avec BotConstruitChere (6%), BotFocusRoi (58%), BotFocusMarchand (28%) et BotRichard (4%), ce dernier n’obtient que 4% de parties gagnées et a le plus bas score moyen des 4 Bots. En effet il se base sur le comportement de BotConstruitChere pour ce qui concerne les actions de bases ainsi que la manière de construire ses quartiers, cependant puisqu’il “perd” du temps et de l’argent à contrer les bots qui ont pris de l’avance, le BotRichard devient un tout petit moins efficace que le BotConstruitChere qui lui a 6% de winrate.

Concernant notre meilleur Bot: en comparant des parties avec et sans le BotRichard, on peut s’apercevoir que le BotFocusMarchand perd beaucoup de parties à cause du BotRichard. On suppose que le BotFocusMarchand se fait viser et ralentir par le BotRichard, et cela permet au BotFocusRoi, qui est juste derrière BotFocusMarchand, de s’imposer plus souvent.


### 5. Fonctionnalités non réalisées
Nous n’avons pas mis en place:
* Un maximum d’or disponible durant une partie.
* Dans les fonctionnalités du BotRichard, nous n’avons pas implémenté dans les spécifications du dernier tour (celui où un bot menace de finir avec 7 quartiers construits) le cas où le joueur en passe de gagner choisit son rôle en 4ème et qu’il y a un bot richard qui choisit son rôle en troisième.
* Dans Bot Richard, le cas spécifique où le joueur menaçant de gagner choisit son rôle en 4ᵉ, si le Bot Richard est en 3ᵉ il ne va pas cibler le joueur en passe de gagner.

## Architecture et qualité

### 1. Architecture du projet
Pour l’organisation de ce projet nous avons décidé assez rapidement de faire une classe pour chaque rôle ainsi qu’une classe pour chaque Bot. Il y a également une interface qui est utilisée pour l’implémentation de chaque rôle. Elle permet de créer un modèle à suivre pour tous les rôles. A l'intérieur de chaque classe Rôle il y a des fonctions qui permettent de faire la ou les actions spéciales du Rôle.

Cependant, ce n’est pas le Rôle qui fait le choix de faire des actions ou non. En effet, la décision est prise par les Bots. Les Bots sont organisés en 2 parties. Tout d’abord il y a une classe abstraite Bot qui donne l’obligation à tous les Bot d’avoir certaines fonctions essentielles (méthodes abstract). Les méthodes de base, communes à tous les Bots, sont également écrites dans cette classe pour éviter la duplication de code. Ensuite il y a une deuxième classe abstraite qui s’appelle “Bot malin”, cette classe est étendue par tous les Bots sauf l’aléatoire. Cette classe a été faite pour éviter la duplication de code car les Bots intelligents ont certaines mécaniques de jeux en commun. Dans cette classe il y a des méthodes comme choixDeCarte qui choisit une carte à piocher. Cette méthode est override dans Bot qui focus Roi qui il essaye de piocher un quartier jaune en priorité.

En ce qui concerne les quartiers, nous avons fait 2 énumérés, Quartier et TypeQuartier. Quartier contient tous les quartiers existant, leur prix, leur couleur (un TypeQuartier) et le nombre de fois où cette carte est dans la pioche. L’énuméré TypeQuartier contient les 5 couleurs ainsi que leur nom par exemple Jaune et “noble”.

Concernant le fonctionnement d’une partie se passe de la manière suivante : une pioche et 4 Bots sont instanciés. Une instance de la classe Tour est faite, tant qu’aucun Bot n’a pas 8 quartiers ou plus les Bots choisissent leurs rôles à la suite (selon leurs ordre). Puis chacun joue son tour dans l’ordre.

### 2. Documentation

Les informations essentielles à notre projet se trouvent dans le [README.md](../README.md). On y décrit les commandes à exécuter pour lancer le projet en fonction du mode que l’on souhaite. On y retrouve également une courte description du projet.

Toutes les informations nécessaires pour contribuer à notre projet se situent dans [CONTRIBUTING.md](CONTRIBUTING.md). Notre stratégie de branchement y est expliquée, ainsi que les étapes à suivre pour faire une nouvelle issue et une nouvelle pull request.

En plus de cela, nous avons réalisé une [javaDoc](javaDoc) qui restitue toute l’architecture de notre projet ainsi que le détail de chaque classe et chaque méthode.

### 3. Points positifs
### 4. Améliorations effectuées grâce à Sonarlint/Sonarqube
Ces 2 outils nous ont permis de cibler certaines parties du code à améliorer. Par exemple l’ouverture du .csv était relevé en bug dans Sonarqube, mais pas repéré par Sonarlint. Nous avons donc modifié l’ouverture du fichier en try-with-resources.

Nous avons pu cibler certaines parties du code qui était dupliquées dans plusieurs endroits du code. De même pour le refactoring de certains éléments, ce qui nous a permis de rendre le code plus clair, plus lisible.

### 5. Améliorations possibles
[Bot Richard](../src/main/java/Citadelle/teamU/moteurJeu/bots/malin/BotRichard.java)

## Processus 
### 1. Répartition du travail
Pour la réparation du travail nous avons décidé de nous attribuer des tâches différentes chaque semaine. Nous faisions 1 milestone par semaine (hors sprint final où nous faisions 1 milestone par jour) avec des issues (liée à cette mistone) que nous répartissions.

Nos milestones sont verticales, quand nous ajoutons un nouveau rôle nous adaptons aussi toutes les stratégies des Bots pour ce rôle dans la même milestone. Le but est d’avancer sur notre ou nos issues pour la semaine prochaine.

Chacun a fait au moins un rôle et un bot (une stratégie spécifique) durant le projet pour permettre une compréhension globale du projet par tout le monde. La personne qui produit un code fait les tests pour ce qu’il a fait.

### 2. Utilisation de git
Nous suivons la stratégie github flow qui correspondait le plus à notre mode de fonctionnement. 
Voir plus de détails [ici](CONTRIBUTING.md)