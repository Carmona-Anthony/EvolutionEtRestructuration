#README Evolution et Restructuration

Carmona Anthony et Pyz Maxime

### Prérequis : 

- Le dossier à analyser doit etre changé dans la variable projectPath de la classe Parser.

### Visualisation

Afin de visualiser le résultat de la sortie JSON, lancer le fichier index.html présent dans le dossier Visualisation

### Présentation

Dans le package Visitor : L'ensemble des visiteur utilisé dans ce TP afin de permettre l'extraction de connaissances (Parcours de L'AST).
Dans le package Decorator : L'ensemble des classes à décorer (Classes représentant les noeuds de l'AST et leurs manipulations).
Dans le package Cluster : Vous trouverez l'ensemble des fonctionnalités pour la gestion des clusters (TP4).
Dans le package FileHandling : Vous trouverez la classe permettant la gestion d'ecriture dans les fichiers.

La classe GenericCompute présente dans le package par défault permet la gestion de lists et de valeurs en utilisant l'ensemble des fonctionnalités proposées par Java 8.

Dans le cadre de ce TP, nous avons réaliser l'ensemble des questions posées ainsi qu'une visualisation se basant sur un graphe d'appel (Method -> Method) ainsi qu'un tutoriel se basant sur l'utilisation de l'outil Spoon.