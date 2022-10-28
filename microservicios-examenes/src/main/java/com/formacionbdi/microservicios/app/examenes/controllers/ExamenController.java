package com.formacionbdi.microservicios.app.examenes.controllers;


import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicio.commons.controllers.CommonController;
import com.formacionbdi.microservicios.app.examenes.services.ExamenService;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;

@RestController
public class ExamenController  extends  CommonController<Examen,ExamenService>{
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Examen examen, @PathVariable Long id, BindingResult result) {
		if (result.hasErrors()) {
			return this.validar(result);	
		}
		
		Optional<Examen> o = service.findById(id);
		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Examen examenDb = o.get();
		examenDb.setNombre(examen.getNombre());
//		List<Pregunta> eliminadas = new ArrayList();
		System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
		examenDb.getPreguntas()
		.stream()
		.filter(pdb -> !examen.getPreguntas().contains(pdb))
		.forEach(examenDb::removePreguntas);
		System.out.println("--------------------------------------------------");
		//lo mismo que el bloque anterior
//		examenDb.getPreguntas().forEach(pdb -> {
//			if(!examen.getPreguntas().contains(pdb)) {
//				eliminadas.add(pdb);
//			}
//		});
		
		
		//eliminadas.forEach(examenDb::removePreguntas);
		
		//igual al bloque de arriba comentado
//		eliminadas.forEach(p -> {
//			examenDb.removePreguntas(p);
//		});
		 	
		examenDb.setPreguntas(examen.getPreguntas());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(examenDb));
		
	}
	
	@GetMapping("/filter/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term){
		return ResponseEntity.ok(service.findByNombre(term));
	}
	
	@GetMapping("/asignaturas")
	public ResponseEntity<?> listarAsignaturas(){
		return ResponseEntity.ok(service.finAllAsignaturas());
	}
}


