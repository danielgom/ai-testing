package com.mna.aipj.aidto;

import com.mna.aipj.model.ActorImportance;
import com.mna.aipj.model.TopicImportance;
import com.mna.aipj.model.Sentiment;
import dev.langchain4j.model.output.structured.Description;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Description("Formato de respuesta del análisis.")
public class AnalyzerResultResponse {

    @Description("Tema de conversacion principal del texto. Ejemplo: Reforma Educativa")
    private String topic;

    @Description("Nombre de quien hace la mención")
    private String actor;

    @Description("Profession del actor de la mención")
    private String profession;

    private TopicImportance topicImportance;

    private ActorImportance actorImportance;

    private Sentiment sentiment;
}
