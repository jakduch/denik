package com.example.knihapokus;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class FlipBookTest extends Application {

    private int currentPage = 0;
    private List<StackPane> pages = new ArrayList<>();

    private boolean holdingRight = false;
    private boolean holdingLeft = false;

    private Timeline holdAnimation;

    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();

        // 📚 vytvoření 20 stran
        for (int i = 1; i <= 20; i++) {
            StackPane page = createPage("Strana " + i + "\n\nToto je ukázkový text pro stránku číslo " + i + ".", i);
            pages.add(page);
        }

        // přidáme první dvě stránky
        root.getChildren().addAll(pages.get(1), pages.get(0));

        Scene scene = new Scene(root, 600, 500);

        // 🎬 animace držení
        holdAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1.2), e -> {

                    if (holdingRight) {
                        nextPage(root);
                    }

                    if (holdingLeft) {
                        previousPage(root);
                    }

                })
        );
        holdAnimation.setCycleCount(Animation.INDEFINITE);

        // ⌨️ ovládání
        scene.setOnKeyPressed(e -> {

            if (e.getCode() == KeyCode.RIGHT) {
                holdingRight = true;
                holdAnimation.play();
                nextPage(root);
            }

            if (e.getCode() == KeyCode.LEFT) {
                holdingLeft = true;
                holdAnimation.play();
                previousPage(root);
            }

        });

        scene.setOnKeyReleased(e -> {

            if (e.getCode() == KeyCode.RIGHT) {
                holdingRight = false;
            }

            if (e.getCode() == KeyCode.LEFT) {
                holdingLeft = false;
            }

            if (!holdingLeft && !holdingRight) {
                holdAnimation.stop();
            }

        });

        stage.setTitle("Flip Book - 20 stran");
        stage.setScene(scene);
        stage.show();

        root.requestFocus();
    }

    // 👉 další stránka
    private void nextPage(StackPane root) {
        if (currentPage >= pages.size() - 1) return;

        StackPane current = pages.get(currentPage);
        StackPane next = pages.get(currentPage + 1);

        Rotate rotate = new Rotate(0, 0, 200, 0, Rotate.Y_AXIS);
        current.getTransforms().clear();
        current.getTransforms().add(rotate);

        root.getChildren().setAll(next, current);

        Timeline flip = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(rotate.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(0.8),
                        new KeyValue(rotate.angleProperty(), -180))
        );

        flip.setOnFinished(e -> {
            currentPage++;
            root.getChildren().setAll(pages.get(currentPage));
        });

        flip.play();
    }

    // 👉 předchozí stránka
    private void previousPage(StackPane root) {
        if (currentPage <= 0) return;

        StackPane current = pages.get(currentPage);
        StackPane prev = pages.get(currentPage - 1);

        Rotate rotate = new Rotate(-180, 0, 200, 0, Rotate.Y_AXIS);
        prev.getTransforms().clear();
        prev.getTransforms().add(rotate);

        root.getChildren().setAll(current, prev);

        Timeline flip = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(rotate.angleProperty(), -180)),
                new KeyFrame(Duration.seconds(0.8),
                        new KeyValue(rotate.angleProperty(), 0))
        );

        flip.setOnFinished(e -> {
            currentPage--;
            root.getChildren().setAll(pages.get(currentPage));
        });

        flip.play();
    }

    // 🎨 vytvoření stránky
    private StackPane createPage(String text, int index) {
        Rectangle bg = new Rectangle(300, 400,
                Color.hsb(index * 18, 0.3, 1.0));
        bg.setStroke(Color.BLACK);

        // hlavní text
        Text content = new Text(text);
        content.setWrappingWidth(260);

        // číslo stránky
        Text pageNumber = new Text(String.valueOf(index));
        pageNumber.setStyle("-fx-font-size: 14px; -fx-fill: black;");

        // layout
        BorderPane layout = new BorderPane();
        layout.setCenter(content);

        StackPane bottom = new StackPane(pageNumber);
        bottom.setPrefHeight(40); // prostor dole
        layout.setBottom(bottom);

        return new StackPane(bg, layout);
    }

    public static void main(String[] args) {
        launch();
    }
}