package com.formacionbdi.microservicio.app.usuarios.services;



import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

import java.util.List;

import com.formacionbdi.microservicio.commons.services.CommonService;

public interface AlumnoService extends CommonService<Alumno> {
	public List<Alumno> findByNombreOrApellido (String term);
}
