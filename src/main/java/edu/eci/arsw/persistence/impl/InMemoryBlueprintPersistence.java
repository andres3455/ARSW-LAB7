/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.persistence.impl;

import edu.eci.arsw.model.Blueprint;
import edu.eci.arsw.model.Point;
import edu.eci.arsw.persistence.BlueprintNotFoundException;
import edu.eci.arsw.persistence.BlueprintPersistenceException;
import edu.eci.arsw.persistence.BlueprintsPersistence;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final Map<Tuple<String, String>, Blueprint> blueprints = new HashMap<>();

    public InMemoryBlueprintPersistence() {
        // Cargar datos de prueba
        Point[] pts = { new Point(140, 140), new Point(115, 115) };
        Blueprint bp = new Blueprint("Pedro", "Plano1", pts);
        Point[] pts2 = { new Point(120, 120), new Point(80, 80) };
        Blueprint bp2 = new Blueprint("Pedro", "Plano2", pts2);
        Point[] pts3 = { new Point(100, 100), new Point(60, 60) };
        Blueprint bp3 = new Blueprint("Juan", "Plano3", pts3);
        Point[] pts4 = { new Point(80, 80), new Point(40, 40) };
        Blueprint bp4 = new Blueprint("Juan", "Plano4", pts4);
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);
        blueprints.put(new Tuple<>(bp4.getAuthor(), bp4.getName()), bp4);
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        Tuple<String, String> key = new Tuple<>(bp.getAuthor(), bp.getName());
        if (blueprints.containsKey(key)) {
            throw new BlueprintPersistenceException("El plano ya existe: " + bp);
        } else {
            blueprints.put(key, bp);
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, bprintname);
        if (!blueprints.containsKey(key)) {
            throw new BlueprintNotFoundException("No se encontró el plano '" + bprintname + "' del autor '" + author + "'");
        }
        return blueprints.get(key);
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> authorBlueprints = new HashSet<>();
        for (Map.Entry<Tuple<String, String>, Blueprint> entry : blueprints.entrySet()) {
            if (entry.getKey().getElem1().equals(author)) {
                authorBlueprints.add(entry.getValue());
            }
        }
        if (authorBlueprints.isEmpty()) {
            throw new BlueprintNotFoundException("No se encontraron planos para el autor '" + author + "'");
        }
        return authorBlueprints;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        return new HashSet<>(blueprints.values());
    }
    
    public void updateBlueprint(String author, String bpname, Blueprint updatedBlueprint) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, bpname);

        // Verificar si el plano existe
        if (!blueprints.containsKey(key)) {
            throw new BlueprintNotFoundException("No se encontró el plano '" + bpname + "' del autor '" + author + "'");
        }

        // Actualizar el plano en la memoria
        blueprints.put(key, updatedBlueprint);
    }

    @Override
    public void updateBluePrint(String author, String bpname, Blueprint updaBlueprint)throws BlueprintNotFoundException{
        Tuple<String, String> blueprintkey = new Tuple<>(author, bpname);

        if(!blueprints.containsKey(blueprintkey)){
            throw new BlueprintNotFoundException("no se encontro el plano"+ bpname);
        }

        blueprints.put(blueprintkey, updaBlueprint);
    }
    }

