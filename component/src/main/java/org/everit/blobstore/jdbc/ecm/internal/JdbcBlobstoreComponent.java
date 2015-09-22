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
package org.everit.blobstore.jdbc.ecm.internal;

import java.sql.Blob;

import javax.sql.DataSource;

import org.everit.blobstore.BlobAccessor;
import org.everit.blobstore.BlobReader;
import org.everit.blobstore.Blobstore;
import org.everit.blobstore.jdbc.BlobAccessMode;
import org.everit.blobstore.jdbc.JdbcBlobstore;
import org.everit.blobstore.jdbc.JdbcBlobstoreConfiguration;
import org.everit.blobstore.jdbc.ecm.JdbcBlobstoreConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.BooleanAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributeOption;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ConfigurationException;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.osgi.framework.Constants;

import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.types.Expression;
import com.querydsl.sql.Configuration;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * ECM component for {@link Blobstore} interface based on {@link JdbcBlobstore}.
 */
@Component(componentId = JdbcBlobstoreConstants.SERVICE_FACTORYPID_JDBC_BLOBSTORE,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Jdbc Blobstore (EverIT)",
    description = "Implements a org.everit.blobstore.Blobstore as an ECM component. "
        + "Registers an org.everit.blobstore.Blobstore OSGi Service.")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = JdbcBlobstoreConstants.DEFAULT_SERVICE_DESCRIPTION) })
@Service
public class JdbcBlobstoreComponent implements Blobstore {

  private BlobAccessMode blobAccessMode;

  private Expression<Blob> blobSelectionExpression;

  private Blobstore blobstore;

  private DataSource dataSource;

  private Expression<Blob> emptyBlobExpression;

  private String flag;

  private Position position;

  private Configuration querydslConfiguration;

  private Boolean updateSQLAfterBlobContentManipulation;

  /**
   * Component activator method.
   */
  @Activate
  public void activate() {
    JdbcBlobstoreConfiguration configuration = new JdbcBlobstoreConfiguration();
    if (((position == null) && (flag != null)) || ((position != null) && (flag == null))) {
      throw new ConfigurationException("Only add one optional option to QueryFlag. Must be add "
          + "position [" + position + "] and flag [" + flag + "] too.");
    } else if ((position != null) && (flag != null)) {
      configuration.lockBlobForShareQueryFlag = new QueryFlag(position, flag);
    }
    configuration.blobAccessMode = blobAccessMode;
    configuration.blobSelectionExpression = blobSelectionExpression;
    configuration.emptyBlobExpression = emptyBlobExpression;
    configuration.querydslConfiguration = querydslConfiguration;
    configuration.updateSQLAfterBlobContentManipulation = updateSQLAfterBlobContentManipulation;
    blobstore = new JdbcBlobstore(dataSource, configuration);
  }

  @Override
  public BlobAccessor createBlob() {
    return blobstore.createBlob();
  }

  @Override
  public void deleteBlob(final long blobId) {
    blobstore.deleteBlob(blobId);
  }

  @Override
  public BlobReader readBlob(final long blobId) {
    return blobstore.readBlob(blobId);
  }

  @Override
  public BlobReader readBlobForUpdate(final long blobId) {
    return blobstore.readBlobForUpdate(blobId);
  }

