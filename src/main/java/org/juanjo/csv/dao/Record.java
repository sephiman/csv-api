package org.juanjo.csv.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Table(name = "record", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Record {
	@Id
	private String code;
	private String Source;
	private String codeListCode;
	private String displayValue;
	private String longDescription;
	private String fromDate;
	private String toDate;
	private Integer sortingPriority;
}
