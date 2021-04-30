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
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gluonhq.connect.converter.InputStreamInputConverter;

/**
 * An InputConverter that converts a JSON object read from an InputStream into an object. This converter
 * uses Gson to convert from JSON to object. See also {@link com.gluonhq.connect.converter.JsonInputConverter}.
 *
 * @param <T> The type of the object to convert the JSON Object into.
 */
public class JsonInputConverter<T> extends InputStreamInputConverter<T> {

    private static final Logger LOGGER = Logger.getLogger(JsonInputConverter.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<? extends T> clazz;

    public JsonInputConverter(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T read() {
        try (Reader reader = new InputStreamReader(getInputStream())) {
            return mapper.readValue(reader, clazz);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong while reading from input stream.", e);
            return null;
        }
    }
}
