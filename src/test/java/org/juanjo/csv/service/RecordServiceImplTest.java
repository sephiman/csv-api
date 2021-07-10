package org.juanjo.csv.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.csv.dao.Record;
import org.juanjo.csv.exception.InternalException;
import org.juanjo.csv.exception.NotFoundException;
import org.juanjo.csv.exception.ValidationException;
import org.juanjo.csv.persistence.RecordRepository;
import org.juanjo.csv.utils.CsvToDaoMapper;
import org.juanjo.csv.utils.DaoToCsvMapper;
import org.juanjo.csv.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class RecordServiceImplTest {
	@InjectMocks
	private RecordServiceImpl service;
	@Mock
	private RecordRepository repository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetByCodeOK() throws NotFoundException, InternalException, IOException {
		String code = RandomStringUtils.randomAlphanumeric(12);
		Record record = TestUtils.generateRecords(1).get(0);
		when(repository.findById(any())).thenReturn(Optional.of(record));
		File result = service.getByCode(code);
		assertNotNull(result);
		List<Record> foundRecords = CsvToDaoMapper.parse(new MockMultipartFile(result.getName(), new FileInputStream(result)));
		assertEquals(1, foundRecords.size());
		assertEquals(record.getCode(), foundRecords.get(0).getCode());
		verify(repository).findById(code);
	}

	@Test
	public void testGetByCodeNotFound() {
		String code = RandomStringUtils.randomAlphanumeric(12);
		when(repository.findById(code)).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> service.getByCode(code));
	}

	@Test
	public void testSaveOk() throws IOException, InternalException, ValidationException {
		MultipartFile request = TestUtils.getRandomCsvRequest(RandomUtils.nextInt(1, 10));
		when(repository.findAllByCodeIn(any())).thenReturn(Collections.emptyList());
		service.save(request);
		ArgumentCaptor<List<Record>> captor = ArgumentCaptor.forClass(List.class);
		verify(repository).saveAll(captor.capture());
		List<Record> inserted = captor.getValue();
		List<Record> requestRecords = CsvToDaoMapper.parse(request);
		assertEquals(requestRecords.size(), inserted.size());
		IntStream.range(0, inserted.size()).forEach(i -> assertEquals(requestRecords.get(i).getCode(), inserted.get(i).getCode()));
	}

	@Test
	public void testSaveDuplicatedInRequest() throws InternalException {
		List<Record> requestList = TestUtils.generateRecords(10);
		Record duplicated = new Record();
		duplicated.setCode(requestList.get(0).getCode());
		requestList.add(duplicated);
		File requestFile = DaoToCsvMapper.parse(requestList);
		assertThrows(ValidationException.class,
				() -> service.save(new MockMultipartFile(requestFile.getName(), new FileInputStream(requestFile))));
		verify(repository, never()).findAllByCodeIn(any());
		verify(repository, never()).saveAll(any());
	}

	@Test
	public void testSaveExistingInDb() throws IOException, InternalException {
		MultipartFile request = TestUtils.getRandomCsvRequest(RandomUtils.nextInt(1, 10));
		when(repository.findAllByCodeIn(any())).thenReturn(TestUtils.generateRecords(10));
		assertThrows(ValidationException.class, () -> service.save(request));
		verify(repository, never()).saveAll(any());
	}

	@Test
	public void testDeleteAllRecords() {
		service.deleteAllRecords();
		verify(repository).deleteAll();
	}

	@Test
	public void getAllRecordsOk() throws InternalException, IOException {
		List<Record> fromDb = TestUtils.generateRecords(10);
		when(repository.findAll()).thenReturn(fromDb);
		File result = service.getAllRecords();
		assertNotNull(result);
		List<Record> foundRecords = CsvToDaoMapper.parse(new MockMultipartFile(result.getName(), new FileInputStream(result)));
		assertEquals(fromDb.size(), foundRecords.size());
		IntStream.range(0, foundRecords.size()).forEach(i -> assertEquals(foundRecords.get(i).getCode(), fromDb.get(i).getCode()));
	}

	@Test
	public void getAllRecordsEmpty() throws InternalException, IOException {
		when(repository.findAll()).thenReturn(Collections.emptyList());
		File result = service.getAllRecords();
		assertNotNull(result);
		List<Record> foundRecords = CsvToDaoMapper.parse(new MockMultipartFile(result.getName(), new FileInputStream(result)));
		assertTrue(foundRecords.isEmpty());
	}
}
