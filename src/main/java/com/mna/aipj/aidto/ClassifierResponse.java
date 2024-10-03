package com.mna.aipj.aidto;

import dev.langchain4j.model.output.structured.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Description("Formato de respuesta de la clasificación de cada mencion.")
public class ClassifierResponse {

    List<ClassifierResultResponse> responseList;
}
