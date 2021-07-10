package org.juanjo.csv.utils;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.experimental.UtilityClass;
import org.juanjo.csv.dao.Record;
import org.juanjo.csv.exception.InternalException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Mapper to convert a csv into a list of dao records
 */
@UtilityClass
public final class CsvToDaoMapper {

	/**
	 * Parse a CSV file into a list of records
	 *
	 * @param file to parse
	 * @return the list of records
	 */
	public static List<Record> parse(MultipartFile file) throws InternalException {
		try {
			InputStreamReader streamReader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
			return new CsvToBeanBuilder<Record>(streamReader).withType(Record.class).build().parse();
		} catch (IOException e) {
			throw new InternalException("Error parsing the CSV with message " + e.getMessage());
		}
	}
}
