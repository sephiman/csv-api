package org.juanjo.csv.utils;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.juanjo.csv.dao.Record;
import org.juanjo.csv.exception.InternalException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Mapper to convert a dao to a csv
 */
@UtilityClass
public final class DaoToCsvMapper {

	/**
	 * Convert the received records into a file. The file is created in the java temporary folder so it will be cleaned later on
	 *
	 * @param records to parse
	 * @return CSV file
	 */
	public static File parse(List<Record> records) throws InternalException {
		File tempFile;
		try {
			tempFile = File.createTempFile(RandomStringUtils.randomAlphanumeric(32), CSVConstants.CSV_EXTENSION);
			try (Writer writer = new FileWriter(tempFile.getAbsolutePath())) {
				StatefulBeanToCsv<Record> statefulBeanToCsv = new StatefulBeanToCsvBuilder<Record>(writer).build();
				statefulBeanToCsv.write(records);
			}
		} catch (IOException | CsvException e) {
			throw new InternalException("Error parsing the records to CSV with message " + e.getMessage());
		}
		return tempFile;
	}
}
