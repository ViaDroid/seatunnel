/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.connectors.seatunnel.http.rabbitmq.config;

import org.apache.seatunnel.api.configuration.Option;
import org.apache.seatunnel.api.configuration.Options;

public class HttpSinkOptions extends HttpCommonOptions {
    public static final Option<Boolean> ARRAY_MODE =
            Options.key("array_mode")
                    .booleanType()
                    .defaultValue(false)
                    .withDescription(
                            "Send data as a JSON array when true, or as a single JSON object when false (default)");

    public static final Option<Integer> BATCH_SIZE =
            Options.key("batch_size")
                    .intType()
                    .defaultValue(1)
                    .withDescription(
                            "The batch size of records to send in one HTTP request. Only works when array_mode is true");

    public static final Option<Integer> REQUEST_INTERVAL_MS =
            Options.key("request_interval_ms")
                    .intType()
                    .defaultValue(0)
                    .withDescription("The interval milliseconds between two HTTP requests");

    public static final Option<String> ROUTING_KEY =
            Options.key("routing_key")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("The key route to the queue");

    public static final Option<String> USER =
            Options.key("user")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("The user of basic auth");

    public static final Option<String> PASSWORD =
            Options.key("password")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("The password of basic auth");

    public static final String AUTHORIZATION = "Authorization";
}
