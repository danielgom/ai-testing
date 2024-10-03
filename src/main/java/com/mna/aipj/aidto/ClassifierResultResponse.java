package com.mna.aipj.aidto;

import dev.langchain4j.model.output.structured.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Description("Formato de respuesta de la clasificación.")
public class ClassifierResultResponse {

    @Description("Clasificación final que describe si la mención es imporante o no.")
    private boolean isImportant;
}
