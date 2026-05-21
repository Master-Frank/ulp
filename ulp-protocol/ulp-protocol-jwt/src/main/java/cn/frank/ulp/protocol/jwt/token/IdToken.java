/*
 * ulp-protocol-jwt - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
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
package cn.frank.ulp.protocol.jwt.token;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

/**
 *
 * @author Frank Zhang
 */
@Data
@Builder
@Jacksonized
public class IdToken {

    @NonNull
    private final String  tokenValue;
    @NonNull
    private final Instant issuedAt;
    @NonNull
    private final Instant expiresAt;
}
