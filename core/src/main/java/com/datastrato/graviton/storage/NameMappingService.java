/*
 * Copyright 2023 Datastrato.
 * This software is licensed under the Apache License version 2.
 */

package com.datastrato.graviton.storage;

import com.datastrato.graviton.storage.kv.KvEntityStore;
import java.io.IOException;

/**
 * {@link NameMappingService} manages name to id mappings when using {@link KvEntityStore} to store
 * entity.
 *
 * <p>Note. Implementations of this interface should be thread-safe.
 */
public interface NameMappingService extends AutoCloseable {

  /**
   * Get id by name in the mapping service.
   *
   * @param name the name of the entity
   * @return the id of the name, or null if the name does not exist
   * @throws IOException if the underlying storage failed
   */
  Long getIdByName(String name) throws IOException;

  /**
   * Get id from name. If the name does not exist, we will create a new id for the name and bind the
   * mapping between them
   *
   * @param name the name of the entity
   * @return the id of the name
   * @throws IOException if the underlying storage failed
   */
  long getOrCreateIdFromName(String name) throws IOException;

  /**
   * Update the mapping of the name to id. This method is used to update the mapping when we rename
   * an entity. E.g., If we change the name of entity from oldName to nameName, the following is the
   * mapping before and after the update.
   *
   * <pre>
   * Before:
   *   oldname -> 1
   *   1       -> oldname
   *
   * After:
   *  newname -> 1
   *  1       -> newname
   * </pre>
   *
   * @param oldName name to be updated
   * @param newName new name
   * @return true if the name exists and is updated successfully, false if the name does not exist
   * @throws IOException if the underlying storage failed
   */
  boolean updateName(String oldName, String newName) throws IOException;

  /**
   * Unbind id-name mapping. Ignore if the name does not exist.
   *
   * @param name name to be unbined
   * @return true if the name exists and is deleted successfully, false if the name does not exist
   * @throws IOException if the underlying storage failed
   */
  boolean unbindNameAndId(String name) throws IOException;
}