/*
 * Copyright 2020 Johan Dykström
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.dykstrom.javafx.dadjoke;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main application.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        final var fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        final var scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setScene(scene);
        stage.setTitle("icanhazdadjoke.com");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
