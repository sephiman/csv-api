package org.juanjo.csv.persistence;

import org.juanjo.csv.dao.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Database repository for records
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, String> {

	/**
	 * Finds all records given a list of codes
	 *
	 * @param codes to filter
	 * @return list of found records
	 */
	List<Record> findAllByCodeIn(List<String> codes);
}
