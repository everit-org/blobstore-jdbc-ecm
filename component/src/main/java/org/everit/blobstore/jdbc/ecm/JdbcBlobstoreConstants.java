/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.blobstore.jdbc.ecm;

/**
 * Constants that make it possible to configure the JDBC Blobstore component programmatically.
 */
public final class JdbcBlobstoreConstants {

  public static final String ATTR_BLOB_ACCESS_MODE = "blobAccessMode.name";

  public static final String ATTR_BLOB_SELECTION_EXPRESSION_TARGET =
      "blobSelectionExpression.target";

  public static final String ATTR_DATASOURCE_TARGET = "dataSource.target";

  public static final String ATTR_EMPTY_BLOB_EXPRESSION_TARGET = "emptyBlobExpression.target";

  public static final String ATTR_FLAG = "flag.name";

  public static final String ATTR_POSITION = "position.name";

  public static final String ATTR_QUERYDSL_CONFIGURATION_TARGET = "querydslConfiguration.target";

  public static final String ATTR_UPDATE_SQL_AFTER_BLOB_CONTENT_MANIPULATION =
      "updateSQLAfterBlobContentManipulation.name";

  public static final String DEFAULT_SERVICE_DESCRIPTION = "Default Jdbc Blobstore Component";

  public static final String OPTION_BLOB_ACCESS_MODE_BYTES = "BYTES";

  public static final String OPTION_BLOB_ACCESS_MODE_STREAM = "STREAM";

  public static final String OPTION_POSITION_AFTER_FILTERS = "AFTER_FILTERS";

  public static final String OPTION_POSITION_AFTER_GROUP_BY = "AFTER_GROUP_BY";

  public static final String OPTION_POSITION_AFTER_HAVING = "AFTER_HAVING";

  public static final String OPTION_POSITION_AFTER_ORDER = "AFTER_ORDER";

  public static final String OPTION_POSITION_AFTER_PROJECTION = "AFTER_PROJECTION";

  public static final String OPTION_POSITION_AFTER_SELECT = "AFTER_SELECT";

  public static final String OPTION_POSITION_BEFORE_FILTERS = "BEFORE_FILTERS";

  public static final String OPTION_POSITION_BEFORE_GROUP_BY = "BEFORE_GROUP_BY";

  public static final String OPTION_POSITION_BEFORE_HAVING = "BEFORE_HAVING";

  public static final String OPTION_POSITION_BEFORE_ORDER = "BEFORE_ORDER";

  public static final String OPTION_POSITION_END = "END";

  public static final String OPTION_POSITION_START = "START";

  public static final String OPTION_POSITION_START_OVERRIDE = "START_OVERRIDE";

  public static final String OPTION_POSITION_WITH = "WITH";

  public static final String SERVICE_FACTORYPID_JDBC_BLOBSTORE =
      "org.everit.blobstore.jdbc.JdbcBlobstore";

  private JdbcBlobstoreConstants() {
  }

}
