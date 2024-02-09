# Projet Citadelle - Equipe U
Le but de ce projet est de reproduire un jeu de Citadelle avec 4 bots intelligent qui s'affrontent.

## Sommaire
* [Rapport](documentation/RAPPORT-U.md)
* [Contributing](documentation/CONTRIBUTING.md)

## Comment lancer le programme
1. `mvn clean package` : lance les tests
1. Plusieurs modes d'executions
   * `mvn exec:java -Dexec.args='--demo'` permet de lancer une partie
   * `mvn exec:java -Dexec.args='--2thousands'` affiche les statistiques de victoire, défaite, égalité, moyenne des points de 4 bots différents qui s'affrontent sur 1000 parties et de 4 bots focus Roi qui s'affrontent sur 1000 parties.
   * `mvn exec:java -Dexec.args='--csv'` Si un fichier csv n'existe pas déjà il crée un fichier csv avec des statistiques de victoire, défaite, égalité, moyenne des points et moyenne des points en cas de victoire en fonction de chaque Bot pour 1000 simulations parties.
Si le fichier existe déjà les statistiques seront mise à jour avec 1000 nouvelles simulations.
Les bots qui s'affrontent peuvent facilement être changé, il est possible d'avoir plusieurs fois le même Bot, si les Bots sont changé le fichier est remis à 0 avec seulement les 1000 nouvelles simulations.
   * `mvn exec:java -Dexec.args='-csv 70'` est identique à --csv, la difference est que l'on peut specifier combien de simulation on choisit de faire (pas forcément 1000)
   * `mvn exec:java -Dexec.args='--demo --2thousands --csv -csv 70'` lance toutes les commandes d'un coup (on peut en mettre que certains et dans n'importe quel ordre)