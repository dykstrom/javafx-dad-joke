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

package se.dykstrom.javafx.dadjoke.rest;

import com.gluonhq.connect.GluonObservableObject;
import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.dykstrom.javafx.dadjoke.model.Joke;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JokeClientIT {

    private final JokeClient client = new JokeClient();

    @BeforeAll
    static void setUpClass() {
        Platform.startup(() -> { });
        Platform.setImplicitExit(false);
    }

    @AfterAll
    static void tearDownClass() {
        Platform.exit();
    }

    @Test
    void shouldGetRandomJoke() throws InterruptedException {
        // Given
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // When
        GluonObservableObject<Joke> observableJoke = client.getRandomJoke();
        observableJoke.setOnSucceeded(event -> countDownLatch.countDown());
        observableJoke.setOnFailed(event -> countDownLatch.countDown());
        observableJoke.addListener((observable, oldValue, newValue) -> System.out.println(Thread.currentThread().getName() + ": CHANGE: " + oldValue + " -> " + newValue));

        // Then
        assertTrue(countDownLatch.await(10, TimeUnit.SECONDS), "Timeout");
        assertTrue(observableJoke.isInitialized());
        assertNotNull(observableJoke.get());
    }

    @Test
    void shouldGetRandomJokeBySearchTerm() throws InterruptedException {
        // Given
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // When
        GluonObservableObject<Joke> observableJoke = client.getRandomJokeBySearchTerm("hipster");
        observableJoke.setOnSucceeded(event -> countDownLatch.countDown());
        observableJoke.setOnFailed(event -> countDownLatch.countDown());

        // Then
        assertTrue(countDownLatch.await(10, TimeUnit.SECONDS), "Timeout");
        assertTrue(observableJoke.isInitialized());
        assertNotNull(observableJoke.get());
    }
}
