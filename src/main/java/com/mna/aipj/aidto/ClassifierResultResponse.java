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

    @Description("Número de la mención. Este numero debe corresponder a la mención dada por la entrada")
    private int mentionNumber;

    @Description("Clasificación final que describe si la mención es imporante o no. " +
            "Menciones sin un **actor específico** Debe existir una figura pública identificada que haga la declaración," +
            "como un presidente, ministro, rector, etc." +
            "Menciones que describan únicamente la universidad sin atribuirlas a una persona deben clasificarse como **No Importante**.")
    private boolean isImportant;
}
