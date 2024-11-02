package com.mna.aipj.model;

import dev.langchain4j.model.output.structured.Description;

@Description("Importancia del tema de la mención")
public enum TopicImportance {

    @Description("Temas irrelevantes como Festividades, eventos deportivos no relacionados con el ITESM," +
            "Temas triviales o personales no relacionados ocn la educación o el impacto institucional.")
    NONE,

    @Description("Temas como Reforma Educativa, Financiamiento Educativo, Apoyo financiero y becas, Subsidio Educativo," +
            "Cambio legislativo en materia educativa, Seguridad, Huelga, Tramites Academicos, Planes de Estudio, Nueva Escuela Mexicana," +
            " Ciencia, Tecnología, Innovación, Educacion Superior, Derecho a la Educacion, Conahcyt, Red Ecos de Educacion, Sistema Educativo.")
    HIGH,

    @Description("Temas como, Cambios en la matricula, Declaraciones de figuras públicas, Calidad educativa, Ranking Universitario," +
            " Evaluaciones, Política pública educativa, Presupuesto universitario, Relaciones con otras universidades, Colaboración en materia educativa" +
            " Ley de trabajo, jubilados, catedráticos, Derechos laborales, Desarrollo de proyectos academicos.")
    MEDIUM,

    @Description("Temas como Conferencias, Eventos educativos, Charlas, Opiniones personales, Honoríficos, Distinciones, Actividades extracurriculares.")
    LOW
}
