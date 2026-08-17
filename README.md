# Number Guessing Game — JavaFX

A desktop guessing game built with **JavaFX** and **FXML**: the program picks a
number between 1 and 100, and you narrow it down with "too high" / "too low"
feedback. Two scenes — a title screen and the game screen — with navigation
between them.

Coursework project, TH Aschaffenburg.

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-23-5382A1)

---

## How it works

### Scene structure

Two FXML layouts, both driven by the same controller:

| File | Role |
|---|---|
| `hello-view.fxml` | Title screen — heading, tagline, **Start** button |
| `new.fxml` | Game screen — guess input, feedback label, submit and reset |

`HelloApplication.start()` loads `hello-view.fxml` as the entry point.
Navigation reloads a layout into the **existing** `Stage`:

```java
public void switchToScene2(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("new.fxml"));
    stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.show();
}
```

The `(Stage)((Node) event.getSource()).getScene().getWindow()` chain walks from
the clicked button up to the window that contains it — the standard FXML way to
reach the `Stage` from a controller that was never handed one.

A side effect worth knowing: because each navigation calls `FXMLLoader.load`
fresh, a **new controller instance** is constructed and `initialize()` runs
again — which calls `resetGame()`. So leaving the game screen and returning
picks a new target number. Scene switching *is* the reset path.

### Game logic

```java
private void resetGame() {
    Random random = new Random();
    targetNumber = random.nextInt(maxNumber) + 1;   // 1..100 inclusive
}
```

`nextInt(100)` yields 0–99, so the `+ 1` shifts the range to 1–100 and makes
both bounds reachable — the off-by-one that this kind of code usually gets wrong.

`handleSubmitGuess()` handles four cases: out of range, too low, too high, and
correct. Non-numeric input is caught by `NumberFormatException` rather than
validated up front, so typing letters gives "Please enter a valid number!"
instead of crashing.

### Module system

The project is a proper JPMS module:

```java
module com.example.projjjjj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.projjjjj to javafx.fxml;   // reflection for @FXML injection
    exports com.example.projjjjj;
}
```

`opens ... to javafx.fxml` is required: FXML injects `@FXML` fields reflectively,
and without opening the package the loader cannot reach them. A missing `opens`
here is the usual cause of a `@FXML`-annotated field arriving as `null`.

---

## Building and running

**This repository does not currently build from a clean clone** — see
*Known issues* below. There is no `pom.xml` or `build.gradle`, only sources.

To run it you need to supply the build yourself. With Maven, add a `pom.xml`
with the `javafx-maven-plugin` and:

```bash
mvn javafx:run
```

Or compile directly against a local JavaFX SDK:

```bash
javac --module-path /path/to/javafx-sdk/lib \
      --add-modules javafx.controls,javafx.fxml \
      -d out $(find . -name "*.java")

java  --module-path /path/to/javafx-sdk/lib \
      --add-modules javafx.controls,javafx.fxml \
      -cp out com.example.projjjjj.HelloApplication
```

Requires Java 17+ and JavaFX 23 (the version the FXML files declare).

---

## Known issues

Recorded honestly rather than papered over:

1. **No build file.** Neither Maven nor Gradle configuration is committed, so
   the project cannot be compiled without the developer reconstructing it.
   Adding a `pom.xml` with the JavaFX plugin is the single highest-value fix.

2. **The package is still the IDE default** — `com.example.projjjjj`. It should
   be something meaningful; renaming it touches `module-info.java` and the
   `fx:controller` attribute in both FXML files.

3. **A directory is misspelled** — `Guessing gamr`, which also contains a space,
   making command-line paths awkward to quote.

4. **`javax.swing.*` is imported into a JavaFX controller.** It is unused, but
   pulling Swing into a JavaFX application is exactly the mixed-toolkit mistake
   worth not shipping. Several imports are also duplicated (`FXML`, `Label`,
   `TextField` each appear twice).

5. **Winning does not end the round.** After "You Win!" the input stays live and
   further guesses are still evaluated against the same number. A `boolean
   gameOver` flag guarding `handleSubmitGuess` would fix it.

6. **No attempt counter.** The most obvious missing feature for a guessing game
   — the score is how few tries you needed.

7. **A new `Random` is constructed on every reset.** Harmless here, but a single
   `Random` field is the conventional approach; repeatedly constructing them in
   a tight loop can produce correlated seeds.

## Project structure

```
guessing-game-app-main/
└── Guessing gamr/
    └── src/main/
        ├── java/
        │   ├── module-info.java
        │   └── com/example/projjjjj/
        │       ├── HelloApplication.java   entry point, loads the title scene
        │       └── HelloController.java    navigation + game logic
        └── resources/com/example/projjjjj/
            ├── hello-view.fxml             title screen
            └── new.fxml                    game screen
```

The Maven-style `src/main/java` and `src/main/resources` layout is already
correct — which is what makes the missing `pom.xml` so conspicuous.

## Author

**Shehan Nimsara** — B.Sc. Software Design (International), TH Aschaffenburg
