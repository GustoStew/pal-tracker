package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository repo;

    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.repo = timeEntryRepository;
        this.timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("timeEntry.counter");
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry timeEntry = repo.create(timeEntryToCreate);

        actionCounter.increment();
        timeEntrySummary.record(repo.list().size());

        return new ResponseEntity<>(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
        TimeEntry timeEntry = repo.find(timeEntryId);
        if (timeEntry != null) {
            actionCounter.increment();
            return new ResponseEntity<>(timeEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry timeEntryToUpdate) {
        TimeEntry timeEntry = repo.update(timeEntryId, timeEntryToUpdate);
        actionCounter.increment();
        return new ResponseEntity<>(timeEntry, timeEntry != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity<>(repo.list(), HttpStatus.OK);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id") long timeEntryId) {
        repo.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(repo.list().size());
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
