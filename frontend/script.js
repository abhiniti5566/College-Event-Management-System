
const eventList = document.getElementById("event-list");

async function loadEvents() {
    eventList.textContent = "Loading events...";

    try {
        const response = await fetch("http://localhost:8080/api/events");

        if (!response.ok) {
            throw new Error("Unable to fetch events");
        }

        const events = await response.json();

        eventList.innerHTML = "";

        if (events.length === 0) {
            eventList.textContent = "No events available.";
            return;
        }

        events.forEach(event => {
            const card = document.createElement("div");
            card.className = "event-card";

            const title = document.createElement("h3");
            title.textContent = event.title;

            const category = document.createElement("p");
            category.textContent = "Category: " + event.category;

            const date = document.createElement("p");
            date.textContent = "Date: " + event.eventDate;

            const venue = document.createElement("p");
            venue.textContent = "Venue: " + event.venue;

            const capacity = document.createElement("p");
            capacity.textContent = "Capacity: " + event.capacity;

            const description = document.createElement("p");
            description.textContent = event.description || "";

            const button = document.createElement("button");
            button.textContent = "Register Now";

            button.addEventListener("click", () => {
                registerEvent(event.eventId, event.title);
            });

            card.append(title, category, date, venue,
                        capacity, description, button);

            eventList.appendChild(card);
        });

    } catch (error) {
        eventList.textContent =
            "Unable to load events. Please check whether the backend server is running.";
        console.error(error);
    }
}

async function registerEvent(eventId, eventName) {
    const rollNo = prompt("Enter your Roll Number:");
    if (!rollNo) return;

    const name = prompt("Enter your Name:");
    if (!name) return;

    const department = prompt("Enter your Department:");
    if (!department) return;

    const formData = new URLSearchParams();
    formData.append("rollNo", rollNo.trim());
    formData.append("name", name.trim());
    formData.append("department", department.trim());
    formData.append("eventId", eventId);

    try {
        const response = await fetch("http://localhost:8080/api/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: formData.toString()
        });

        const result = await response.json();

        if (response.ok) {
            alert("Successfully registered for " + eventName);
        } else {
            alert(result.error);
        }

    } catch (error) {
        alert("Registration failed. Please check the backend server.");
        console.error(error);
    }
}
const feedbackForm = document.getElementById("feedbackForm");

feedbackForm.addEventListener("submit", async function(event) {
    event.preventDefault();

    const registrationId =
        document.getElementById("registrationId").value;

    const rating =
        document.getElementById("rating").value;

    const comments =
        document.getElementById("comments").value;

    const formData = new URLSearchParams();
    formData.append("registrationId", registrationId);
    formData.append("rating", rating);
    formData.append("comments", comments);

    try {
        const response = await fetch(
            "http://localhost:8080/api/feedback",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: formData.toString()
            }
        );

        const result = await response.json();

        const message = document.getElementById("feedbackMessage");

        if (response.ok) {
            message.textContent = "Feedback submitted successfully!";
            feedbackForm.reset();
        } else {
            message.textContent = result.error;
        }

    } catch (error) {
        document.getElementById("feedbackMessage").textContent =
            "Unable to submit feedback. Check backend server.";
        console.error(error);
    }
});
loadEvents();
