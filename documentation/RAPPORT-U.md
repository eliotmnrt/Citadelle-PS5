# Rapport projet citadelle - Groupe U
## Point d’avancement
### 1. Fonctionnalités réalisées
Nous avons réalisé la majorité du jeu Citadelle. Dans notre projet nous avons 4 Bots (des joueurs) qui s’affrontent. 
* Nous avons implémenté la totalité des rôles du jeu sans extension. Soit : assassin, voleur, magicien, roi, prêtre, marchand, architecte, condottiere. Ainsi que leurs caractéristiques spécifiques.
Chaque Bot a une stratégie parmi les 6 stratégies que nous avons codées. Les stratégies sont les suivantes :
* Bot construit chère : Son but est d’avoir un score élevé.
  * Il cherche à piocher des quartiers qui coûtent chers (qui coûtent 4 pièces ou plus), et il construit uniquement les quartiers chers.
  * Il choisit le rôle du Marchand quand il peut, afin de pouvoir collecter plus de pièces.
  * Quand il a le rôle du Condottière, il détruit uniquement les quartiers qui valent 1, parce que ça ne lui coûte rien → il économise de l’argent.
  * Quand il a le rôle de Magicien, il échange ses cartes avec le joueur qui a le plus de cartes, si c’est lui le joueur avec le plus de cartes il échange avec la pioche.
* Bot construit vite : Son but est de finir en premier en construisant 8 quartiers.
  * Il cherche à piocher des quartiers qui coûtent peu chers ( qui coûtent moins de 4 pièces), et il construit uniquement les quartiers peu chers.
  * Il choisit le rôle de l'Architecte quand il peut, afin de pouvoir construire jusqu’à 3 fois s’il peut.
  * Quand il a le rôle du Condottière, il détruit le quartier le moins cher du bot qui a le plus de quartiers construits.
  * Quand il a le rôle de Magicien, il fait la même chose que Bot construit chère.
* Bot focus Marchand : Son but est de choisir le rôle du Marchand et construire des quartiers verts pour gagner beaucoup d’argent
* Bot focus Roi : Son but est de monopoliser le rôle du Roi pour avoir la couronne et construire des quartiers jaune pour gagner beaucoup d’argent

* Fonctionnalités communes à Bot Focus Roi et Bot Focus Marchand:
  * Au début de la partie, le bot a comme priorité de construire 2 quartiers de sa couleur. Pour cela, il cherche à choisir les rôles Architecte ou Magicien dans l’ordre, pour avoir plus de choix parmi les quartiers. Sinon il choisit Marchant pour botFocusMarchand ou Roi pour botFocusRoi ensuite Roi pour botFocusMarchand (pour choisir au premier au prochain tour) dans l’ordre, ou enfin aléatoirement parmi les rôles qui restent. 
  * A partir du moment où le bot a construit 2 quartiers de sa couleur, il vise dans l’ordre les rôles Marchant pour BotFocusMarchand, Roi, Architecte, Magicien ou aléatoirement parmi les rôles qui restent.
  * Pour construire : dès que le bot possède un quartier de sa couleur dans sa main il économise des pièces pour le construire. Si il n’a pas de quartier de sa couleur,il construit le quartier le moins cher de sa main et pioche un quartier (de sa couleur si possible).
  * Quand le bot a le rôle du Condottière, il détruit uniquement les quartiers qui valent 1 (qui ne lui coûte rien à détruire).
  * Quand le bot a le rôle de Magicien, il échange ses cartes avec un joueur qui a 3 quartiers de plus que lui, sinon il échange avec la pioche uniquement les quartiers qui ne sont pas de sa couleur, ou ceux de sa couleur qu’il a déjà construit.
* Bot aléatoire : il fait tous ses choix en aléatoire, le choix du rôles, du ou des quartiers à construire, qui tuer, qui voler, etc…
* Bot Richard avec la majorité de ses fonctionnalités (détaillées plus tard dans le rapport).

