/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.services;


import edu.eci.arsw.filters.BlueprintFilter;
import edu.eci.arsw.model.Blueprint;
import edu.eci.arsw.persistence.BlueprintNotFoundException;
import edu.eci.arsw.persistence.BlueprintPersistenceException;
import edu.eci.arsw.persistence.BlueprintsPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hcadavid
 */




@Service
@Primary
public class BlueprintsServices {

    @Autowired
    public BlueprintsPersistence bpp;

    @Autowired
    @Qualifier("redundancyFilter")
    public BlueprintFilter blueprintFilter;

    @Autowired
    @Qualifier("subsamplingFilter")
    private BlueprintFilter subsamplingFilter;

    private final Map<String , Set<String>> blueprints = new HashMap<>();

    public void addNewBlueprint(Blueprint blueprint) throws BlueprintPersistenceException {
        bpp.saveBlueprint(blueprint);
        
    }
    
    public Set<Blueprint> getAllBlueprints(){
        return bpp.getAllBlueprints();
    }

    /**
     * 
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        try {
            Blueprint blueprint = bpp.getBlueprint(author, name);
            
            if (blueprint == null) {
                throw new BlueprintNotFoundException("No se encontró el plano '" + name + "' del autor '" + author + "'");
            }
    
            return blueprintFilter.filterBlueprint(blueprint);
        } catch (BlueprintNotFoundException e) {
            throw new BlueprintNotFoundException("No se encontró el plano '" + name + "' del autor '" + author + "'");
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el plano: " + e.getMessage(), e);
        }
    }
    
    
    /**
     * 
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
        return blueprintFilter.filterBlueprints(bpp.getBlueprintsByAuthor(author));
    }


}