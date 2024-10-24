package com.mna.aipj.model;

import dev.langchain4j.model.output.structured.Description;

@Description("Tipo de alerta que se considera que tiene el texto con respecto al ITESM.")
public enum AlertLevel {

    @Description("Temas irrelevantes como Festividades, eventos deportivos no relacionados con el ITESM," +
            "Comentarios triviales o personales no relacionados ocn la educación o el impacto institucional." +
            "Con cualquier figura publica." +
            " En cualquier medio o plataforma que hable de un tema irrelevante o eventro trivial no relacionado con la educacio")
    NONE,

    @Description("Temas como Reforma Educativa, Financiamiento Educativo, Apoyo financiero y becas, Subsidio Educativo," +
            "Cambio legislativo en materia educativa, Seguridad, Huelga, Tramites Academicos, Planes de Estudio, Nueva Escuela Mexicana," +
            "Ciencia, Tecnología, Innovación, Educacion Superior, Derecho a la Educacion, Conahcyt, Red Ecos de Educacion, Sistema Educativo. " +
            "Con Figuras publicas como Presidentes, Gobernadores, Senadores, Diputados, Ministros de Educación, Directivos de universidades." +
            " En plataformas reconocidas como El financiero, El economista, La jornada o Comunicados oficiales del gobierno o instituciones educativas relevantes")
    HIGH,

    @Description("Temas como, Cambios en la matricula, Declaraciones de figuras públicas, Calidad educativa, Ranking Universitario," +
            "Evaluaciones, Política pública educativa, presupuesto universitario, Relaciones con otras universidades, Colaboración en materia educativa" +
            "Ley de trabajo, jubilados, catedráticos, Derechos laborales, Desarrollo de proyectos academicos." +
            " Con figuras publicas como Senadores, Diputados, Académicos de renombre, Catedráticos, Lideres Empresariales." +
            " En Redes sociales públicas influyentes, o plataformas menos formales pero de relevancia en el ambito educativo")
    MEDIUM,

    @Description("Temas como Conferencias, Eventos educativos, Charlas, Opiniones personales, Honoríficos, Distinciones, Actividades extracurriculares." +
            " Con personalidades del entretenimiento, presidentes de otras naciones cuando hablen de nuestros temas de interes en México." +
            " Con comentarios de usuarios comunes en redes sociales, Blogs de poca relevancia o no verificados")
    LOW
}
