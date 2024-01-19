
# Guide rapide d'installation (JDK/JavaFX/Gradle)


Ce document vous guide rapidement sur l'installation et la configuration de votre environnement.
Vous allez ainsi installer "à la main", un kit de développement Java (Java SDK), la librairie JavaFX et réaliser un premier programme.


## Projet souche Gradle/JavaFX/JUnit

Pour simplifier votre démarrage et si vous disposez déjà d'un kit de développement Java, vous pouvez utiliser la souche de projet que nous vous proposons. Pour cela, vous devez simplement cloner le projet git disponible à l'adresse suivante : https://gitlab.telecomnancy.univ-lorraine.fr/projets/2324/pcd2k24/pcd2k24-javafx-bootstrap

Cette souche contient un projet directement utilisable en utilisant le moteur de production Gradle (https://gradle.org/). Vous n'avez rien à installer, l'outil téléchargera les dépendances pour vous.

```bash
git clone git@gitlab.telecomnancy.univ-lorraine.fr:projets/2324/pcd2k24/pcd2k24-javafx-bootstrap.git
cd pcd2k24-javafx-bootstrap
./gradlew run
```

Après quelques instants, le temps que l'outil télécharge les différentes dépendances (l'application Gradle et ses dépendances, la librairie JavaFX et ses dépendances, la libraire JUnit, etc.), compile le code de l'application exemple, vous devriez voir apparaître une fenêtre avec un bouton sur votre écran.

Le projet souche suit la configuration standard d'un projet Java, à savoir :

```
pcd2k24-javafx-bootstrap
├── build.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── eu
    │   │       └── telecomnancy
    │   │           ├── Main.java
    │   │           └── ...
    │   └── resources
    │       └── ...
    └── test
        ├── java
        │   └── eu
        │       └── telecomnancy
        │           ├── MainTest.java
        │           └── ...
        └── resources
            └── ...
```

- le répertoire `src/main/java` contient le code source Java des classes de votre application (dans des sous-répertoires correspondant aux paquetages de votre application).
- le répertoire `src/main/resources` contient les fichiers de ressources (images, données, etc.) de votre application (potentiellement dans des sous-répertoires correspondant aux paquetages de votre application) ;
- le répertoire `src/test/java` contient le code source Java des classes de tests de votre application (dans des sous-répertoires correspondant aux paquetages de votre application) ;
- le répertoire `src/test/resources` contient les fichiers de ressources (images, données, etc.) nécessaires à l'exécution des tests de votre application (potentiellement dans des sous-répertoires correspondant aux paquetages de votre application).



Ce projet souche est directement utilisable dans Visual Studio Code ou IntelliJ.

### Utilisation dans Visual Studio Code

Pour utiliser ce projet dans Visual Studio Code, il suffit d'ouvrir le répertoire que vous venez de cloner en tant que projet dans Visual Studio Code.

Vous aurez besoin d'avoir installé le paquet d'extensions Java ainsi que l'extension pour Gradle (https://code.visualstudio.com/docs/java/extensions).

Le projet devrait se configurer automatiquement (*classpath*, répertoire des ressources, etc.). Pour compiler/exécuter le projet, vous utiliserez alors soit le terminal (par exemple avec la commande `./gradlew run`) ou l'icône de l'onglet Gradle (cf. le petit éléphant).


### Utilisation dans IntelliJ

Pour utiliser ce projet dans IntelliJ, il suffit d'ouvrir ~~le répertoire~~ le fichier `build.gradle` présent dans le répertoire vous venez de cloner en tant que projet dans IntelliJ.

Le projet devrait se configurer automatiquement (*classpath*, répertoire des ressources, etc.). Pour compiler/exécuter le projet, vous utiliserez alors soit le terminal (par exemple avec la commande `./gradlew run`) ou l'icône de l'onglet Gradle (cf. le petit éléphant).


> ٩◔‿◔۶
>
> ℹ️ **NOTE**
>
> Le reste de ce guide est uniquement destinés aux élèves souhaitant installer les différentes dépendances "à la main".
>
>(✿◠‿◠)


## Installation d'un JDK (Java Development Kit)

Vous aurez besoin d'un JDK d'installé. Nous vous invitons à installer une version LTS (Long Term Support) de préférence (version 17 ou 21).

Vous pouvez télécharger une version d'OpenJDK pour votre système d'exploitation à l'adresse suivante : https://adoptium.net/temurin/releases
Depuis cette page, choisissez votre système d'exploitation, votre architecture (le plus souvent x64), le type de paquetage (JDK et non JRE), et la version (17).

Après avoir téléchargé l'archive, décompresser celle-ci à un emplacement que vous noterez.


### Décompression de l'archive (en ligne de commande)

```sh
tar xvfz OpenJDK17U-jdk_x64_mac_hotspot_17.0.9_9.tar.gz
```

### Mise en place des variables d'environnement `JAVA_HOME` et `PATH`

Il est recommandé de définir une variable d'environnement `JAVA_HOME` pointant vers le répertoire d'installation de votre JDK. Puis de mettre à jour votre variable d'environnement `PATH` pour avoir accès aux commandes `java` et `javac`.

Vous pouvez réaliser cela avec les lignes de commandes suivantes :

```sh
export JAVA_HOME=$(pwd)/jdk-17.0.9+9 # Attention ici, $(pwd) correspond au répertoire où vous avez précédemment décompressé l'archive du JDK
export PATH=${JAVA_HOME}/bin:${PATH}
```

Ces variables ne sont pas persistantes d'une session à l'autre. Si vous voulez les rendre persistantes, le mieux est de les ajouter au fichier des commandes qui sont exécutées à l'ouverture de votre *shell* (`~/.bashrc` ou `~/.zshrc` par exemple).

