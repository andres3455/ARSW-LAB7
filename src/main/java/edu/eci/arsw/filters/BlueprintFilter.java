
package edu.eci.arsw.filters;


import edu.eci.arsw.model.Blueprint;
import org.springframework.stereotype.Component;

import java.util.Set;


public interface BlueprintFilter {


    public Blueprint filterBlueprint(Blueprint bp);

    Set<Blueprint> filterBlueprints(Set<Blueprint> blueprints);


}