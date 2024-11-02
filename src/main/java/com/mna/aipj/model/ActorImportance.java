package com.mna.aipj.model;

import dev.langchain4j.model.output.structured.Description;

@Description("Importancia del actor de la mención")
public enum ActorImportance {
    @Description("Cualquier tipo de figura publica con poca o nula relevancia")
    NONE,

    @Description("Figuras publicas como Presidentes, Gobernadores, Senadores, Diputados, Ministros de Educación, Directivos de universidades.")
    HIGH,

    @Description("Figuras publicas como Académicos de renombre, Catedráticos, Lideres Empresariales.")
    MEDIUM,

    @Description("Con personalidades del entretenimiento, presidentes de otras naciones cuando hablen de nuestros temas de interes en México.")
    LOW
}
