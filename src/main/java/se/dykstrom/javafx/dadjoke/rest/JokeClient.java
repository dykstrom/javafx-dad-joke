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

import java.io.IOException;
import java.util.Random;

import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.ObjectDataReader;
import com.gluonhq.connect.provider.RestClient;
import se.dykstrom.javafx.dadjoke.model.Joke;
import se.dykstrom.javafx.dadjoke.model.JokeList;

/**
 * A REST client for retrieving jokes. This class uses Gluon Connect (https://github.com/gluonhq/connect)
 * to make REST calls and return the result as an observable object with JavaFX properties that can be observed.
 */
public class JokeClient {

    private static final String HOST_ADDRESS = "https://icanhazdadjoke.com";

    private static final int CONNECT_TIMEOUT = 10_000;
    private static final int READ_TIMEOUT = 10_000;

    private final Random random = new Random();

    /**
     * Gets a random joke from a REST service. This method makes an asynchronous call to a REST endpoint,
     * and returns an observable object that will be initialized once the result comes back. When the object
     * has been initialized, the result can be retrieved by calling {@link GluonObservableObject#get()}.
     *
     * @return A {@link GluonObservableObject} that can be observed while waiting for the result.
     */
    public GluonObservableObject<Joke> getRandomJoke() {
        final var client = createBasicRestClient();
        return DataProvider.retrieveObject(client.createObjectDataReader(new JsonInputConverter<>(Joke.class)));
    }

    /**
     * Gets a random joke that contains the given search term from a REST service. See also {@link #getRandomJoke()}.
     *
     * @param searchTerm The search term to search for.
     * @return A {@link GluonObservableObject} that can be observed while waiting for the result.
     */
    public GluonObservableObject<Joke> getRandomJokeBySearchTerm(String searchTerm) {
        ObjectDataReader<Joke> reader = new ObjectDataReader<>() {
            @Override
            public GluonObservableObject<Joke> newGluonObservableObject() {
                return new GluonObservableObject<>();
            }

            @Override
            public Joke readObject() throws IOException {
                // Get the list that contains only the first joke
                final var getJokeList = createSearchRestClient(searchTerm, "1").createObjectDataReader(new JsonInputConverter<>(JokeList.class));
                final var listWithFirstJoke = getJokeList.readObject();

                // Find out how many jokes there are in total, and pick a random one
                final var totalNumberOfJokes = listWithFirstJoke.total_jokes();
                if (totalNumberOfJokes == 0) {
                    throw new IOException("No jokes about '" + searchTerm + "' found.");
                }
                final var index = Integer.toString(random.nextInt(totalNumberOfJokes) + 1);

                // Get the list that contains only the randomly chosen joke
                final var getRandomJoke = createSearchRestClient(searchTerm, index).createObjectDataReader(new JsonInputConverter<>(JokeList.class));
                final var listWithRandomJoke = getRandomJoke.readObject();

                // Extract the single joke that we know is in the list
                return listWithRandomJoke.results()[0];
            }
        };
        return DataProvider.retrieveObject(reader);
    }

    /**
     * Creates a basic REST client.
     */
    private RestClient createBasicRestClient() {
        return RestClient.create()
                .method("GET")
                .host(HOST_ADDRESS)
                .connectTimeout(CONNECT_TIMEOUT)
                .readTimeout(READ_TIMEOUT)
                .header("Accept", "application/json")
                .header("User-Agent", "javafx-dad-joke (https://github.com/dykstrom/javafx-dad-joke)");
    }

    /**
     * Creates a REST client for searching. This client searches with a limit of one (1),
     * which means there will always be one joke per page. Therefore, the parameter {@code index}
     * specifies which joke to find in the search result.
     *
     * @param searchTerm The term to search for.
     * @param index The 1-based index of the joke to find.
     * @return The configured REST client.
     */
    private RestClient createSearchRestClient(String searchTerm, String index) {
        return createBasicRestClient()
                .path("/search")
                .queryParam("term", searchTerm)
                .queryParam("page", index)
                .queryParam("limit", "1");
    }
}
