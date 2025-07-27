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

package org.apache.seatunnel.connectors.seatunnel.http.rabbitmq.sink;

import org.apache.seatunnel.api.configuration.ReadonlyConfig;
import org.apache.seatunnel.api.sink.SinkWriter;
import org.apache.seatunnel.api.sink.SupportMultiTableSink;
import org.apache.seatunnel.api.table.catalog.CatalogTable;
import org.apache.seatunnel.api.table.type.SeaTunnelRow;
import org.apache.seatunnel.api.table.type.SeaTunnelRowType;
import org.apache.seatunnel.connectors.seatunnel.common.sink.AbstractSimpleSink;
import org.apache.seatunnel.connectors.seatunnel.http.rabbitmq.config.HttpConfig;
import org.apache.seatunnel.connectors.seatunnel.http.rabbitmq.config.HttpParameter;
import org.apache.seatunnel.connectors.seatunnel.http.rabbitmq.config.HttpSinkOptions;

import java.io.IOException;
import java.util.Optional;

import static org.apache.seatunnel.connectors.seatunnel.http.rabbitmq.util.AuthorizationUtil.getTokenByBasicAuth;

public class HttpSink extends AbstractSimpleSink<SeaTunnelRow, Void>
        implements SupportMultiTableSink {
    protected final HttpParameter httpParameter = new HttpParameter();
    protected CatalogTable catalogTable;
    protected SeaTunnelRowType seaTunnelRowType;
    protected ReadonlyConfig pluginConfig;

    public HttpSink(ReadonlyConfig pluginConfig, CatalogTable catalogTable) {
        this.pluginConfig = pluginConfig;
        httpParameter.setUrl(pluginConfig.get(HttpSinkOptions.URL));
        if (pluginConfig.getOptional(HttpSinkOptions.HEADERS).isPresent()) {
            httpParameter.setHeaders(pluginConfig.get(HttpSinkOptions.HEADERS));
        }
        if (pluginConfig.getOptional(HttpSinkOptions.PARAMS).isPresent()) {
            httpParameter.setHeaders(pluginConfig.get(HttpSinkOptions.PARAMS));
        }
        if (pluginConfig.getOptional(HttpSinkOptions.ARRAY_MODE).isPresent()) {
            httpParameter.setArrayMode(pluginConfig.get(HttpSinkOptions.ARRAY_MODE));
        }
        if (pluginConfig.getOptional(HttpSinkOptions.BATCH_SIZE).isPresent()) {
            httpParameter.setBatchSize(pluginConfig.get(HttpSinkOptions.BATCH_SIZE));
        }
        if (pluginConfig.getOptional(HttpSinkOptions.REQUEST_INTERVAL_MS).isPresent()) {
            httpParameter.setRequestIntervalMs(
                    pluginConfig.get(HttpSinkOptions.REQUEST_INTERVAL_MS));
        }
        if (pluginConfig.getOptional(HttpSinkOptions.ROUTING_KEY).isPresent()) {
            httpParameter.setRoutingKey(pluginConfig.get(HttpSinkOptions.ROUTING_KEY));
        }
        if (pluginConfig.getOptional(HttpSinkOptions.USER).isPresent()) {
            httpParameter.setUser(pluginConfig.get(HttpSinkOptions.USER));
        }
        if (pluginConfig.getOptional(HttpSinkOptions.PASSWORD).isPresent()) {
            httpParameter.setPassword(pluginConfig.get(HttpSinkOptions.PASSWORD));
        }
        this.catalogTable = catalogTable;
        this.seaTunnelRowType = catalogTable.getSeaTunnelRowType();

        String accessToken =
                getTokenByBasicAuth(httpParameter.getUser(), httpParameter.getPassword());
        httpParameter.getHeaders().put(HttpSinkOptions.AUTHORIZATION, accessToken + "==");
    }

    @Override
    public String getPluginName() {
        return HttpConfig.CONNECTOR_IDENTITY;
    }

    @Override
    public HttpSinkWriter createWriter(SinkWriter.Context context) throws IOException {
        return new HttpSinkWriter(seaTunnelRowType, httpParameter);
    }

    @Override
    public Optional<CatalogTable> getWriteCatalogTable() {
        return Optional.ofNullable(catalogTable);
    }
}
