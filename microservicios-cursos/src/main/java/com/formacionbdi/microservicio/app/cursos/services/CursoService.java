package com.formacionbdi.microservicio.app.cursos.services;

import com.formacionbdi.microservicio.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicio.commons.services.CommonService;

public interface CursoService extends CommonService<Curso> {
	public Curso findCursoByAlumnoId(Long id);

	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long alumnoId);
}
