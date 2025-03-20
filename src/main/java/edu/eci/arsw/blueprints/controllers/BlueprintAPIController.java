/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.Set;

import edu.eci.arsw.model.Blueprint;
import edu.eci.arsw.persistence.BlueprintNotFoundException;
import edu.eci.arsw.persistence.BlueprintPersistenceException;
import edu.eci.arsw.persistence.impl.InMemoryBlueprintPersistence;
import edu.eci.arsw.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author hcadavid
 */

@RestController
@Component
@RequestMapping
public class BlueprintAPIController {

    InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

    @Autowired
    private BlueprintsServices blueprintsServices;

    @GetMapping("/blueprints")
    public ResponseEntity<Set<Blueprint>> getBlueprints() {
        Set<Blueprint> blueprints = blueprintsServices.getAllBlueprints();
        return ResponseEntity.ok(blueprints);
    }

    @GetMapping("/blueprints/{author}")
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author) throws BlueprintNotFoundException {
        Set<Blueprint> blueprints = blueprintsServices.getBlueprintsByAuthor(author);
        System.out.println("Planos encontrados para el autor " + author + ": " + blueprints);
        if (blueprints == null || blueprints.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron planos del autor:" + author);
        }
        return ResponseEntity.ok(blueprints);
    }

    @GetMapping("/blueprints/{author}/{bpname}")
    public ResponseEntity<?> getBlueprintByAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            Blueprint blueprint = blueprintsServices.getBlueprint(author, bpname);
            return ResponseEntity.ok(blueprint);
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el plano '" + bpname + "' del autor '" + author + "'");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }

    @PostMapping("/planos")
    public ResponseEntity<?> addNewBluePrint(@RequestBody Blueprint blueprint) {
        try {
            blueprintsServices.addNewBlueprint(blueprint);
            return ResponseEntity.status(HttpStatus.CREATED).body("Plano creado Exitosamente");
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el plano");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor" + e.getMessage());
        }

    }

    @PutMapping("/blueprints/{author}/{bpname}")
    public ResponseEntity<?> updateBluePrint(@PathVariable String author, @PathVariable String bpname,@RequestBody Blueprint updBlueprint){
        try{
            blueprintsServices.updateBlueprint(author, bpname, updBlueprint);
            return ResponseEntity.ok("Plano actualizado correctamente ");
        }catch(BlueprintNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nop se encontro el plano" + bpname + "del autor" + author);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el plano");
        }
    }

    @DeleteMapping("/blueprints/{author}/{bpname}")
    public ResponseEntity<?> deleteBlueprint(@PathVariable String author, @PathVariable String bpname) {
        try {
            blueprintsServices.deleteBlueprint(author, bpname);
            return ResponseEntity.ok("Plano eliminado exitosamente.");
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el plano '" + bpname + "' del autor '" + author + "'");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }
}
