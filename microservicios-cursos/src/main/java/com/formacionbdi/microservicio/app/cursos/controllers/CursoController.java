package com.formacionbdi.microservicio.app.cursos.controllers;

import com.formacionbdi.microservicio.app.cursos.services.CursoService;
import com.formacionbdi.microservicio.commons.controllers.CommonController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import  com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;

import ch.qos.logback.core.joran.conditional.IfAction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.formacionbdi.microservicio.app.cursos.models.entity.Curso;
@RestController
public class CursoController extends CommonController<Curso,CursoService> {

	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("balanceador", balanceadorTest);
		response.put("cursos", service.findAll());
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody Curso curso, @PathVariable Long id, BindingResult result){
		if (result.hasErrors()) {
			return this.validar(result);	
		}
		Optional<Curso> o= this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		dbCurso.setNombre(curso.getNombre());
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<?> asignarAlumnos (@RequestBody List<Alumno> alumnos , @PathVariable Long id){
		Optional<Curso> o= this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		alumnos.forEach(a -> {
			dbCurso.addAlumnos(a);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<?> eliminarAlumnos (@RequestBody Alumno alumno , @PathVariable Long id){
		Optional<Curso> o= this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		dbCurso.removeAlumnos(alumno);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	@GetMapping("/alumno/{id}")
	public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id){
		Curso curso = service.findCursoByAlumnoId(id);
		
		if (curso != null) {
			List<Long> examenesIds =(List<Long>)service.obtenerExamenesIdsConRespuestasAlumno(id);
			List<Examen> examenes = curso.getExamenes().stream().map( examen -> {
				if(examenesIds.contains(examen.getId())) {
					examen.setRespondido(true);
				}
				return examen;
			}).collect(Collectors.toList());
			curso.setExamenes(examenes);
		}
		return ResponseEntity.ok(curso);
		
	}
	
	@PutMapping("/{id}/asignar-examennes")
	public ResponseEntity<?> asignarExamenes (@RequestBody List<Examen> examen , @PathVariable Long id){
		Optional<Curso> o= this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		examen.forEach(e -> {
			dbCurso.addExamen(e);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	
	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<?> eliminarExamen (@RequestBody Examen examen , @PathVariable Long id){
		Optional<Curso> o= this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		dbCurso.removeExamen(examen);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}


	
}
