/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sql.parser.api;

import com.google.common.cache.LoadingCache;
import org.apache.shardingsphere.sql.parser.core.ParseContext;
import org.apache.shardingsphere.sql.parser.core.database.cache.ParseTreeCacheBuilder;
import org.apache.shardingsphere.sql.parser.core.database.parser.SQLParserExecutor;

/**
 * SQL parser engine.
 */
public final class SQLParserEngine {
    
    private final SQLParserExecutor sqlParserExecutor;
    
    private final LoadingCache<String, ParseContext> parseTreeCache;
    
    public SQLParserEngine(final String databaseType, final boolean sqlCommentParseEnabled) {
        this(databaseType, new CacheOption(128, 1024L, 4), sqlCommentParseEnabled);
    }
    
    public SQLParserEngine(final String databaseType, final CacheOption cacheOption, final boolean sqlCommentParseEnabled) {
        sqlParserExecutor = new SQLParserExecutor(databaseType, sqlCommentParseEnabled);
        parseTreeCache = ParseTreeCacheBuilder.build(cacheOption, databaseType, sqlCommentParseEnabled);
    }
    
    /**
     * Parse SQL. 解析为抽象语法树
     *
     * @param sql SQL to be parsed
     * @param useCache whether use cache
     * @return parse tree
     */
    public ParseContext parse(final String sql, final boolean useCache) {
        return useCache ? parseTreeCache.getUnchecked(sql) : sqlParserExecutor.parse(sql);
    }
}
