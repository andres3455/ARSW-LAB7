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

import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Primary
public class BlueprintsServices {

    @Autowired
    private BlueprintsPersistence bpp;

    @Autowired
    @Qualifier("redundancyFilter")
    private BlueprintFilter blueprintFilter;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void addNewBlueprint(Blueprint blueprint) throws BlueprintPersistenceException {
        lock.writeLock().lock();
        try {
            bpp.saveBlueprint(blueprint);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Set<Blueprint> getAllBlueprints() {
        lock.readLock().lock();
        try {
            return bpp.getAllBlueprints();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        lock.readLock().lock();
        try {
            Blueprint blueprint = bpp.getBlueprint(author, name);
            return blueprintFilter.filterBlueprint(blueprint);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        lock.readLock().lock();
        try {
            return blueprintFilter.filterBlueprints(bpp.getBlueprintsByAuthor(author));
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updateBlueprint(String author, String bpname, Blueprint updatedBlueprint) throws BlueprintNotFoundException {
        lock.writeLock().lock();
        try {
            bpp.updateBluePrint(author, bpname, updatedBlueprint);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void deleteBlueprint(String author, String bpname) throws BlueprintNotFoundException {
        lock.writeLock().lock();
        try {
            bpp.deleteBlueprint(author, bpname);
        } finally {
            lock.writeLock().unlock();
        }
    }

}
