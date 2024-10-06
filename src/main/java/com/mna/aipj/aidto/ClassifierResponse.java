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

    @Description("Lista que guarda la clasificación de las menciones." +
            "El tamaño de la lista debe ser igual al número de menciones clasificadas.")
    List<ClassifierResultResponse> responseList;
}
