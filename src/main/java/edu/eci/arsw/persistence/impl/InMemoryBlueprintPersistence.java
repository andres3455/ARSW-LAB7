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

/**
 *
 * @author hcadavid
 * @author Andrés Felipe Rodríguez Chaparro
 * @author Santiago Guerra
 */

@Component
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final Map<Tuple<String, String>, Blueprint> blueprints = new HashMap<>();

    public InMemoryBlueprintPersistence() {
        // load stub data
        Point[] pts = new Point[] { new Point(140, 140), new Point(115, 115) };
        Blueprint bp = new Blueprint("Pedro", "Plano1", pts);
        Point[] pts2 = new Point[] { new Point(120, 120), new Point(80, 80)};
        Blueprint bp2 = new Blueprint("Pedro", "Plano2", pts2);
        Point[] pts3 = new Point[] { new Point(100, 100), new Point(60, 60)};
        Blueprint bp3 = new Blueprint("Juan", "Plano3", pts3);
        Point[] pts4 = new Point[] { new Point(80, 80), new Point(40, 40)};
        Blueprint bp4 = new Blueprint("Juan", "Plano4", pts4);
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);
        blueprints.put(new Tuple<>(bp4.getAuthor(), bp4.getName()), bp4);

    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        } else {
            blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        return blueprints.get(new Tuple<>(author, bprintname));

    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> authorBlueprints = new HashSet<>();
        for (Blueprint b : blueprints.values()) {
            if (b.getAuthor().equals(author)) {
                authorBlueprints.add(b);

            }
        }
        return authorBlueprints;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        Set<Blueprint> allBlueprints = new HashSet<>();
        for (Blueprint b : blueprints.values()) {
            allBlueprints.add(b);
        }
        return allBlueprints;
    }

}
