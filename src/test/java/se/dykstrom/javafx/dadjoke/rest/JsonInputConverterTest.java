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

import org.junit.jupiter.api.Test;
import se.dykstrom.javafx.dadjoke.model.Joke;

import java.io.ByteArrayInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonInputConverterTest {

    private static final String JOKE = "What did the 0 say to the 8? Nice belt!";
    private static final int STATUS = 200;

    private static final String JSON = "{\"id\":\"the-id\",\"joke\":\"" + JOKE + "\",\"status\":" + STATUS + "}";

    private final JsonInputConverter<Joke> converter = new JsonInputConverter<>(Joke.class);

    @Test
    void shouldConvertFromInputStream() {
        // Given
        converter.setInputStream(new ByteArrayInputStream(JSON.getBytes(UTF_8)));

        // When
        Joke joke = converter.read();

        // Then
        assertEquals(new Joke(JOKE, STATUS), joke);
    }
}
