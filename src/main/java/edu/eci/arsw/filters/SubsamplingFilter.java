package edu.eci.arsw.filters;


import edu.eci.arsw.model.Blueprint;
import edu.eci.arsw.model.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SubsamplingFilter implements BlueprintFilter {

    @Override
    public Blueprint filterBlueprint(Blueprint bp) {
        List<Point> points = bp.getPoints();
        List<Point> filteredPoints = new ArrayList<>();

        for (int i = 0; i < points.size(); i += 2) {
            filteredPoints.add(points.get(i));
        }

        bp.setPoints(filteredPoints); // Mantiene el mismo objeto
        return bp;
    }

    @Override
    public Set<Blueprint> filterBlueprints(Set<Blueprint> blueprints) {
        return blueprints.stream()
                .map(this::filterBlueprint)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
