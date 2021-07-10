package org.juanjo.csv.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.csv.exception.InternalException;
import org.juanjo.csv.exception.NotFoundException;
import org.juanjo.csv.exception.ValidationException;
import org.juanjo.csv.service.RecordService;
import org.juanjo.csv.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RecordControllerTest {
	@InjectMocks
	private RecordController controller;
	@Mock
	private RecordService recordService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetByCode() throws NotFoundException, InternalException {
		String code = RandomStringUtils.randomAlphanumeric(12);
		when(recordService.getByCode(code)).thenReturn(TestUtils.getRandomCsvFile(1));
		RestAssuredMockMvc.given().standaloneSetup(controller).when().get("/api/records/{code}", code).then()
				.statusCode(HttpStatus.OK.value());
		verify(recordService).getByCode(code);
	}

	@Test
	public void testGetAllRecords() throws InternalException {
		when(recordService.getAllRecords()).thenReturn(TestUtils.getRandomCsvFile(RandomUtils.nextInt(1, 12)));
		RestAssuredMockMvc.given().standaloneSetup(controller).when().get("/api/records").then().statusCode(HttpStatus.OK.value());
		verify(recordService).getAllRecords();
	}

	@Test
	public void testSave() throws InternalException, ValidationException {
		File request = TestUtils.getRandomCsvFile(RandomUtils.nextInt(1, 100));
		RestAssuredMockMvc.given().standaloneSetup(controller).multiPart(request).contentType(MediaType.MULTIPART_FORM_DATA_VALUE).when()
				.post("/api/records").then().statusCode(HttpStatus.CREATED.value());
		verify(recordService).save(any());
	}

	@Test
	public void testDeleteAllRecords() {
		RestAssuredMockMvc.given().standaloneSetup(controller).when().delete("/api/records").then()
				.statusCode(HttpStatus.NO_CONTENT.value());
		verify(recordService).deleteAllRecords();
	}


}
