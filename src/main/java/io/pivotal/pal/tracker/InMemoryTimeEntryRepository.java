package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> store = new HashMap<>();

    private long id = 1L;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        if (timeEntry.getId() == 0) {
            timeEntry.setId(id++);
        }
        store.put(timeEntry.getId(), timeEntry);

        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        return store.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(store.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (store.containsKey(id)) {
            timeEntry.setId(id);
            store.put(id, timeEntry);
            return timeEntry;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        store.remove(id);
    }
}
