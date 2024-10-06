package com.mna.aipj.model;

import dev.langchain4j.model.output.structured.Description;

@Description("Clasificación del sentimiento expresado en la mención realizada por el actor.")
public enum Sentiment {

    @Description("El sentimiento expresado es positivo, reflejando satisfacción, entusiasmo o aprobación." +
            " Puede incluir expresiones de felicidad, orgullo, o apoyo hacia una idea o situación.")
    POSITIVE,

    @Description("El sentimiento expresado es negativo, reflejando descontento, enojo o desaprobación." +
            " Puede incluir expresiones de molestia, crítica, enojo, o desaprobación hacia una situación o idea.")
    NEGATIVE,

    @Description("El sentimiento expresado es neutral, sin indicaciones emocionales claras." +
            " Incluye información objetiva o factual donde no se perciben emociones destacables del actor.")
    NEUTRAL
}
