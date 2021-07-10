package org.juanjo.csv.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.juanjo.csv.dao.Record;
import org.juanjo.csv.exception.InternalException;
import org.juanjo.csv.exception.NotFoundException;
import org.juanjo.csv.exception.ValidationException;
import org.juanjo.csv.persistence.RecordRepository;
import org.juanjo.csv.utils.CsvToDaoMapper;
import org.juanjo.csv.utils.DaoToCsvMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RecordServiceImpl implements RecordService {
	private final RecordRepository recordRepository;

	@Override
	public File getByCode(String code) throws NotFoundException, InternalException {
		Record record = recordRepository.findById(code).orElseThrow(() -> {
			log.debug("record code {} not found", code);
			return new NotFoundException();
		});
		return DaoToCsvMapper.parse(Collections.singletonList(record));

	}

	@Override
	@Transactional
	public void save(MultipartFile request) throws InternalException, ValidationException {
		List<Record> records = CsvToDaoMapper.parse(request);
		if (!records.isEmpty()) {
			validateNoDuplicates(records);
			recordRepository.saveAll(records);
			log.debug("Saved {} new records", records.size());
		}
	}

	private void validateNoDuplicates(List<Record> records) throws ValidationException {
		Set<String> codes = records.stream().map(Record::getCode).collect(Collectors.toSet());
		if (codes.size() != records.size()) {
			log.debug("Found codes that are duplicated in the request");
			throw new ValidationException("The request contains duplicated codes");
		}

		List<Record> duplicates = recordRepository.findAllByCodeIn(new ArrayList<>(codes));
		if (!duplicates.isEmpty()) {
			log.debug("Found {} codes that are already in the database", duplicates.size());
			throw new ValidationException("The request contains existing codes");
		}
	}

	@Override
	public File getAllRecords() throws InternalException {
		List<Record> records = recordRepository.findAll();
		return DaoToCsvMapper.parse(records);
	}

	@Override
	public void deleteAllRecords() {
		recordRepository.deleteAll();
		log.debug("All records have been deleted");
	}
}