  @StringAttribute(attributeId = JdbcBlobstoreConstants.ATTR_BLOB_ACCESS_MODE, optional = true,
      options = {
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_BLOB_ACCESS_MODE_BYTES,
              value = JdbcBlobstoreConstants.OPTION_BLOB_ACCESS_MODE_BYTES),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_BLOB_ACCESS_MODE_STREAM,
              value = JdbcBlobstoreConstants.OPTION_BLOB_ACCESS_MODE_STREAM) },
      label = "Blob Access Mode",
      description = "The mode how the blob is accessed. If <code>null</code>, the mode is "
          + "automatically guessed from the type of the database.")
  public void setBlobAccessMode(final String blobAccessMode) {
    this.blobAccessMode = blobAccessMode != null ? BlobAccessMode.valueOf(blobAccessMode) : null;
  }

  @ServiceRef(attributeId = JdbcBlobstoreConstants.ATTR_BLOB_SELECTION_EXPRESSION_TARGET,
      optional = true, label = "Blob selection expression",
      description = "Selection expression in SQL queries of the Blob field. If null it is "
          + "automatically derived based on the database metadata. OSGi Service filter "
          + "expression for Expression<Blob>.")
  public void setBlobSelectionExpression(final Expression<Blob> blobSelectionExpression) {
    this.blobSelectionExpression = blobSelectionExpression;
  }

  @ServiceRef(attributeId = JdbcBlobstoreConstants.ATTR_DATASOURCE_TARGET, label = "DataSource",
      defaultValue = "",
      description = "The datasource that will be used to store and read blobs. OSGi Service filter "
          + "expression for javax.sql.DataSource.")
  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @ServiceRef(attributeId = JdbcBlobstoreConstants.ATTR_EMPTY_BLOB_EXPRESSION_TARGET,
      optional = true, label = "Empty blob expression.",
      description = "Expression that generates the empty blob. If null the empty blob expression"
          + " is automatically guessed from the type of the database. OSGi Service filter "
          + "expression for Expression<Blob>.")
  public void setEmptyBlobExpression(final Expression<Blob> emptyBlobExpression) {
    this.emptyBlobExpression = emptyBlobExpression;
  }

  @StringAttribute(attributeId = JdbcBlobstoreConstants.ATTR_POSITION, optional = true,
      options = {
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_FILTERS,
              value = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_FILTERS),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_GROUP_BY,
              value = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_GROUP_BY),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_HAVING,
              value = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_HAVING),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_ORDER,
              value = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_ORDER),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_PROJECTION,
              value = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_PROJECTION),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_SELECT,
              value = JdbcBlobstoreConstants.OPTION_POSITION_AFTER_SELECT),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_BEFORE_FILTERS,
              value = JdbcBlobstoreConstants.OPTION_POSITION_BEFORE_FILTERS),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_BEFORE_GROUP_BY,
              value = JdbcBlobstoreConstants.OPTION_POSITION_BEFORE_GROUP_BY),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_BEFORE_HAVING,
              value = JdbcBlobstoreConstants.OPTION_POSITION_BEFORE_HAVING),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_BEFORE_ORDER,
              value = JdbcBlobstoreConstants.OPTION_POSITION_BEFORE_ORDER),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_END,
              value = JdbcBlobstoreConstants.OPTION_POSITION_END),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_START,
              value = JdbcBlobstoreConstants.OPTION_POSITION_START),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_START_OVERRIDE,
              value = JdbcBlobstoreConstants.OPTION_POSITION_START_OVERRIDE),
          @StringAttributeOption(label = JdbcBlobstoreConstants.OPTION_POSITION_WITH,
              value = JdbcBlobstoreConstants.OPTION_POSITION_WITH) },
      label = "Position (QueryFlag)",
      description = "The QueryFlag position. If add must be add flag too.")
  public void setPosition(final String position) {
    this.position = position != null ? Position.valueOf(position) : null;
  }

  @ServiceRef(attributeId = JdbcBlobstoreConstants.ATTR_QUERYDSL_CONFIGURATION_TARGET,
      optional = true, label = "Querydsl Configuration",
      description = "Configuration of queryDsl to construct the SQL queries. If null, it is "
          + "guessed based on the metadata of the database connection. OSGi Service filter "
          + "expression for com.querydsl.sql.Configuration.")
  public void setQuerydslConfiguration(final Configuration querydslConfiguration) {
    this.querydslConfiguration = querydslConfiguration;
  }

  @StringAttribute(attributeId = JdbcBlobstoreConstants.ATTR_FLAG, optional = true,
      label = "Flag (QueryFlag)",
      description = "Flaq to query serialization. If add must be add position too.")
  public void setQueryflag(final String flag) {
    this.flag = flag;
  }

  @BooleanAttribute(
      attributeId = JdbcBlobstoreConstants.ATTR_UPDATE_SQL_AFTER_BLOB_CONTENT_MANIPULATION,
      optional = true, label = "Update SQL After Blob Content Manipulation",
      description = "Whether calling update SQL after manipulating the Blob instance is "
          + "necessary or not.")
  public void setUpdateSQLAfterBlobContentManipulation(
      final Boolean updateSQLAfterBlobContentManipulation) {
    this.updateSQLAfterBlobContentManipulation = updateSQLAfterBlobContentManipulation;
  }

  @Override
  public BlobAccessor updateBlob(final long blobId) {
    return blobstore.updateBlob(blobId);
  }

}
