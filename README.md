# Cawberry !

Cette application web est un désir de créer un équivalent français à Reddit. Elle présente donc des "sous-baies" que chaque utilisateur peut créer, contenant un ensemble de posts où chaque utilisateur pourra s'engager dans une conversation en rapport avec ces posts.


# Installation

Afin de lancer l'application web, il est nécessaire d'avoir la version **1.5.x** du framework Play! et Python 2.
Afin d'installer toutes les dépendances, il faudra lancer la commande 
> play dependencies .

au niveau de la racine du projet. Si le module pagination n'est pas disponible, il faudra rajouter son *.jar* en dépendance du projet.
Nous utilisons IntelliJ pour développer le projet. Si vous souhaitez l'utiliser aussi, il faudra lancer la commande 
> play idealize .

au niveau de la racine du projet après avoir rajouté les dépendances.
 
# Comptes Utilisateurs

La création de compte étant parfois buggé, nous peuplons une multitude de compte utilisateurs dans notre base de données au chargement de l'application.
Vous pouvez donc utiliser les deux comptes suivants pour tester les différentes fonctionnalités : 

* **Compte Administrateur** : boblennon@gmail.com / boblennonMDP
* **Compte Normal** : jeff@gmail.com / jeffMDP 

Les mots de passes étant sauvegardés avec un hash et un sel dans la base de donnés il aurait été compliqué de se connecter sans ces logs.
## Lancer le projet sous environnement DEV

Il est possible de lancer le projet sous environnement DEV afin de rapidement vérifier certaines fonctionnalités, modifier le code Java en temps réel etc..

Il faut pour cela lancer la commande suivante sans changer d'id à la racine du projet :
> play run .

Il faudra alors se connecter à [l'index du serveur](http://localhost:9000) (localhost port 9000)

## Lancer le projet sous environnement TESTS

Il est possible de lancer le projet sous environnement TESTS afin de vérifier plus en détail chacune des fonctionnalités précédemment implémentés (tests de non régression), des nouvelles fonctionnalités (tests unitaires, tests sélénium, tests applicatif)..

Il faut pour cela lancer la commande suivante sans changer d'id à la racine du projet : 
> play test .

Il faudra alors se connecter à [l'index des tests du serveur](http://localhost:9000/@tests) (localhost port 9000)

## Lancer le projet sous environnement PROD 

Il est possible de lancer le projet sous environnement PROD afin de le lancer officiellement sur le port 80 d'un serveur web. L'application ainsi déployée pourra alors être accessible à tous une fois que le développeur se sera assuré de ses fonctionnalités et de sa sécurité.

Il faut pour cela dans un premier temps changer l'id du serveur hébergeant l'application à l'aide de la racine :
> play id 

On prendra alors soin de choisir comme id **prod**.

On lance alors l'application dans le serveur de la même manière : 
>play run .

Il faudra alors se connecter à [l'index du server](http://localhost:80) (localhost port 80)

## Fonctionnalités implémentés

* Il est possible de **créer** un utilisateur, **se connecter** et **se déconnecter** de manière plus ou moins sécurisée (génération d'un sel aléatoire à la création et hash avec SHA-256). 
* La création d'utilisateur, la connexion et la déconnexion permettent de **revenir automatiquement** vers la dernière page visitée après avoir terminer l'action demandée.
* Il est possible de **créer** un subberry, **l'afficher** et **afficher** les posts qu'il contient (s'il en a).
* Chaque post étant un espace de discussion entre les membres, il est possible pour chaque utilisateur et chaque visiteur non connecté d'**afficher** les commentaires des utilisateurs. Chaque utilisateur pourra aussi **commenter** sur le post à condition qu'il soit connecté.
* Toutes ces créations se font de manière plus ou moins sécurisée grâce à un token d'authentification généré automatiquement par Play!, empêchant tout **Cross Site Request Forgery**.
* Un subberry possédant plus de 3 posts est considéré comme "gros", il aura donc droit à un **bel effet carousel** présentant ses 3 derniers posts.
* La barre de navigation contient les 3 principaux subberry en terme de nombre d'utilisateurs abonnés. Ils seront donc ajoutés dynamiquement à la création de la page. Le bouton "Afficher tous les subs" permettra de rechercher le subberry de notre choix.
* Un système de **pagination** est disponible pour l'affichage des posts et des subberrys au cas où il y en aurait beaucoup.
* Enfin, les utilisateurs **administrateurs** pourront quant à eux décider de **supprimer** et **modifier** des subberries, des posts et des messages qui ne sont pas les leurs, action que les simples utilisateurs ne pourront pas faire.

Concernant la partie administrateur, nous avions oublié le pré-requis que l'application devait avoir deux rôles d'utilisateurs bien distincts, nous avons donc rajoutés cette partie à la dernière minute sans peaufiner le style. C'est donc le style template du module CRUD de Play! et une page HTML sans style fournie lors du tutoriel guidé qui sont fournis pour cette partie, à notre plus grand regret.

## Extensions

Il est possible de modifier le fichier *application.conf* plus en profondeur pour ajouter, par exemple, une connexion à une base de données MySQL ou PostgreSQL. Ainsi toute modification restera persistante au lieu de disparaître à chaque redémarrage de l'application.
