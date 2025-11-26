package com.example.tictactoe;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {
    private enum Player { X, O }

    private Player current = Player.X;
    private Player[][] board = new Player[3][3];
    private boolean gameOver = false;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(420, 480);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                StackPane cell = makeCell(r, c);
                grid.add(cell, c, r);
            }
        }

        Button restart = new Button("Restart");
        restart.setOnAction(e -> reset());

        root.setCenter(grid);
        root.setBottom(restart);
        BorderPane.setAlignment(restart, Pos.CENTER);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Tic-Tac-Toe (JavaFX)");
        stage.show();
    }

    private StackPane makeCell(int row, int col) {
        StackPane pane = new StackPane();
        pane.setPrefSize(120, 120);
        pane.setStyle("-fx-background-color: white; -fx-border-color: #333; -fx-border-width: 2;");

        Text mark = new Text();
        mark.setFont(Font.font(48));

        pane.setOnMouseClicked(e -> {
            if (gameOver) return;
            if (board[row][col] != null) return;

            board[row][col] = current;
            mark.setText(current == Player.X ? "X" : "O");
            mark.setFill(current == Player.X ? Color.DARKBLUE : Color.DARKRED);

            // animation
            ScaleTransition st = new ScaleTransition(Duration.millis(200), mark);
            st.setFromX(0.1);
            st.setFromY(0.1);
            st.setToX(1);
            st.setToY(1);
            st.play();

            checkWin();
            current = (current == Player.X) ? Player.O : Player.X;
        });

        pane.getChildren().add(mark);
        return pane;
    }

    private void reset() {
        this.board = new Player[3][3];
        current = Player.X;
        gameOver = false;
        // rebuild UI: close and reopen the stage
        Stage stage = (Stage) Stage.getWindows().filtered(w -> w.isShowing()).get(0);
        stage.close();
        start(new Stage());
    }

    private void checkWin() {
        Player winner = null;
        // rows
        for (int r = 0; r < 3; r++) {
            if (board[r][0] != null && board[r][0] == board[r][1] && board[r][1] == board[r][2]) {
                winner = board[r][0];
                highlightRow(r);
            }
        }
        // cols
        for (int c = 0; c < 3; c++) {
            if (board[0][c] != null && board[0][c] == board[1][c] && board[1][c] == board[2][c]) {
                winner = board[0][c];
                highlightCol(c);
            }
        }
        // diagonals
        if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winner = board[0][0];
            highlightDiag(true);
        }
        if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            winner = board[0][2];
            highlightDiag(false);
        }

        if (winner != null) {
            gameOver = true;
            showWinner(winner);
        } else if (isBoardFull()) {
            gameOver = true;
            showDraw();
        }
    }

    // Note: simple highlight implementations that draw a line across the window
    private void highlightRow(int r) {
        Line line = new Line(20, 120 + r * 130, 400, 120 + r * 130);
        line.setStroke(Color.GREEN);
        line.setStrokeWidth(6);
        FadeTransition ft = new FadeTransition(Duration.millis(800), line);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        Stage stage = (Stage) Stage.getWindows().filtered(w -> w.isShowing()).get(0);
        ((BorderPane) stage.getScene().getRoot()).getChildren().add(line);
    }

    private void highlightCol(int c) {
        Line line = new Line(80 + c * 130, 20, 80 + c * 130, 420);
        line.setStroke(Color.GREEN);
        line.setStrokeWidth(6);
        FadeTransition ft = new FadeTransition(Duration.millis(800), line);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        Stage stage = (Stage) Stage.getWindows().filtered(w -> w.isShowing()).get(0);
        ((BorderPane) stage.getScene().getRoot()).getChildren().add(line);
    }

    private void highlightDiag(boolean leftToRight) {
        Line line = leftToRight ? new Line(20, 20, 400, 400) : new Line(400, 20, 20, 400);
        line.setStroke(Color.GREEN);
        line.setStrokeWidth(6);
        FadeTransition ft = new FadeTransition(Duration.millis(800), line);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        Stage stage = (Stage) Stage.getWindows().filtered(w -> w.isShowing()).get(0);
        ((BorderPane) stage.getScene().getRoot()).getChildren().add(line);
    }

    private boolean isBoardFull() {
        for (int r = 0; r < 3; r++) for (int c = 0; c < 3; c++) if (board[r][c] == null) return false;
        return true;
    }

    private void showWinner(Player winner) {
        Stage stage = (Stage) Stage.getWindows().filtered(w -> w.isShowing()).get(0);
        Text txt = new Text((winner == Player.X ? "X" : "O") + " Wins!");
        txt.setFont(Font.font(36));
        txt.setFill(Color.GREEN);
        BorderPane root = (BorderPane) stage.getScene().getRoot();
        root.setTop(txt);
        BorderPane.setAlignment(txt, Pos.CENTER);

        FadeTransition ft = new FadeTransition(Duration.millis(1000), txt);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void showDraw() {
        Stage stage = (Stage) Stage.getWindows().filtered(w -> w.isShowing()).get(0);
        Text txt = new Text("Draw");
        txt.setFont(Font.font(36));
        txt.setFill(Color.ORANGE);
        BorderPane root = (BorderPane) stage.getScene().getRoot();
        root.setTop(txt);
        BorderPane.setAlignment(txt, Pos.CENTER);

        FadeTransition ft = new FadeTransition(Duration.millis(1000), txt);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public static void main(String[] args) {
        launch();
    }
}

