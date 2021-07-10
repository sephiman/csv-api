package org.juanjo.csv.controller;

import lombok.AllArgsConstructor;
import org.juanjo.csv.exception.InternalException;
import org.juanjo.csv.exception.NotFoundException;
import org.juanjo.csv.exception.ValidationException;
import org.juanjo.csv.service.RecordService;
import org.juanjo.csv.utils.CSVConstants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


/**
 * Controller to handle CSV records
 */
@RestController
@RequestMapping(value = "/api/records", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RecordController {
	private final RecordService recordService;

	/**
	 * Gets a CSV with the record containing the given code
	 *
	 * @param code that identify the record
	 * @return CSV with the found record
	 * @throws NotFoundException when the resource is not found
	 */
	@GetMapping(value = "/{code}", produces = CSVConstants.CSV_MEDIA_TYPE)
	public ResponseEntity<Resource> getByCode(@PathVariable String code) throws NotFoundException, InternalException {
		File response = recordService.getByCode(code);
		return getResponseBody(response);
	}

	private ResponseEntity<Resource> getResponseBody(File response) {
		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + response.getName())
				.contentLength(response.length()).contentType(MediaType.parseMediaType("text/csv")).body(new FileSystemResource(response));
	}

	/**
	 * Gets a CSV with the record containing all existing records in database
	 *
	 * @return CSV with all records in database
	 */
	@GetMapping(produces = CSVConstants.CSV_MEDIA_TYPE)
	public ResponseEntity<Resource> getAllRecords() throws InternalException {
		File response = recordService.getAllRecords();
		return getResponseBody(response);
	}

	/**
	 * Reads the CSV to save the records into database
	 *
	 * @param request containing the CSV
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void save(@RequestParam("file") MultipartFile request) throws InternalException, ValidationException {
		recordService.save(request);
	}

	/**
	 * Deletes all records
	 */
	@DeleteMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteAllRecords() {
		recordService.deleteAllRecords();
	}
}
