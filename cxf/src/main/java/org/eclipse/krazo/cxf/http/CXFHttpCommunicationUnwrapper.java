/*
 * Copyright (c) 2014-2015 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2018-2020 Eclipse Krazo committers and contributors
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
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.krazo.cxf.http;

import org.eclipse.krazo.core.HttpCommunicationUnwrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * Unwrapper for CXF which extracts the original {@link HttpServletRequest} and / or {@link HttpServletResponse}
 * from the wrapper which are used e.g. by Liberty. This is required because the wrappers don't use the official wrapper base classes
 * and therefore Liberty fails to forward such requests because unwrapping isn't possible.
 */
public class CXFHttpCommunicationUnwrapper implements HttpCommunicationUnwrapper {

    @Override
    public boolean supports(final Object obj) {
        final String implName = obj.getClass().getName();
        return implName.equals("org.apache.cxf.jaxrs.impl.tl.ThreadLocalHttpServletRequest")
            || implName.equals("org.apache.cxf.jaxrs.impl.tl.ThreadLocalHttpServletResponse");
    }

    @Override
    public <T extends HttpServletResponse> T unwrapResponse(final T obj, final Class<T> type) {
        return unwrap(obj, type);
    }

    @Override
    public <T extends HttpServletRequest> T unwrapRequest(final T obj, final Class<T> type) {
        return unwrap(obj, type);
    }

    private <T> T unwrap(final T obj, final Class<T> type) {
        try {
            return type.cast(
                obj.getClass().getMethod("get").invoke(obj)
            );
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Failed to unwrap", e);
        }
    }
}
