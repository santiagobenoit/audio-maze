/*
 * Audio Game
 * Version 1.2
 * By Santiago Benoit
 */
package com.santiagobenoit.audiogame.src;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * The graphical user interface.
 * @author Santiago Benoit
 */
public class GUI extends Application {
    
    @Override
    public void start(Stage stage) {
        // Title Scene
        this.stage = stage;
        maze = new Maze();
        maze.setWidth(10);
        maze.setHeight(10);
        ready = false;
        mazeDir = new File("Mazes");
        myMazeDir = new File("Mazes/My Mazes");
        BorderPane border = new BorderPane();
        VBox info = new VBox();
        info.setPadding(new Insets(10, 10, 10, 10));
        info.setSpacing(10);
        Text title = new Text("Audio Game");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        Text author = new Text("By Santiago Benoit");
        author.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        info.getChildren().addAll(title, author);
        info.setAlignment(Pos.CENTER);
        HBox buttons = new HBox();
        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setSpacing(10);
        Button play = new Button();
        play.setText("Play");
        play.setOnAction((ActionEvent event) -> {
            FileChooser mazeChooser = new FileChooser();
            mazeChooser.setTitle("Select a Maze");
            mazeChooser.getExtensionFilters().add(new ExtensionFilter("Maze Files", "*.maze"));
            if (mazeDir.exists()) {
                mazeChooser.setInitialDirectory(mazeDir);
            }
            File mazeFile = mazeChooser.showOpenDialog(stage);
            if (mazeFile != null) {
                try {
                    maze = Maze.deserialize(mazeFile.getPath());
                    original = new Maze(maze);
                    ready = true;
                } catch (IOException | ClassNotFoundException e) {
                    ready = false;
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.initOwner(stage);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("There was a problem loading the maze.");
                    alert.showAndWait();
                }
                if (ready) {
                    stage.setScene(setupScene);
                    stage.setTitle("Audio Game (Game Settings)");
                    stage.setWidth(640);
                    stage.setHeight(480);
                    stage.setMinWidth(640);
                    stage.setMinHeight(480);
                    stage.centerOnScreen();
                }
            }
        });
        Button editor = new Button();
        editor.setText("Maze Editor");
        editor.setOnAction((ActionEvent event) -> {
            maze = new Maze();
            maze.setWidth(10);
            maze.setHeight(10);
            stage.setScene(editorScene);
            stage.setTitle("Audio Game (Maze Editor)");
            stage.setWidth(640);
            stage.setHeight(480);
            stage.setMinWidth(640);
            stage.setMinHeight(480);
            stage.centerOnScreen();
        });
        buttons.getChildren().addAll(play, editor);
        buttons.setAlignment(Pos.CENTER);
        Text versionText = new Text("Version " + VERSION);
        HBox versionBox = new HBox();
        versionBox.setPadding(new Insets(10, 10, 10, 10));
        versionBox.getChildren().add(versionText);
        versionBox.setAlignment(Pos.CENTER);
        border.setTop(info);
        border.setCenter(buttons);
        border.setBottom(versionBox);
        titleScene = new Scene(border);
        stage.setScene(titleScene);
        stage.setTitle("Audio Game");
        stage.getIcons().add(new Image("/com/santiagobenoit/audiogame/resources/images/icon.png"));
        stage.setWidth(300);
        stage.setHeight(200);
        stage.setMinWidth(300);
        stage.setMinHeight(200);
        stage.centerOnScreen();
        stage.show();
        // Setup Scene
        BorderPane setupBorder = new BorderPane();
        Text setupTitle = new Text("Game Settings");
        setupTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        VBox topBox = new VBox();
        topBox.getChildren().add(setupTitle);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10, 10, 10, 10));
        CheckBox marbleCheck = new CheckBox("Enable marble limit");
        marbleCheck.textFillProperty().set(Color.BLACK);
        Spinner marbleLimiter = new Spinner();
        marbleLimiter.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        marbleLimiter.setPrefWidth(60);
        marbleLimiter.setDisable(true);
        CheckBox timeCheck = new CheckBox("Enable time limit (minutes : seconds)");
        timeCheck.textFillProperty().set(Color.BLACK);
        Spinner minutes = new Spinner();
        minutes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        minutes.setPrefWidth(60);
        minutes.setDisable(true);
        Text colon = new Text(":");
        Spinner seconds = new Spinner();
        seconds.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60));
        seconds.setPrefWidth(60);
        seconds.setDisable(true);
        HBox timeLimiter = new HBox();
        timeLimiter.setSpacing(10);
        timeLimiter.getChildren().addAll(minutes, colon, seconds);
        CheckBox hardcoreCheck = new CheckBox("Hardcore Mode");
        hardcoreCheck.textFillProperty().set(Color.BLACK);
        marbleCheck.setOnAction((ActionEvent event) -> {
            if (marbleCheck.isSelected()) {
                marbleLimiter.setDisable(false);
            } else {
                marbleLimiter.setDisable(true);
            }
        });
        timeCheck.setOnAction((ActionEvent event) -> {
            if (timeCheck.isSelected()) {
                minutes.setDisable(false);
                seconds.setDisable(false);
            } else {
                minutes.setDisable(true);
                seconds.setDisable(true);
            }
        });
        VBox gameSettingsBox = new VBox();
        gameSettingsBox.setPadding(new Insets(10, 10, 10, 10));
        gameSettingsBox.setSpacing(10);
        gameSettingsBox.getChildren().addAll(marbleCheck, marbleLimiter, timeCheck, timeLimiter, hardcoreCheck);
        gameSettingsBox.setAlignment(Pos.TOP_LEFT);
        Button okBtn = new Button("OK");
        okBtn.setOnAction((ActionEvent event) -> {
            if (marbleCheck.isSelected()) {
                marbleLimit = (Integer) marbleLimiter.getValue();
            } else {
                marbleLimit = -1;
            }
            if (timeCheck.isSelected()) {
                timeLimit = ((Integer) minutes.getValue() * 60000) + ((Integer) seconds.getValue() * 1000);
            } else {
                timeLimit = -1L;
            }
            hardcoreEnabled = hardcoreCheck.isSelected();
            stage.setScene(playScene);
            stage.setTitle("Audio Game");
            stage.setWidth(640);
            stage.setHeight(480);
            stage.setMinWidth(640);
            stage.setMinHeight(480);
            stage.centerOnScreen();
            playGame();
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction((ActionEvent event) -> {
            stage.setScene(titleScene);
            stage.setTitle("Audio Game");
            stage.setWidth(300);
            stage.setHeight(200);
            stage.setMinWidth(300);
            stage.setMinHeight(200);
            stage.centerOnScreen();
        });
        HBox okCancel = new HBox();
        okCancel.setPadding(new Insets(10, 10, 10, 10));
        okCancel.setSpacing(10);
        okCancel.getChildren().addAll(okBtn, cancelBtn);
        okCancel.setAlignment(Pos.BOTTOM_RIGHT);
        setupBorder.setTop(topBox);
        setupBorder.setCenter(gameSettingsBox);
        setupBorder.setBottom(okCancel);
        setupScene = new Scene(setupBorder);
        // Play Scene
        Text line1 = new Text("GAME IN PROGRESS");
        Text line2 = new Text("Press ESC to exit to the main menu.");
        Text line3 = new Text("Controls:");
        Text line4 = new Text("W or Up Arrow - move forwards");
        Text line5 = new Text("S or Down Arrow - move backwards");
        Text line6 = new Text("A or Left Arrow - rotate left");
        Text line7 = new Text("D or Right Arrow - rotate right");
        Text line8 = new Text("Space - roll marble");
        Text line9 = new Text("E or Enter - interact");
        Text line10 = new Text("Backspace - restart");
        line1.setFill(Color.WHITE);
        line2.setFill(Color.WHITE);
        line3.setFill(Color.WHITE);
        line4.setFill(Color.WHITE);
        line5.setFill(Color.WHITE);
        line6.setFill(Color.WHITE);
        line7.setFill(Color.WHITE);
        line8.setFill(Color.WHITE);
        line9.setFill(Color.WHITE);
        line10.setFill(Color.WHITE);
        VBox playInfo = new VBox();
        playInfo.getChildren().addAll(line1, line2, line3, line4, line5, line6, line7, line8, line9, line10);
        playInfo.setAlignment(Pos.CENTER);
        StackPane playPane = new StackPane();
        playPane.setStyle("-fx-background-color: #000000");
        playPane.getChildren().add(playInfo);
        playScene = new Scene(playPane);
        // Editor Scene
        zoom = 25;
        widthVal = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
        heightVal = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
        widthVal.setValue(10);
        heightVal.setValue(10);
        selectedTool = "floor";
        BorderPane editorBorder = new BorderPane();
        Button newBtn = new Button("New");
        newBtn.setOnAction((ActionEvent event) -> {
            maze = new Maze();
            maze.setWidth(10);
            maze.setHeight(10);
            widthVal.setValue(10);
            heightVal.setValue(10);
            refreshMaze();
        });
        Button openBtn = new Button("Open");
        openBtn.setOnAction((ActionEvent event) -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open Maze");
            chooser.getExtensionFilters().add(new ExtensionFilter("Maze Files", "*.maze"));
            if (myMazeDir.exists()) {
                chooser.setInitialDirectory(myMazeDir);
            }
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    maze = Maze.deserialize(file.getPath());
                    widthVal.setValue(maze.getWidth());
                    heightVal.setValue(maze.getHeight());
                } catch (IOException | ClassNotFoundException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.initOwner(stage);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("There was a problem opening the maze.");
                    alert.showAndWait();
                }
                refreshMaze();
            }
        });
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction((ActionEvent event) -> {
            if (!maze.hasTile(TileStart.class)) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.initOwner(stage);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Your maze needs a start!");
                alert.showAndWait();
            } else if (!maze.hasTile(TileFinish.class)) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.initOwner(stage);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Your maze needs a finish!");
                alert.showAndWait();
            } else {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Save Maze");
                chooser.getExtensionFilters().add(new ExtensionFilter("Maze Files", "*.maze"));
                if (myMazeDir.exists()) {
                    chooser.setInitialDirectory(myMazeDir);
                }
                File file = chooser.showSaveDialog(stage);
                if (file != null) {
                    try {
                        maze.serialize(file.getPath());
                    } catch (IOException e) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.initOwner(stage);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("There was a problem saving the maze.");
                        alert.showAndWait();
                    }
                }
            }
        });
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction((ActionEvent event) -> {
            stage.setScene(titleScene);
            stage.setTitle("Audio Game");
            stage.setWidth(300);
            stage.setHeight(200);
            stage.setMinWidth(300);
            stage.setMinHeight(200);
            stage.centerOnScreen();
            maze = new Maze();
            maze.setWidth(10);
            maze.setHeight(10);
            widthVal.setValue(10);
            heightVal.setValue(10);
            refreshMaze();
        });
        HBox actionBox = new HBox();
        actionBox.getChildren().addAll(newBtn, openBtn, saveBtn, exitBtn);
        actionBox.setPadding(new Insets(10, 10, 10, 10));
        actionBox.setSpacing(10);
        actionBox.setAlignment(Pos.CENTER);
        ScrollPane mazeScroll = new ScrollPane();
        mazeGrid = new GridPane();
        mazeGrid.setPadding(new Insets(10, 10, 10, 10));
        refreshMaze();
        mazeScroll.setContent(mazeGrid);
        mazeScroll.setPadding(new Insets(10, 10, 10, 10));
        ToggleGroup toolToggle = new ToggleGroup();
        RadioButton clearBtn = new RadioButton("Floor");
        clearBtn.setToggleGroup(toolToggle);
        clearBtn.setOnAction((ActionEvent event) -> {
            selectedTool = "floor";
        });
        clearBtn.setSelected(true);
        RadioButton wallBtn = new RadioButton("Wall");
        wallBtn.setToggleGroup(toolToggle);
        wallBtn.setOnAction((ActionEvent event) -> {
            selectedTool = "wall";
        });
        RadioButton doorBtn = new RadioButton("Door");
        doorBtn.setToggleGroup(toolToggle);
        doorBtn.setOnAction((ActionEvent event) -> {
            selectedTool = "door";
        });
        RadioButton keyBtn = new RadioButton("Key");
        keyBtn.setToggleGroup(toolToggle);
        keyBtn.setOnAction((ActionEvent event) -> {
            selectedTool = "key";
        });
        RadioButton waterBtn = new RadioButton("Water");
        waterBtn.setToggleGroup(toolToggle);
        waterBtn.setOnAction((ActionEvent event) -> {
            selectedTool = "water";
        });
        RadioButton startBtn = new RadioButton("Start");
        startBtn.setToggleGroup(toolToggle);
        startBtn.setOnAction((ActionEvent event) -> {
            selectedTool = "start";
        });
        RadioButton finishBtn = new RadioButton("Finish");
        finishBtn.setToggleGroup(toolToggle);
        finishBtn.setOnAction((ActionEvent event) -> {
            selectedTool = "finish";
        });
        HBox toolBox = new HBox();
        toolBox.getChildren().addAll(clearBtn, wallBtn, doorBtn, keyBtn, waterBtn, startBtn, finishBtn);
        toolBox.setPadding(new Insets(10, 10, 10, 10));
        toolBox.setSpacing(10);
        toolBox.setAlignment(Pos.CENTER);
        Text widthTxt = new Text("Width:");
        widthTxt.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Text heightTxt = new Text("Height:");
        heightTxt.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Spinner widthSpin = new Spinner();
        widthSpin.setValueFactory(widthVal);
        widthSpin.setPrefWidth(60);
        widthSpin.setOnMouseClicked((MouseEvent event) -> {
            maze.setWidth((Integer) widthSpin.getValue());
            refreshMaze();
        });
        widthSpin.setUserData(10);
        Spinner heightSpin = new Spinner();
        heightSpin.setValueFactory(heightVal);
        heightSpin.setPrefWidth(60);
        heightSpin.setOnMouseClicked((MouseEvent event) -> {
            maze.setHeight((Integer) heightSpin.getValue());
            refreshMaze();
        });
        heightSpin.setUserData(10);
        HBox widthBox = new HBox();
        HBox heightBox = new HBox();
        widthBox.setSpacing(10);
        heightBox.setSpacing(10);
        widthBox.getChildren().addAll(widthTxt, widthSpin);
        heightBox.getChildren().addAll(heightTxt, heightSpin);
        VBox settingsBox = new VBox();
        settingsBox.setPadding(new Insets(10, 10, 10, 10));
        settingsBox.setSpacing(10);
        settingsBox.getChildren().addAll(widthBox, heightBox);
        settingsBox.setAlignment(Pos.TOP_CENTER);
        Text zoomTxt = new Text("Zoom");
        zoomTxt.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        HBox zoomTxtBox = new HBox();
        zoomTxtBox.getChildren().add(zoomTxt);
        zoomTxtBox.setAlignment(Pos.CENTER);
        Button zoomIn = new Button("+");
        zoomIn.setMinSize(30, 30);
        zoomIn.setOnAction((ActionEvent event) -> {
            if (zoom < 100) {
                zoom += 5;
            }
            refreshMaze();
        });
        Button zoomOut = new Button("-");
        zoomOut.setMinSize(30, 30);
        zoomOut.setOnAction((ActionEvent event) -> {
            if (zoom > 5) {
                zoom -=5;
            }
            refreshMaze();
        });
        TilePane plusMinus = new TilePane();
        plusMinus.getChildren().addAll(zoomOut, zoomIn);
        plusMinus.setAlignment(Pos.CENTER);
        VBox zoomBox = new VBox();
        zoomBox.getChildren().addAll(zoomTxtBox, plusMinus);
        zoomBox.setSpacing(10);
        zoomBox.setPadding(new Insets(10, 10, 10, 10));
        StackPane left = new StackPane();
        left.setPrefWidth(150);
        StackPane right = new StackPane();
        right.setPrefWidth(150);
        left.getChildren().add(settingsBox);
        right.getChildren().add(zoomBox);
        editorBorder.setTop(actionBox);
        editorBorder.setCenter(mazeScroll);
        editorBorder.setBottom(toolBox);
        editorBorder.setLeft(left);
        editorBorder.setRight(right);
        KeyCombination ctrlN = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
        KeyCombination ctrlO = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
        KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        KeyCombination ctrlE = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
        editorScene = new Scene(editorBorder);
        editorScene.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (ctrlN.match(event)) {
                newBtn.fire();
            }
            if (ctrlO.match(event)) {
                openBtn.fire();
            }
            if (ctrlS.match(event)) {
                saveBtn.fire();
            }
            if (ctrlE.match(event)) {
                exitBtn.fire();
            }
        });
    }
    
    public void refreshMaze() {
        mazeGrid.getChildren().clear();
        maze.initializeTiles();
        for (int x = 0; x < maze.getWidth(); x++) {
            for (int y = 0; y < maze.getHeight(); y++) {
                int fx = x;
                int fy = y;
                StackPane stack = new StackPane();
                stack.setMinWidth(zoom);
                stack.setMinHeight(zoom);
                stack.setMaxWidth(zoom);
                stack.setMaxHeight(zoom);
                Tile tile = maze.getTile(x, y);
                if (tile instanceof TileFloor) {
                    stack.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px");
                }
                if (tile instanceof TileWall) {
                    stack.setStyle("-fx-background-color: #282828; -fx-border-color: #d3d3d3; -fx-border-width: 1px");
                }
                if (tile instanceof TileDoor) {
                    stack.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px");
                    Image door = new Image("/com/santiagobenoit/audiogame/resources/images/door.png");
                    ImageView doorView = new ImageView(door);
                    stack.getChildren().add(doorView);
                }
                if (tile instanceof TileKey) {
                    stack.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px");
                    Image key = new Image("/com/santiagobenoit/audiogame/resources/images/key.png");
                    ImageView keyView = new ImageView(key);
                    stack.getChildren().add(keyView);
                }
                if (tile instanceof TileWater) {
                    stack.setStyle("-fx-background-color: #0000ff; -fx-border-color: #d3d3d3; -fx-border-width: 1px");
                }
                if (tile instanceof TileStart) {
                    stack.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px");
                    Text s = new Text("S");
                    stack.getChildren().add(s);
                }
                if (tile instanceof TileFinish) {
                    stack.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px");
                    Text f = new Text("F");
                    stack.getChildren().add(f);
                }
                stack.setOnMousePressed((MouseEvent event) -> {
                    Tile newTile = null;
                    switch (selectedTool) {
                        case "floor":
                            newTile = new TileFloor(fx, fy);
                            break;
                        case "wall":
                            newTile = new TileWall(fx, fy);
                            break;
                        case "start":
                            newTile = new TileStart(fx, fy);
                            break;
                        case "finish":
                            newTile = new TileFinish(fx, fy);
                            break;
                        case "door":
                            newTile = new TileDoor(fx, fy);
                            break;
                        case "key":
                            newTile = new TileKey(fx, fy);
                            break;
                        case "water":
                            newTile = new TileWater(fx, fy);
                            break;
                        default:
                            break;
                    }
                    if (newTile != null) {
                        maze.addTile(newTile);
                        newTile.initialize();
                    }
                    refreshMaze();
                });
                mazeGrid.add(stack, x, y);
            }
        }
    }
    
    public void playGame() {
        resetGame();
        playScene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case W:
                case UP:
                    if (maze.getTile(player.getFront().x, player.getFront().y) != null) {
                        maze.getTile(player.getFront().x, player.getFront().y).playerTouch();
                    } else if (hardcoreEnabled) {
                        player.kill();
                    }
                    if (!player.frontObstructed()) {
                        player.moveForwards();
                    }
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        
                    }
                    break;
                case S:
                case DOWN:
                    if (maze.getTile(player.getBack().x, player.getBack().y) != null) {
                        maze.getTile(player.getBack().x, player.getBack().y).playerTouch();
                    } else if (hardcoreEnabled) {
                        player.kill();
                    }
                    if (!player.backObstructed()) {
                        player.moveBackwards();
                    }
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        
                    }
                    break;
                case A:
                case LEFT:
                    try {
                        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/footstep.wav", -10.0f, 0.2f);
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        
                    }
                    player.rotateLeft();
                    break;
                case D:
                case RIGHT:
                    try {
                        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/footstep.wav", -10.0f, -0.2f);
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        
                    }
                    player.rotateRight();
                    break;
                case SPACE:
                    if (marbleCount != 0) {
                        player.rollMarble();
                        marbleCount--;
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            
                        }
                    }
                    break;
                case E:
                case ENTER:
                    maze.getTile(player.getFront().x, player.getFront().y).interact();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        
                    }
                    break;
                case BACK_SPACE:
                    gameover = true;
                    resetGame();
                    break;
                case ESCAPE:
                    gameover = true;
                    if (timer != null) {
                        timer.cancel();
                    }
                    if (warning != null) {
                        warning.cancel();
                    }
                    stage.setScene(titleScene);
                    stage.setWidth(300);
                    stage.setHeight(200);
                    stage.setMinWidth(300);
                    stage.setMinHeight(200);
                    stage.centerOnScreen();
                    break;
                default:
                    break;
            }
        });
    }
    
    public static void resetGame() {
        gameover = false;
        maze = new Maze(original);
        player = new Player();
        marbleCount = marbleLimit;
        keyCount = 0;
        maze.initializeTiles();
        player.setPosition(maze.getStart().getX(), maze.getStart().getY());
        if (timer != null) {
            timer.cancel();
        }
        if (warning != null) {
            warning.cancel();
        }
        if (timeLimit > 0) {
            if (timeLimit > 9000) {
                timeWarning = timeLimit - 9000;
            } else {
                timeWarning = 0;
            }
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/buzzer.wav", 0.0f, 0.0f);
                        gameover = true;
                        Thread.sleep(1500);
                        resetGame();
                    } catch (InterruptedException e) {
                        
                    }
                }
            }, timeLimit);
            warning = new Timer(true);
            warning.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 5; i++) {
                            Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/clock.wav", -10.0f, 0.0f);
                            Thread.sleep(1800);
                        }
                    } catch (InterruptedException e) {
                        
                    }
                }
            }, timeWarning);
        }
        try {
            Util.playSound("/com/santiagobenoit/audiogame/resources/sounds/warp.wav", 0.0f, 0.0f);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private File mazeDir, myMazeDir;
    public static Maze maze;
    private static Maze original;
    public static Player player;
    public static int marbleLimit, marbleCount, keyCount;
    public static long timeLimit, timeWarning;
    private Scene titleScene, setupScene, playScene, editorScene;
    private static int zoom;
    private String selectedTool;
    private GridPane mazeGrid;
    private SpinnerValueFactory widthVal, heightVal;
    private Stage stage;
    private static Timer timer, warning;
    private boolean ready;
    public static boolean gameover, victory, defeat, hardcoreEnabled;
    private static final String VERSION = "1.2";
}
