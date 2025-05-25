document.addEventListener('DOMContentLoaded', async () => {
    const allEventsListDiv = document.getElementById('allEventsList');

    async function fetchAllEvents() {
        // ... (fetch logic remains the same) ...
        try {
            const response = await fetch('/api/tasks/all');
            if (!response.ok) {
                console.error("Failed to fetch all events:", response.status, await response.text());
                allEventsListDiv.innerHTML = '<p class="no-events">Could not load events. Please try again later.</p>';
                return;
            }
            const events = await response.json();
            renderEvents(events);
        } catch (error) {
            console.error("Error fetching all events:", error);
            allEventsListDiv.innerHTML = '<p class="no-events">An error occurred while loading events.</p>';
        }
    }

    function renderEvents(events) {
        allEventsListDiv.innerHTML = '';

        if (!events || events.length === 0) {
            allEventsListDiv.innerHTML = '<p class="no-events">You have no events scheduled.</p>';
            return;
        }

        const ul = document.createElement('ul');
        ul.style.listStyleType = 'none';
        ul.style.padding = '0';

        events.forEach(event => {
            const li = document.createElement('li');
            li.classList.add('event-list-item');
            // Add border class based on event.color (which is like 'event-blue')
            if (event.color) {
                li.classList.add(`border-${event.color}`);
            } else {
                li.classList.add('border-event-grey');
            }


            // Optional: If you still want a color dot in addition to the left border
            // const colorIndicator = document.createElement('div');
            // colorIndicator.classList.add('event-color-indicator');
            // if (event.color) {
            //     colorIndicator.classList.add(event.color); // This class should define background-color
            // } else {
            //     colorIndicator.classList.add('event-grey');
            // }
            // li.appendChild(colorIndicator);


            const detailsDiv = document.createElement('div');
            detailsDiv.classList.add('event-list-details');

            const titleH4 = document.createElement('h4');
            titleH4.textContent = event.taskName;

            const dateP = document.createElement('p');
            const formattedDate = new Date(event.tdueDate + 'T00:00:00').toLocaleDateString(undefined, { // Ensure correct date parsing
                year: 'numeric', month: 'long', day: 'numeric'
            });
            dateP.innerHTML = `<i class="far fa-calendar-alt"></i> Due: ${formattedDate}`;

            detailsDiv.appendChild(titleH4);
            detailsDiv.appendChild(dateP);

            if (event.taskDescription && event.taskDescription.trim() !== "") {
                const descriptionP = document.createElement('p');
                descriptionP.classList.add('description'); // Use this class for styling
                descriptionP.innerHTML = `<i class="fas fa-info-circle"></i> ${event.taskDescription}`;
                detailsDiv.appendChild(descriptionP);
            }

            const statusP = document.createElement('p');
            const statusClass = event.isCompleted ? 'completed' : 'pending';
            const statusIcon = event.isCompleted ? 'fa-check-circle' : 'fa-clock';
            statusP.innerHTML = `<i class="fas ${statusIcon}"></i> Status: <span class="event-status ${statusClass}">${event.isCompleted ? 'Completed' : 'Pending'}</span>`;
            detailsDiv.appendChild(statusP);

            li.appendChild(detailsDiv); // Color indicator removed if using left border primarily
            ul.appendChild(li);
        });
        allEventsListDiv.appendChild(ul);
    }

    fetchAllEvents();
});