package edu.eci.arsw.persistence.impl;

import edu.eci.arsw.model.Blueprint;
import edu.eci.arsw.model.Point;
import edu.eci.arsw.persistence.BlueprintNotFoundException;
import edu.eci.arsw.persistence.BlueprintPersistenceException;
import edu.eci.arsw.persistence.BlueprintsPersistence;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final ConcurrentHashMap<Tuple<String, String>, Blueprint> blueprints = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public InMemoryBlueprintPersistence() {
        // Datos de prueba
        saveInitialData();
    }

    private void saveInitialData() {
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
        lock.writeLock().lock();
        try {
            if (blueprints.containsKey(key)) {
                throw new BlueprintPersistenceException("El plano ya existe: " + bp);
            }
            blueprints.put(key, bp);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, bprintname);
        lock.readLock().lock();
        try {
            Blueprint blueprint = blueprints.get(key);
            if (blueprint == null) {
                throw new BlueprintNotFoundException("No se encontró el plano '" + bprintname + "' del autor '" + author + "'");
            }
            return blueprint;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        lock.readLock().lock();
        try {
            Set<Blueprint> result = ConcurrentHashMap.newKeySet();
            blueprints.forEach((key, value) -> {
                if (key.getElem1().equals(author)) {
                    result.add(value);
                }
            });

            if (result.isEmpty()) {
                throw new BlueprintNotFoundException("No se encontraron planos para el autor '" + author + "'");
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        lock.readLock().lock();
        try {
            return new HashSet<>(blueprints.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void updateBluePrint(String author, String bpname, Blueprint updatedBlueprint) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, bpname);
        lock.writeLock().lock();
        try {
            if (!blueprints.containsKey(key)) {
                throw new BlueprintNotFoundException("No se encontró el plano '" + bpname + "' del autor '" + author + "'");
            }
            blueprints.put(key, updatedBlueprint);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteBlueprint(String author, String bpname) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, bpname);
        lock.writeLock().lock();
        try {
            if (!blueprints.containsKey(key)) {
                throw new BlueprintNotFoundException("No se encontró el plano '" + bpname + "' del autor '" + author + "'");
            }
            blueprints.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

}
