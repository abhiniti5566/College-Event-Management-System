package com.vcet.event;

public class Event {
    private int eventId;
    private String title, category, eventDate, venue, description;
    private int capacity;

    public Event(int eventId, String title, String category, String eventDate,
                 String venue, int capacity, String description) {
        this.eventId = eventId; this.title = title; this.category = category;
        this.eventDate = eventDate; this.venue = venue; this.capacity = capacity;
        this.description = description;
    }
    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getEventDate() { return eventDate; }
    public String getVenue() { return venue; }
    public int getCapacity() { return capacity; }
    public String getDescription() { return description; }
}
