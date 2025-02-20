/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.eci.arsw.model.Blueprint;
import edu.eci.arsw.persistence.impl.InMemoryBlueprintPersistence;
import edu.eci.arsw.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */

@RestController
@Component
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {
	InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

	@Autowired
	private BlueprintsServices blueprintsServices;
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Set<Blueprint>> getBlueprints() {
		Set<Blueprint> blueprints = blueprintsServices.getAllBlueprints();
		return ResponseEntity.ok(blueprints);
	}
}

