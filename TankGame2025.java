import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class TankGame2025 extends Application {
    /** The width of the game window. */
    private int WIDTH = 800;
    /** The height of the game window. */
    private int HEIGHT = 800;
    /** The animation timer for the game loop. */
    private AnimationTimer gameAnimationTimer;
    /** The root group for all game elements. */
    private Group gameRoot;
    /** A list to store enemy bullets. */
    private List<ImageView> enemyBullets = new ArrayList<>();
    /** The currently selected tank image for the player. */
    private String selectedTank = "whiteTank1";
    /** The last time a player bullet was fired (in nanoseconds). */
    private long time1 = 0;
    /** The minimum time difference between player bullets (in nanoseconds). */
    private final long deltaTime = 600_000_000;
    /** The primary stage for the game. */
    private Stage stage;
    /** The scenes for different game states: menu, game, pause, and game over. */
    private Scene menuScene, gameScene, pauseScene, gameOverScene;
    /** The movement speed of the player's tank. */
    private int playerSpeed = 2;
    /** The current direction of the player's tank. */
    private String playerDirection = "RIGHT";
    /** The ImageView representing the player's tank. */
    private ImageView userTank;
    /** A list to store player bullets. */
    private List<ImageView> bullets = new ArrayList<>();
    /** A list to store wall objects. */
    private List<ImageView> walls = new ArrayList<>();
    /** A list to store enemy tank objects. */
    private List<ImageView> enemies = new ArrayList<>();
    /** A list of image names for enemy tanks. */
    private List<String> enemyTankImages = new ArrayList<>();
    /** Flag to indicate if the game is paused. */
    private boolean paused = false;
    /** Flag to indicate if the game is over. */
    private boolean gameOver = false;
    /** First image for player tank animation. */
    private Image tankImage;
    /** Second image for player tank animation. */
    private Image tankImage2;
    /** A set to track currently pressed keyboard keys. */
    private final Set<KeyCode> keyBoard = new HashSet<>();
    /** The player's current score. */
    private int score = 0;
    /** The player's remaining lives. */
    private int lives = 3;
    /** Text element to display the current score. */
    private Text scoreText = new Text("Score: " + score);
    /** Text element to display the remaining lives. */
    private Text livesText = new Text("Lives: " + lives);
    /** Frame index for player tank animation. */
    private int frameIndex = 0;
    /** Timeline for player tank walking animation. */
    private Timeline walkAnimation;


    /**
     * Default constructor for the TankGame2025 class.
     */
    public TankGame2025() {
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        mainMenu();
        PauseMenu();
    }

    /**
     * Initializes and displays the main menu scene.
     * Allows the player to choose a tank and start the game.
     */
    public void mainMenu() {
        Group menuRoot = new Group();
        Text title = new Text("TankGame");
        title.setFill(Color.RED);
        title.setFont(Font.font(30));
        Button wht1 = new Button("White Tank");
        Button ylw1 = new Button("Yellow Tank");
        Button start = new Button("Start Game");

        // Set action for White Tank selection
        wht1.setOnAction(e -> selectedTank = "whiteTank1");
        wht1.setStyle("-fx-background-color: white;");

        // Set action for Yellow Tank selection
        ylw1.setOnAction(e -> selectedTank = "yellowTank1");
        enemyTankImages.add("yellowTank1.png");
        enemyTankImages.add("whiteTank1.png");
        ylw1.setStyle("-fx-background-color: yellow;");

        // Set action for Start Game button
        start.setOnAction(e -> gameEvent());
        start.setStyle("-fx-background-color: green;");

        VBox box = new VBox(10, title, wht1, ylw1, start);
        box.setAlignment(Pos.CENTER);
        box.setLayoutX(WIDTH / 2 - 75);
        box.setLayoutY(HEIGHT / 2 - 150);

        menuRoot.getChildren().add(box);
        menuScene = new Scene(menuRoot, 800, 800, Color.BLACK);
        stage.setScene(menuScene);
        stage.show();
    }

    /**
     * Initializes and displays the game over menu scene.
     * Allows the player to restart the game or exit.
     */
    private void gameOverMenu() {
        gameOver = true;
        Group gameOverRoot = new Group();
        Text overText = new Text("GAME OVER\nScore: " + score + "\nPress R to Restart or ESC to Exit");
        overText.setFill(Color.RED);
        overText.setFont(Font.font(30));
        overText.setTranslateX(150);
        overText.setTranslateY(400);
        gameOverRoot.getChildren().add(overText);
        gameOverScene = new Scene(gameOverRoot, WIDTH, HEIGHT, Color.BLACK);

        // Set key event handlers for restart and exit
        gameOverScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.R) {
                reStart();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });
        stage.setScene(gameOverScene);
    }

    /**
     * Sets up and starts the main game event loop.
     * Initializes player tank, score/lives display, wall creation, and enemy spawning.
     */
    private void gameEvent() {
        gameRoot = new Group();
        gameScene = new Scene(gameRoot, 800, 800, Color.BLACK);
        userTank = new ImageView(new Image("assets/" + selectedTank + ".png"));
        userTank.setX(100);
        userTank.setY(700);
        gameRoot.getChildren().add(userTank);
        scoreText.setFill(Color.RED);
        livesText.setFill(Color.RED);
        scoreText.setTranslateX(20);
        scoreText.setTranslateY(30);
        livesText.setTranslateX(20);
        livesText.setTranslateY(50);
        gameRoot.getChildren().addAll(scoreText, livesText);

        // Set key pressed event for player movement and pausing
        gameScene.setOnKeyPressed(event -> {
            keyBoard.add(event.getCode());
            if (event.getCode() == KeyCode.P && !paused) {
                paused = true;
                stage.setScene(pauseScene);
            }
        });

        // Set key released event to remove keys from the set
        gameScene.setOnKeyReleased(event -> {
            keyBoard.remove(event.getCode());
        });
        spawnEnemies();
        createWalls();
        // Starts the continuous spawning of enemies

        // Set tank images for animation based on selected tank
        if (selectedTank.equals("whiteTank1")) {
            tankImage = new Image("assets/whiteTank1.png");
            tankImage2 = new Image("assets/whiteTank2.png");
        } else if (selectedTank.equals("yellowTank1")) {
            tankImage = new Image("assets/yellowTank1.png");
            tankImage2 = new Image("assets/yellowTank2.png");
        }
        /**
         * The game's main animation timer.
         * Handles player movement, bullet movement, enemy movement, and enemy bullet movement.
         */
        gameAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (paused || gameOver) return; // Stop game logic if paused or game over

                if (!paused && gameRoot.getChildren().contains(userTank)) {
                    double tankX = userTank.getX();
                    double tankY = userTank.getY();

                    // Player movement based on pressed keys
                    if (keyBoard.contains(KeyCode.UP)) {
                        moveAnimation();
                        moveTank(0, -playerSpeed);
                        playerDirection = "UP";
                        userTank.setRotate(270);
                        walkAnimation.playFromStart();
                    }
                    if (keyBoard.contains(KeyCode.DOWN)) {
                        moveAnimation();
                        moveTank(0, playerSpeed);
                        playerDirection = "DOWN";
                        userTank.setRotate(90);
                        walkAnimation.playFromStart();
                    }
                    if (keyBoard.contains(KeyCode.LEFT)) {
                        moveAnimation();
                        moveTank(-playerSpeed, 0);
                        playerDirection = "LEFT";
                        userTank.setRotate(180);
                        walkAnimation.playFromStart();
                    }
                    if (keyBoard.contains(KeyCode.RIGHT)) {
                        moveAnimation();
                        moveTank(playerSpeed, 0);
                        playerDirection = "RIGHT";
                        userTank.setRotate(0);
                        walkAnimation.playFromStart();
                    }
                    // Player firing logic
                    if (keyBoard.contains(KeyCode.X)) {
                        if (now - time1 >= deltaTime) { // Check for firing cooldown
                            if (playerDirection.equals("UP")) {
                                fire(tankX + 10, tankY, "UP");
                                time1 = now;
                            } else if (playerDirection.equals("DOWN")) {
                                fire(tankX + 10, tankY + 20, "DOWN");
                                time1 = now;
                            } else if (playerDirection.equals("LEFT")) {
                                fire(tankX, tankY + 10, "LEFT");
                                time1 = now;
                            } else if (playerDirection.equals("RIGHT")) {
                                fire(tankX + 10, tankY + 10, "RIGHT");
                                time1 = now;
                            }
                        }
                    }
                }
                // Update game elements
                moveBullet();
                moveEnemies();
                moveEnemyBullets();
            }
        };
        gameAnimationTimer.start(); // Start the game loop
        stage.setScene(gameScene);
        stage.show();
    }

    /**
     * Initializes and displays the pause menu scene.
     * Allows the player to unpause, restart, or exit the game.
     */
    private void PauseMenu() {
        Group pauseRoot = new Group();
        Text text = new Text("Press P to resume game\n" +
                "Press R to restart game\n" +
                "Press ESC to exit game");
        text.setFill(Color.BLUE);
        text.setFont(Font.font(30));
        text.setTranslateX(150);
        text.setTranslateY(400);
        pauseScene = new Scene(pauseRoot, 800, 800, Color.WHITESMOKE);

        // Set key event handlers for pausing, exiting, and restarting
        pauseScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.P) {
                paused = false;
                stage.setScene(gameScene);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            } else if (e.getCode() == KeyCode.R) {
                reStart();
            }
        });

        pauseRoot.getChildren().add(text);
    }

    /**
     * Creates and adds wall objects to the game root.
     * Walls are strategically placed to form a maze-like structure and borders.
     */
    private void createWalls() {
        int wallWidth = 16;
        int wallHeight = 14;

        for (int y = 300; y < 700; y += wallHeight) {
            ImageView wall = new ImageView(new Image("assets/wall.png"));
            wall.setFitWidth(wallWidth);
            wall.setFitHeight(wallHeight);
            wall.setY(y);
            wall.setX(200);
            walls.add(wall);
            gameRoot.getChildren().add(wall);
        }
        for (int y = 300; y < 700; y += wallHeight) {
            ImageView wall = new ImageView(new Image("assets/wall.png"));
            wall.setFitWidth(wallWidth);
            wall.setFitHeight(wallHeight);
            wall.setY(y);
            wall.setX(600);
            walls.add(wall);
            gameRoot.getChildren().add(wall);
        }
        for (int x = 200; x < 600; x += wallWidth) {
            ImageView wall = new ImageView(new Image("assets/wall.png"));
            wall.setFitWidth(wallWidth);
            wall.setFitHeight(wallHeight);
            wall.setX(x);
            wall.setY(200);
            walls.add(wall);
            gameRoot.getChildren().add(wall);
        }
        for (int x = 0; x < WIDTH; x += wallWidth) {
            ImageView wall = new ImageView(new Image("assets/wall.png"));
            wall.setFitWidth(wallWidth);
            wall.setFitHeight(wallHeight);
            wall.setX(x);
            wall.setY(0);
            walls.add(wall);
            gameRoot.getChildren().add(wall);
        }
        for (int x = 0; x < WIDTH; x += wallWidth) {
            ImageView wall = new ImageView(new Image("assets/wall.png"));
            wall.setFitWidth(wallWidth);
            wall.setFitHeight(wallHeight);
            wall.setX(x);
            wall.setY(HEIGHT - wallHeight);
            walls.add(wall);
            gameRoot.getChildren().add(wall);
        }
        for (int y = 0; y < HEIGHT; y += wallHeight) {
            ImageView wall = new ImageView(new Image("assets/wall.png"));
            wall.setFitWidth(wallWidth);
            wall.setFitHeight(wallHeight);
            wall.setX(0);
            wall.setY(y);
            walls.add(wall);
            gameRoot.getChildren().add(wall);
        }

        for (int y = 0; y < HEIGHT; y += wallHeight) {
            ImageView wall = new ImageView(new Image("assets/wall.png"));
            wall.setFitWidth(wallWidth);
            wall.setFitHeight(wallHeight);
            wall.setX(WIDTH - wallWidth);
            wall.setY(y);
            walls.add(wall);
            gameRoot.getChildren().add(wall);
        }
    }


    /**
     * Checks if a wall exists at the given coordinates.
     *
     * @return true if a wall intersects the given bounds, false otherwise.
     */
    private boolean isWallAt(double x, double y) {
        for (ImageView wall : walls) {
            // Using a fixed size (32,32) for the check, assuming tank size
            if (wall.getBoundsInParent().intersects(x, y, 32, 32)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Spawns a single enemy tank at a random valid location (not on a wall).
     * The enemy's initial direction is randomized, and it begins random firing.
     */
    private void spawnEnemy() {
        double x = Math.random() * (WIDTH - 32); // Ensure enemy spawns within bounds
        double y = Math.random() * (HEIGHT / 2 - 32); // Spawn in the upper half of the screen
        if (!isWallAt(x, y)) { // Only spawn if a valid location is found
            Random r = new Random();
            String imageFileName = enemyTankImages.get(r.nextInt(enemyTankImages.size())); // Randomly select enemy tank type
            ImageView enemy = new ImageView(new Image("assets/" + imageFileName));
            // Store direction, animation images, and step counter as UserData
            enemy.setUserData(new Object[]{new Random().nextInt(4), 0});
            enemy.setX(x);
            enemy.setY(y);
            randomFire(enemy); // Start enemy's firing behavior

            gameRoot.getChildren().add(enemy);
            enemies.add(enemy);
        }
    }
    /**
     * Continuously spawns new enemies at random intervals.
     * Ensures that the number of enemies on screen does not exceed a limit.
     */
    private void spawnEnemies() {
        Random random = new Random();

        // Random time interval [1,3] 1 ile 3 second
        int time = random.nextInt(3) + 1;

        PauseTransition pause = new PauseTransition(Duration.seconds(time));
        pause.setOnFinished(event -> {
            if (enemies.size() < 8) {
                spawnEnemy();
            }
            spawnEnemies();
        });
        pause.play();
    }


    /**
     * Sets up the move animation for the player's tank.
     * This animation cycles between two tank images to give a walking effect.
     */
    private void moveAnimation() {
        if (userTank != null && gameRoot.getChildren().contains(userTank)) {
            walkAnimation = new Timeline(
                    new KeyFrame(Duration.millis(5), e -> {
                        if (userTank != null && gameRoot.getChildren().contains(userTank)) {
                            if (frameIndex % 2 == 0) {
                                userTank.setImage(tankImage);
                            } else {
                                userTank.setImage(tankImage2);
                            }
                            frameIndex++;
                        }
                    })
            );
            walkAnimation.setCycleCount(2); // Play the animation twice (e.g., tankImage -> tankImage2 -> tankImage)
        }
    }

    /**
     * Moves the player's tank by the given delta x and delta y,
     * checking for collisions with walls.
     *
     * @param x The amount to move horizontally.
     * @param y The amount to move vertically.
     */
    private void moveTank(int x, int y) {
        double newX = userTank.getX() + x;
        double newY = userTank.getY() + y;

        // Only move if the new position does not collide with a wall
        if (!isWallAt(newX, newY)) {
            userTank.setX(newX);
            userTank.setY(newY);
        }
    }

    /**
     * Moves all enemy tanks, handles their collision with walls and the player.
     * Enemies move randomly and change direction upon hitting a wall.
     * If an enemy collides with the player, both are "destroyed" and the player loses a life.
     */
    private void moveEnemies() {
        Iterator<ImageView> enemyIt = enemies.iterator();
        while (enemyIt.hasNext()) {
            ImageView enemy = enemyIt.next();

            // Check for collision between enemy and player tank
            if (gameRoot.getChildren().contains(userTank)) {
                if (enemy.getBoundsInParent().intersects(userTank.getBoundsInParent())) {
                    // Explosion effect for enemy
                    ImageView bigExploration = new ImageView(new Image("assets/explosion.png"));
                    bigExploration.setX(enemy.getX());
                    bigExploration.setY(enemy.getY());
                    gameRoot.getChildren().add(bigExploration);
                    PauseTransition explosion = new PauseTransition(Duration.seconds(1));
                    explosion.setOnFinished(event -> {
                        gameRoot.getChildren().remove(bigExploration);
                    });
                    explosion.play();

                    gameRoot.getChildren().remove(enemy); // Remove enemy
                    enemyIt.remove(); // Remove enemy from list
                    score += 100; // Increase score
                    updateScore();

                    // Explosion effect for player
                    ImageView bigExplosion = new ImageView(new Image("assets/explosion.png"));
                    bigExplosion.setX(userTank.getX());
                    bigExplosion.setY(userTank.getY());
                    gameRoot.getChildren().add(bigExplosion);
                    PauseTransition explosion2 = new PauseTransition(Duration.seconds(1));
                    explosion2.setOnFinished(event -> {
                        gameRoot.getChildren().remove(bigExplosion);
                    });
                    explosion2.play();

                    gameRoot.getChildren().remove(userTank); // Remove player tank
                    userTank = null; // Set player tank to null to indicate it's destroyed



                    // Respawn player after a delay
                    PauseTransition respawn = new PauseTransition(Duration.seconds(3));
                    respawn.setOnFinished(event -> {
                        if (!gameRoot.getChildren().contains(userTank)) { // Check if userTank was not recreated yet
                            userTank = new ImageView(new Image("assets/" + selectedTank + ".png"));
                            userTank.setX(100);
                            userTank.setY(700);
                            gameRoot.getChildren().add(userTank);
                        }
                    });
                    respawn.play();

                    lives--; // Decrease lives
                    updateLives();
                    if (lives <= 0) {
                        gameOverMenu(); // End game if no lives left
                    }
                    break; // Exit inner loop as player tank is destroyed
                }
            }

            // Enemy movement logic
            Object[] data = (Object[]) enemy.getUserData();
            int stepCounter = (int) data[1];
            stepCounter++;
            if (stepCounter > 240) { // Change direction periodically
                data[0] = new Random().nextInt(4); // Random new direction
                stepCounter = 0;
            }
            data[1] = stepCounter;
            int direction = (int) data[0]; // Current direction of enemy

            double dx = 0, dy = 0;
            if (direction == 0) {
                dy = -1;
                enemy.setRotate(270);
            } else if (direction == 1) {
                dx = 1;
                enemy.setRotate(0);
            } else if (direction == 2) {
                dy = 1;
                enemy.setRotate(90);
            } else if (direction == 3) {
                dx = -1;
                enemy.setRotate(180);
            }

            double newX = enemy.getX() + dx;
            double newY = enemy.getY() + dy;

            // Collision detection with walls for enemy
            boolean hitWall = false;
            for (ImageView wall : walls) {
                // Temporarily set enemy position to check for collision
                enemy.setX(newX);
                enemy.setY(newY);
                if (enemy.getBoundsInParent().intersects(wall.getBoundsInParent())) {
                    hitWall = true;
                    break;
                }
            }
            // Revert enemy position before collision check
            enemy.setX(enemy.getX() - dx);
            enemy.setY(enemy.getY() - dy);

            // Check if enemy goes out of bounds
            boolean outOfBounds = newX < 0 || newX + enemy.getFitWidth() > WIDTH || newY < 0 || newY + enemy.getFitHeight() > HEIGHT;

            if (outOfBounds || hitWall) {
                data[0] = new Random().nextInt(4); // Change direction if out of bounds or hit wall
            } else {
                enemy.setX(newX); // Apply movement if no collision
                enemy.setY(newY);
            }
        }
    }


    /**
     * Restarts the game by clearing all game elements, resetting score and lives,
     * and re-initializing the game state.
     */
    public void reStart() {
        enemies.clear();
        bullets.clear();
        enemyBullets.clear();
        keyBoard.clear();
        score = 0;
        lives = 3; // Reset lives to initial value (typo in code, was 5 initially)
        gameOver = false;
        paused = false;
        gameRoot.getChildren().clear(); // Clear all nodes from the game root
        scoreText = new Text("Score: " + score); // Recreate text nodes to add them back
        livesText = new Text("Lives: " + lives);
        playerDirection = "RIGHT"; // Reset player direction
        if (gameAnimationTimer != null) {
            gameAnimationTimer.stop(); // Stop the current game loop
        }
        gameEvent();
        // Start a new game event
    }
    /**
     * Creates and adds a player bullet to the game.
     *
     * @param direction The direction the bullet will travel ("UP", "DOWN", "LEFT", "RIGHT").
     */
    private void fire(double x, double y, String direction) {
        if (gameRoot.getChildren().contains(userTank)) { // Only fire if player tank exists
            ImageView bullet = new ImageView(new Image("assets/bullet.png"));
            bullet.setX(x);
            bullet.setY(y);
            bullet.setUserData(direction); // Store direction as user data
            bullets.add(bullet);
            gameRoot.getChildren().add(bullet);
        }
    }
    /**
     * Moves all player bullets and handles their collisions with enemies and walls.
     * If a bullet hits an enemy, both are removed, and the score increases.
     * If a bullet hits a wall, the bullet is removed and a small explosion is shown.
     * Bullets are removed if they go out of bounds.
     */
    private void moveBullet() {
        Iterator<ImageView> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            boolean bulletRemoved = false;
            ImageView bullet = iterator.next();
            String direction = (String) bullet.getUserData();

            // Move bullet based on its direction
            if (direction.equals("RIGHT")) {
                bullet.setX(bullet.getX() + 5);
            } else if (direction.equals("LEFT")) {
                bullet.setX(bullet.getX() - 5);
                bullet.setRotate(180);
            } else if (direction.equals("UP")) {
                bullet.setY(bullet.getY() - 5);
                bullet.setRotate(270);
            } else if (direction.equals("DOWN")) {
                bullet.setY(bullet.getY() + 5);
                bullet.setRotate(90);
            }

            // Check for collision with enemies
            Iterator<ImageView> enemyIt = enemies.iterator();
            while (enemyIt.hasNext()) {
                ImageView enemy = enemyIt.next();
                if (bullet.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    ImageView bigExploration = new ImageView(new Image("assets/explosion.png"));
                    gameRoot.getChildren().remove(bullet); // Remove bullet
                    bigExploration.setX(enemy.getX());
                    bigExploration.setY(enemy.getY());
                    gameRoot.getChildren().add(bigExploration); // Add explosion effect
                    PauseTransition explosion = new PauseTransition(Duration.seconds(1));
                    explosion.setOnFinished(event -> {
                        gameRoot.getChildren().remove(bigExploration); // Remove explosion after delay
                    });
                    explosion.play();

                    gameRoot.getChildren().remove(enemy); // Remove enemy
                    iterator.remove(); // Remove bullet from list
                    enemyIt.remove(); // Remove enemy from list
                    bulletRemoved = true; // Mark bullet as removed

                    score += 100; // Increase score
                    updateScore();
                    break; // Exit inner loop as bullet is removed
                }
            }

            // If bullet was not removed by hitting an enemy, check for wall collisions
            if (!bulletRemoved) {
                for (ImageView wall : walls) {
                    if (bullet.getBoundsInParent().intersects(wall.getBoundsInParent())) {
                        ImageView smallExplosion = new ImageView(new Image("assets/smallExplosion.png"));
                        smallExplosion.setX(wall.getX());
                        smallExplosion.setY(wall.getY());
                        gameRoot.getChildren().add(smallExplosion); // Add small explosion
                        gameRoot.getChildren().remove(bullet); // Remove bullet
                        iterator.remove(); // Remove bullet from list
                        PauseTransition deleteExplosion = new PauseTransition(Duration.seconds(1));
                        deleteExplosion.setOnFinished(event -> {
                            gameRoot.getChildren().remove(smallExplosion); // Remove explosion after delay
                        });
                        deleteExplosion.play();
                        bulletRemoved = true; // Mark bullet as removed
                        break;
                    }
                }
            }

            // Remove bullet if it goes out of bounds
            if (!bulletRemoved && (bullet.getX() < 0 || bullet.getY() < 0 || bullet.getX() > 800 || bullet.getY() > 800)) {
                gameRoot.getChildren().remove(bullet);
                iterator.remove();
            }
        }
    }
    /**
     * Creates and adds an enemy bullet to the game.
     * The bullet's direction is determined by the enemy's current direction.
     * @param direction The numerical direction of the enemy (0=UP, 1=RIGHT, 2=DOWN, 3=LEFT).
     */
    private void enemyFire(ImageView enemy, int direction) {
        ImageView bullet = new ImageView(new Image("assets/bullet.png"));
        bullet.setX(enemy.getX() + 10); // Offset to be more centered on tank
        bullet.setY(enemy.getY() + 10); // Offset to be more centered on tank
        String direction2 = ""; // String representation of direction

        // Convert numerical direction to string direction
        if (direction == 0) {
            direction2 = "UP";
        } else if (direction == 1) {
            direction2 = "RIGHT";
        } else if (direction == 2) {
            direction2 = "DOWN";
        } else if (direction == 3) {
            direction2 = "LEFT";
        }
        bullet.setUserData(direction2); // Store string direction as user data
        enemyBullets.add(bullet);
        gameRoot.getChildren().add(bullet);
    }

    /**
     * Initiates random firing for a given enemy tank.
     * After a random delay, the enemy fires a bullet and then schedules itself to fire again.
     */
    private void randomFire(ImageView enemy) {
        int randomtime = (int) (Math.random() * 1500) + 500; // Random delay between 0.5 and 2 seconds

        Timeline fireTimer = new Timeline(new KeyFrame(Duration.millis(randomtime), e -> {
            if (enemies.contains(enemy)) { // Only fire if the enemy still exists
                Object[] data = (Object[]) enemy.getUserData();
                int direction = (int) data[0]; // Get current enemy direction
                enemyFire(enemy, direction); // Fire a bullet
                randomFire(enemy); // Schedule next random fire
            }
        }));
        fireTimer.setCycleCount(1); // Play only once
        fireTimer.play();
    }
    /**
     * Moves all enemy bullets and handles their collisions with walls and the player.
     * If an enemy bullet hits a wall, the bullet is removed and a small explosion is shown.
     * If an enemy bullet hits the player, the player loses a life and an explosion is shown.
     * Bullets are removed if they go out of bounds.
     */
    private void moveEnemyBullets() {
        Iterator<ImageView> it = enemyBullets.iterator();
        while (it.hasNext()) {
            ImageView bullet = it.next();
            String direction = (String) bullet.getUserData();

            // Move bullet based on its direction
            if ("UP".equals(direction)) {
                bullet.setY(bullet.getY() - 2);
                bullet.setRotate(270);
            } else if ("DOWN".equals(direction)) {
                bullet.setY(bullet.getY() + 2);
                bullet.setRotate(90);
            } else if ("LEFT".equals(direction)) {
                bullet.setX(bullet.getX() - 2);
                bullet.setRotate(180);
            } else if ("RIGHT".equals(direction)) {
                bullet.setX(bullet.getX() + 2);
            }

            // Check for collision with walls
            boolean collisionWall = false;
            for (ImageView wall : walls) {
                if (bullet.getBoundsInParent().intersects(wall.getBoundsInParent())) {
                    ImageView smallExplosion2 = new ImageView(new Image("assets/smallExplosion.png"));
                    smallExplosion2.setX(wall.getX());
                    smallExplosion2.setY(wall.getY());
                    gameRoot.getChildren().add(smallExplosion2); // Add small explosion
                    gameRoot.getChildren().remove(bullet); // Remove bullet
                    it.remove(); // Remove bullet from list
                    PauseTransition deleteExplosion = new PauseTransition(Duration.seconds(1));
                    deleteExplosion.setOnFinished(event -> {
                        gameRoot.getChildren().remove(smallExplosion2); // Remove explosion after delay
                    });
                    deleteExplosion.play();
                    collisionWall = true;
                    break;
                }
            }

            // If bullet went out of bounds, skip to next bullet (it will be removed eventually)
            if (bullet.getX() < 0 || bullet.getY() < 0 || bullet.getX() > WIDTH || bullet.getY() > HEIGHT) {
                if (!collisionWall) { // Only remove if not already removed by wall collision
                    gameRoot.getChildren().remove(bullet);
                    it.remove();
                }
                continue;
            }

            // Check for collision with player tank
            if (gameRoot.getChildren().contains(userTank) && !collisionWall) { // Check if userTank exists and bullet didn't hit a wall
                if (bullet.getBoundsInParent().intersects(userTank.getBoundsInParent())) {
                    it.remove(); // Remove bullet from list
                    ImageView bigExploration = new ImageView(new Image("assets/explosion.png"));
                    gameRoot.getChildren().remove(bullet); // Remove bullet from scene
                    if (walkAnimation != null) {
                        walkAnimation.stop(); // Stop player walk animation
                    }
                    bigExploration.setX(userTank.getX());
                    bigExploration.setY(userTank.getY());
                    gameRoot.getChildren().remove(userTank); // Remove player tank
                    userTank = null; // Mark player tank as destroyed

                    gameRoot.getChildren().add(bigExploration); // Add explosion effect
                    PauseTransition explosion = new PauseTransition(Duration.seconds(1));
                    explosion.setOnFinished(event -> {
                        gameRoot.getChildren().remove(bigExploration); // Remove explosion after delay
                    });
                    explosion.play();

                    // Respawn player after a delay
                    PauseTransition respawn = new PauseTransition(Duration.seconds(3));
                    respawn.setOnFinished(event -> {
                        if (!gameRoot.getChildren().contains(userTank)) { // Check if userTank was not recreated yet
                            userTank = new ImageView(new Image("assets/" + selectedTank + ".png"));
                            userTank.setX(100);
                            userTank.setY(700);
                            gameRoot.getChildren().add(userTank);
                        }
                    });
                    respawn.play();
                    lives--; // Decrease lives
                    updateLives();
                    if (lives <= 0) {
                        gameOverMenu(); // End game if no lives left
                    }
                }
            }
        }
    }
    /**
     * Updates the score display on the screen.
     */
    private void updateScore() {
        scoreText.setText("Score: " + score);
    }

    /**
     * Updates the lives display on the screen.
     */
    private void updateLives() {
        livesText.setText("Lives: " + lives);
    }
}