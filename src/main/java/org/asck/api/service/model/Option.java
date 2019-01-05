package org.asck.api.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Option {

	private Long id;

	private String optionalDescription;

	private String iconPath;

}
