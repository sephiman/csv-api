package org.juanjo.csv.service;

import org.juanjo.csv.exception.InternalException;
import org.juanjo.csv.exception.NotFoundException;
import org.juanjo.csv.exception.ValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Service that manages the records
 */
public interface RecordService {

	/**
	 * Gets the record by the {@code code} provided.
	 *
	 * @param code that identifies the record
	 * @return found record
	 * @throws NotFoundException when record is not found
	 */
	File getByCode(String code) throws NotFoundException, InternalException;

	/**
	 * Saves a CSV with multiple files if there is no duplicated code
	 *
	 * @param request with multiple records
	 */
	void save(MultipartFile request) throws InternalException, ValidationException;

	/**
	 * Gets all records that are in database
	 *
	 * @return csv file
	 */
	File getAllRecords() throws InternalException;

	/**
	 * Deletes all records
	 */
	void deleteAllRecords();
}