Sous Windows, le mieux est d'ajouter ces définitions à vos variables d'environnement système. Vous pouvez :
- soit en passer par l'interface graphique spécifique (vous pouvez normalement y accéder en effectuant la recherche `env` dans votre barre de recherche) ;
- soit utiliser les lignes de commande suivantes :

```shell
setx /m JAVA_HOME "C:\Progra~1\Java\jdk-17.0.9+9"
setx /m PATH "%JAVA_HOME%\bin;%PATH"

# Pensez à redémarrer votre terminal pour que les nouvelles valeurs des variables d'environnement soient prises en compte
```

À noter que sous Windows, la valeur d'une variable d'environnement s'obtient par `%JAVA_HOME%` et non pas `${JAVA_HOME}`


## Librarie JavaFX

Vous aurez également besoin de télécharger la librairie JavaFX. À partir de la version 11 du JDK, cette librairie ne fait plus partie du JDK.
Nous vous recommandons de télécharger la version 17 (ou 21).

Vous pouvez trouver une distribution (binaire compilée) sur le site web : https://gluonhq.com/products/javafx/
À partir de cette, choisissez la version (17), votre système d'exploitation, votre architecture et le type d'archive (SDK)

Après avoir téléchargé l'archive, décompresser celle-ci à un emplacement que vous noterez.


### Mise en place de la variable d'environnement `JAVAFX_HOME` qui sera utilisée lors des phases de compilation/exécution

Voici l'exemple de la commande pour définir la variable d'environnement sous un shell bash/zsh. Il faut bien entendu adapter cette commande pour un environnement Windows (voir la section précédente sur la définition des variables d'environnement sous Windows).

```bash
export JAVAFX_HOME=$(pwd)/javafx-sdk-17.0.9 # Attention ici, $(pwd) correspond au répertoire où vous avez précédemment décompressé l'archive du SDK JavaFX
```


## Téléchargement du kit Scene Builder (pour plus tard)

https://gluonhq.com/products/scene-builder/#download 

- Liens direct : https://download2.gluonhq.com/scenebuilder/21.0.0/scenebuilder-kit-21.0.0.jar



## Application Exemple JavaFx

Pour tester votre installation en ligne de commande, nous allons écrire un premier programme utilisant la librairie graphique JavaFX.

On commence par créer un répertoire `src/`.

```sh
mkdir src
```

Vous saisirez le code Java suivant dans un fichier `src/Main.java` :

```java
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

public class Main extends Application {

public static void main(String[] args) {
    Application.launch(args);
}

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello world JavaFX Application");
        primaryStage.setWidth(300);
        primaryStage.setHeight(200);

        Label helloWorldLabel = new Label("Hello world!");
        helloWorldLabel.setAlignment(Pos.CENTER);
        Scene primaryScene = new Scene(helloWorldLabel);

        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

}
```


### Compilation de l'exemple

La librairie JavaFX étant en dehors du JDK, il est nécessaire de l'*activer* en l'ajoutant les modules correspondant (pour faire simple, les modules sont des archives contenant à la fois des classes Java compilées et également des librairies natives de type `.dll`, `.dylib` `.so` en fonction de votre système). 

Dans la commande suivante, on précise donc le répertoire où se trouvent les modules que l'on veut activer et le nom des modules (on se contente pour l'instant des modules `javafx.base` et `javafx.controls`, plus tard vous serez amené à en ajouter d'autres, notamment `javafx.fxml`).

```bash
javac -classpath src/ --module-path ${JAVAFX_HOME}/lib --add-modules=javafx.base,javafx.controls src/Main.java
```

### Exécution de l'exemple

De même, lors de l'exécution, il est nécessaire d'activer les modules liés à la librairie JavaFX.

```bash
java -classpath src/ --module-path ${JAVAFX_HOME}/lib --add-modules=javafx.base,javafx.controls Main
```



## Installation IntellJ et configuration d'un projet JavaFX

Dans les différents TPs nous utiliserons l'environnement de développement intégré (IDE) IntelliJ. Vous pouvez télécharger et installer cet environnement sur votre ordinateur. La version *Community* sera suffisante, mais sachez qu'avec le programme éducation vous avez accès la version *Ultimate*.

Lien du site JetBrains IntelliJ : https://www.jetbrains.com/fr-fr/idea/


- Lancer IntelliJ
- Choisissez "New Project" -> "Java FX"
- Dans "Project SDK" -> ajoutez un nouveau JDK ("Add JDK")
    - indiquez le chemin où vous avez installé votre JDK17.
- Choisissez "JavaFX Application"
- Dans "Project Structure" -> "Project Settings" -> "Modules", 
    - choisissez "+" -> "Library" -> "Java"
        - indiquez le chemin où vous avez installé la librairie JavaFX
        - donnez comme nom "JavaFX" à cette nouvelle librairie que vous venez d'ajouter
- Dans "Run" -> "Edit configurations"
    - choisissez "Application" -> "Main"
    - et ajouter dans "VM Options" : ``--module-path %REPERTOIRE_JAVAFX%/lib --add-modules javafx.controls,javafx.fxml`` où vous remplacez `%REPERTOIRE_JAVAFX%` par le chemin vers le répertoire où vous avez installé la librairie JavaFX.
- Dans "Settings" -> "Languages and Frameworks" -> JavaFX
    - indiquez dans "Path to SceneBuilder", le chemin vers le .jar "Scene Builder Kit" que vous avez téléchargé.

Si vous souhaitez des instructions plus complètes, vous pouvez consulter les pages : 

- https://www.jetbrains.com/help/idea/javafx.html#add-javafx-lib
- https://www.jetbrains.com/help/idea/opening-fxml-files-in-javafx-scene-builder.html#open-in-scene-builder
