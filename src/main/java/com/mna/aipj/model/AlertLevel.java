package com.mna.aipj.model;

import dev.langchain4j.model.output.structured.Description;

@Description("Tipo de alerta que se considera que tiene el texto con respecto al ITESM.")
public enum AlertLevel {

    @Description("Sin importancia relevante o sin impacto para el ITESM.")
    NONE,

    @Description("Alertas críticas que afectan directamente a la reputación, financiamiento o estabilidad del ITESM." +
            " Ejemplos incluyen reformas educativas nacionales que impactan negativamente al ITESM," +
            " ataques directos a la universidad por parte de figuras públicas, o situaciones de seguridad interna o" +
            " ciberataques que pongan en riesgo los datos o la operación de la institución.")
    HIGH,

    @Description("Alertas de prioridad media que afectan aspectos importantes de la operación o la percepción del ITESM." +
            " Incluyen cambios en políticas educativas," +
            " declaraciones de figuras públicas sobre la educación privada que podrían influir en el ITESM," +
            " o problemas que impacten la calidad educativa o las relaciones con los estudiantes.")
    MEDIUM,

    @Description("Alertas de baja prioridad que tienen un impacto limitado o menor sobre la operación del ITESM." +
            " Esto podría incluir menciones menores en redes sociales o" +
            " medios que no afecten directamente la reputación ni la operación del ITESM.")
    LOW
}
