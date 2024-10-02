package com.mna.aipj.aidto;

import com.mna.aipj.model.AlertLevel;
import dev.langchain4j.model.output.structured.Description;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Description("Formato de respuesta del análisis.")
public class AnalyzerResultResponse {

    @Description("El numero de mencion proporcionado en el texto.")
    private int mentionNumber;

    @Description("Tema de conversacion principal del texto. Ejemplo: Reforma Educativa")
    private String topic;

    private AlertLevel alertLevel;

}
