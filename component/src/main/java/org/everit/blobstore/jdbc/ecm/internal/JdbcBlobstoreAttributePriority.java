package org.everit.blobstore.jdbc.ecm.internal;

/**
 * Constants of JDBC BlobStore attribute priority.
 */
public final class JdbcBlobstoreAttributePriority {

  public static final int P01_SERVICE_DESCRIPTION = 1;

  public static final int P02_DATASOURCE = 2;

  public static final int P03_QUERYDSL_CONFIGURATION = 3;

  public static final int P04_BLOB_ACCESS_MODE = 4;

  public static final int P05_BLOB_READING_LOCK_QUERYFLAG_POSITION = 5;

  public static final int P06_BLOB_READING_LOCK_QUERYFLAG_EXPRESSION = 6;

  public static final int P07_UPDATE_SQL_AFTER_BLOB_CONTENT_MANIPULATION = 7;

  public static final int P08_EMPTY_BLOB_EXPRESSION_TARGET = 8;

  public static final int P09_BLOB_SELECTION_EXPRESSION_TARGET = 9;

  private JdbcBlobstoreAttributePriority() {
  }
}
