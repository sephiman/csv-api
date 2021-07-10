package org.juanjo.csv.persistence;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.csv.dao.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.yml")
@DataJpaTest
public class RecordRepositoryTest {
	@Autowired
	private RecordRepository repository;

	@Test
	void testSave() {
		Record record = new Record();
		record.setCode(RandomStringUtils.randomAlphanumeric(12));
		Record inserted = repository.save(record);
		assertNotNull(inserted.getCode());
		assertEquals(record.getCode(), inserted.getCode());
	}

	@Test
	void testFindById() {
		Record record = new Record();
		record.setCode(RandomStringUtils.randomAlphanumeric(32));
		Record inserted = repository.save(record);
		Optional<Record> found = repository.findById(inserted.getCode());
		if (found.isPresent()) {
			assertNotNull(found.get());
			assertEquals(record.getCode(), found.get().getCode());
		} else {
			fail("Not found");
		}
	}

	@Test
	void testFindAll() {
		int total = RandomUtils.nextInt(1, 30);
		insertRecords(total);
		List<Record> result = new ArrayList<>(repository.findAll());
		assertEquals(total, result.size());
		result.forEach(record -> assertNotNull(record.getCode()));
	}

	private void insertRecords(int total) {
		IntStream.range(0, total).forEach(i -> {
			Record record = new Record();
			record.setCode(RandomStringUtils.randomAlphanumeric(32));
			repository.save(record);
		});
	}
}