* Fonctionnalités commmunes pour tous les bots (hors Bot Richard):
  * Pour les rôles Assassin et Voleur,si le bot choisit en dernier son rôle, il tue ou vole aléatoirement parmi tous les rôles. Sinon il effectue son action sur un rôle parmi les rôles restants après avoir choisi le sien.
  * Pour le rôle Architecte le bot construit 3 fois s'il peut.
  * Pour les rôles Roi et Prêtre, le bot ne fait rien de spécial à part jouer son tour.


* Nous avons également décidé que si la pioche était vide, nous en ferions une nouvelle.
* L’ordre de jeux est également pris en compte, l’ordre dans lequel les bots sont donnés au début est gardé et respecté pour toute la partie. Le premier commencera à choisir son rôle puis le deuxième, etc... 
* Une fois la couronne prise par un Bot, il choisira en premier au tour d’après, puis celui après lui (si le bot qui a la couronne est 4ème dans l’ordre d’origine, c’est le 1er qui joue après lui).
* Le dernier tour est le tour où un bot construit 8 quartiers ou plus en premier. Une fois qu’un Bot a construit 8 quartiers il ne peut plus être visé par le Condottière.
* Les scores de chaque Bot sont mis à jour et affichés à chaque tour.
* Nous avons également fait tous les quartiers violets, pour les quartiers université et dracoport la valeur ajouté a leur score est affiché à la fin de la partie.
* Les bonus sont affichés à la fin de la partie, nous avons réalisés les bonus suivants :
  * Le Bot qui a fini en premier gagne 4 points
  * Le Bot qui finit avec 8 quartiers ou plus (hors celui qui a fini en premier) gagne 2 points.
  * Un Bot qui finit avec un quartier de chaque couleur (en prenant en compte la cours des miracles) gagne 3 points.

* Nous avons également implémenté la possibilité de lancer notre jeu sous plusieurs modes en lancant la commande suivante:
mvn exec:java -Dexec.args="--2thousands --demo --csv -csv 50", on peut lancer tous ou un argument à la fois.
  * --2thousands: permet de voir des statistiques sur 1000 parties où 4 bots différents d'affrontent, et 1000 parties où 4 Bot identiques s'affrontent.
  * --demo: permet d’avoir la trace d’une partie entière avec 4 Bots différents.
  * --csv: permet de creer un fichier csv avec les statistiques sur 1000 parties ou 4 bots différents s'affrontent. Si le fichier existe déjà les statisques sont améliorées.
  * -csv [nombre] : permet de faire la même chose que --csv mais avec autant de simulation que le [nombre] indique

* Nous avons également mis en place le github action dans le fichier [CI.yml](../.github/workflows/CI.yml).
  * à chaque fois qu’il y’a un push ou une pull request sur la branche master, un build qui exécute les commandes suivantes est lancé:
  * mvn -B package --file pom.xml : compilation et exécution de tous les tests unitaires du projet
  * mvn exec:java -Dexec.args="--demo" : compilation et exécution d'une démonstration d’une partie de jeu


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

* Il est facilement possible de changer les bots qui s'affrontent, deux bots identiques peuvent s'affronter. 

* Si le fichier n’est pas déjà créé dans le dossier, il le crée. 

* Quand le fichier est déjà créé et qu’on relance la commande (et les bots sont les mêmes) les valeurs sont mises à jour dans le fichier. 
Le total des parties lancées augmente et les statistiques sont réévaluées en fonction du nombre de simulations qu’on avait et du nombre de nouvelles simulations. 

* Si on change les bots qui sont testés, le fichier est remis à 0 (seulement les nouvelles simulations sont prises en compte).

* Il est également possible d'exécuter *mvn exec:java -Dexec.args='-csv 50'*, cela permet d’améliorer le fichier csv avec un nombre de nouvelles simulations données (ici 50), si le nombre n’est pas valide (négatif) l’option n’est pas prise en compte, une partie normale est lancée.

### 4. Bot Richard et meilleur Bot

Nous avons implémenté quasiment toutes les fonctionnalités demandées pour le BotRichard:

* Le choix du rôle en fonction de l’état de la partie, selon si des joueurs menacent de finir en fonction de leur rôle potentiel, de leur nombre de quartiers construits et de leur ordre dans le choix des rôles
  * Prendre l’assassin pour contrer un joueur qui menace de finir avec l’architecte en tuant l’Architecte
  * Prendre (dans l’ordre) le Roi, l’Assassin, le Condottiere et l'Évêque si un adversaire a 6 quartiers construits
* Ciblage des Bots les plus avancés selon l’état de la partie
  * Pour reprendre l’exemple ci-dessus, si un joueur a 6 quartiers construits et qu’on récupère le rôle d’assassin, Bot Richard va tuer le rôle le plus probable que l’adversaire ai choisi
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
Le code implémenté permet de simuler une partie complète de Citadelles, avec toutes les règles, tous les quartiers, tous les effets des quartiers violets, avec des Bots qui jouent selon différentes stratégies assez claires.
Les responsabilités des choix au cours d’une partie sont bien faites par les Bots en fonction de leur stratégie.
La couverture du code est assez efficace, nous avons mis beaucoup d'énergie afin de maintenir ce niveau de couverture tout au long du projet.
Nous avons également mis en place des tests unitaires pour chaque classe, et des tests d’intégration pour les classes qui en avaient besoin.


### 4. Améliorations effectuées grâce à Sonarlint/Sonarqube
Ces 2 outils nous ont permis de cibler certaines parties du code à améliorer. Par exemple l’ouverture du .csv était relevé en bug dans Sonarqube, mais pas repéré par Sonarlint. Nous avons donc modifié l’ouverture du fichier en try-with-resources.

Nous avons pu cibler certaines parties du code qui était dupliquées dans plusieurs endroits du code. De même pour le refactoring de certains éléments, ce qui nous a permis de rendre le code plus clair, plus lisible.

### 5. Améliorations possibles


Si on avait plus de temps, nous aurions aimer:
* Améliorer les méthodes avec une grande complexité cognitive, notamment dans la classe [Bot Richard](../src/main/java/Citadelle/teamU/moteurJeu/bots/malin/BotRichard.java) où on a des méthodes qui traitent plusieurs cas avec beaucoup de conditions imbriqués et des nom de variables qui sont pas toujours clair.
* Actuellement, nous avons mis en place plusieurs méthodes void qui devrait retourner des objets, notamment dans la classe [Bot](../src/main/java/Citadelle/teamU/moteurJeu/bots/Bot.java) les méthodes d’actions spéciales des rôles. Par exemple la fonction void actionSpecialeAssassin devrait renvoyer le rôle qui s’est fait tué, afin d’avoir un code plus cohérent mais également pour les tests, car on pourrait mocker plus facilement les actions.
* Refactoring de la classe [Tour](../src/main/java/Citadelle/teamU/moteurJeu/Tour.java) parce qu’elle est instanciée une seule fois, au lieu de reinstancier un Tour à chaque début de tour, on appelle la fonction void prochainTour().
* Amélioration des nom des branches de fonctionnalités pour que ce soit plus clair
* Mettre en place du versionning en posant un tag à la fin de chaque milestone.


## Processus 
### 1. Répartition du travail
Pour la réparation du travail nous avons décidé de nous attribuer des tâches différentes chaque semaine. Nous faisions 1 milestone par semaine (hors sprint final où nous faisions 1 milestone par jour) avec des issues (liée à cette mistone) que nous répartissions.

Nos milestones sont verticales, quand nous ajoutons un nouveau rôle nous adaptons aussi toutes les stratégies des Bots pour ce rôle dans la même milestone. Le but est d’avancer sur notre ou nos issues pour la semaine prochaine.

Chacun a fait au moins un rôle et un bot (une stratégie spécifique) durant le projet pour permettre une compréhension globale du projet par tout le monde. La personne qui produit un code fait les tests pour ce qu’il a fait.

### 2. Utilisation de git
Nous suivons la stratégie github flow qui correspondait le plus à notre mode de fonctionnement. 
Voir plus de détails [ici](CONTRIBUTING.md)