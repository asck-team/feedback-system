package org.asck.api.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "event")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name ="SEQ_EVENT_GEN" , sequenceName = "SEQ_EVENT", initialValue = 5, allocationSize = 50)
public class EventTableModel {

	@Id
	@GeneratedValue(generator = "SEQ_EVENT_GEN", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column
	private String name;

	@Column(name = "owned_by")
	private Long ownedBy;

}
