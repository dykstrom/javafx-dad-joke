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

import com.gluonhq.connect.ConnectState;
import com.gluonhq.connect.GluonObservableObject;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import se.dykstrom.javafx.dadjoke.model.Joke;
import se.dykstrom.javafx.dadjoke.rest.JokeClient;

/**
 * Controller class for the main window.
 */
public class MainController {

    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    @FXML
    private ProgressBar progressBar;

    private final JokeClient jokeClient = new JokeClient();

    @FXML
    public void handleSearchAction() {
        String searchTerm = textField.getText().strip();
        GluonObservableObject<Joke> observableJoke;
        if (searchTerm.isBlank()) {
            observableJoke = jokeClient.getRandomJoke();
        } else {
            observableJoke = jokeClient.getRandomJokeBySearchTerm(searchTerm);
        }
        observableJoke.setOnFailed(event -> textArea.setText("Failed to get joke: " + event.getSource().getException().getMessage()));
        observableJoke.setOnSucceeded(event -> textArea.setText(observableJoke.get().joke()));
        progressBar.visibleProperty().bind(observableJoke.stateProperty().isEqualTo(ConnectState.RUNNING));
    }
}
