package de.aha.backend.model.advisor;

import de.aha.backend.model.AbstractDocument;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reviews")
public class Review extends AbstractDocument {
    private String advisorId;
    private String patientId;
    private String patientName;
    private Integer rating;
    private String comment;
}