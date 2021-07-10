package org.juanjo.csv.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.juanjo.csv.dao.Record;
import org.juanjo.csv.exception.InternalException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestUtils {

	public static List<Record> generateRecords(int total) {
		List<Record> result = new ArrayList<>();
		IntStream.range(0, total).forEach(i -> {
			Record record = new Record();
			record.setCode(RandomStringUtils.randomAlphanumeric(32));
			result.add(record);
		});
		return result;
	}

	public static File getRandomCsvFile(int total) throws InternalException {
		return DaoToCsvMapper.parse(generateRecords(total));
	}

	public static MultipartFile getRandomCsvRequest(int total) throws InternalException, IOException {
		File file = getRandomCsvFile(total);
		return new MockMultipartFile(file.getName(), new FileInputStream(file));
	}
}
