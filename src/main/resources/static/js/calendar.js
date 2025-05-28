document.addEventListener('DOMContentLoaded', () => {
    const calendarGrid = document.getElementById('calendarGrid');
    const currentMonthYearDisplay = document.getElementById('currentMonthYear');
    const prevMonthBtn = document.getElementById('prevMonthBtn');
    const nextMonthBtn = document.getElementById('nextMonthBtn');

    const newEventBtn = document.getElementById('newEventBtn');
    const eventModal = document.getElementById('eventModal');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const eventForm = document.getElementById('eventForm');
    const eventDateInput = document.getElementById('eventDate');
    const eventDescriptionInput = document.getElementById('eventDescription'); // Get description input

    // For opening modal via query param
    const openModalParamInput = document.getElementById('openModalParam');

    const sidebarEventsListDiv = document.getElementById('sidebarUpcomingEventsList');

    let currentDate = new Date();
    let currentMonth = currentDate.getMonth(); // 0-11
    let currentYear = currentDate.getFullYear();

    let SourcedEvents = []; // Will hold events fetched from backend

    const monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    async function fetchEventsForMonth(year, month) {
        try {
            // month is 0-indexed JS, API expects 1-indexed
            const response = await fetch(`/api/tasks?year=${year}&month=${month + 1}`);
            if (!response.ok) {
                console.error("Failed to fetch events:", response.status, await response.text());
                return [];
            }
            const data = await response.json();
            // Adapt API response to { date: 'YYYY-MM-DD', title: 'Event', color: 'css-class' }
            return data.map(task => ({
                date: task.tdueDate, // API returns this as string YYYY-MM-DD
                title: task.taskName,
                color: task.color || 'event-grey' // Use color from API, or default
            }));
        } catch (error) {
            console.error("Error fetching events:", error);
            return [];
        }
    }

    async function renderCalendar(month, year) {
        calendarGrid.innerHTML = ''; // Clear previous grid content

        const dayHeaders = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
        dayHeaders.forEach(header => {
            const headerCell = document.createElement('div');
            headerCell.classList.add('calendar-day-header');
            headerCell.textContent = header;
            calendarGrid.appendChild(headerCell);
        });

        currentMonthYearDisplay.textContent = `${monthNames[month]} ${year}`;
        SourcedEvents = await fetchEventsForMonth(year, month);

        const firstDayOfMonth = new Date(year, month, 1).getDay(); // 0 (Sun) - 6 (Sat)
        const daysInMonth = new Date(year, month + 1, 0).getDate();

        for (let i = 0; i < firstDayOfMonth; i++) {
            const emptyCell = document.createElement('div');
            emptyCell.classList.add('calendar-day', 'other-month');
            calendarGrid.appendChild(emptyCell);
        }

        const today = new Date();
        for (let day = 1; day <= daysInMonth; day++) {
            const dayCell = document.createElement('div');
            dayCell.classList.add('calendar-day');
            const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;

            const dayNumberSpan = document.createElement('span');
            dayNumberSpan.classList.add('day-number');
            dayNumberSpan.textContent = day;
            dayCell.appendChild(dayNumberSpan);

            if (day === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
                dayCell.classList.add('today');
            }

            const dayEvents = SourcedEvents.filter(event => event.date === dateStr);
            dayEvents.forEach(event => {
                const eventDiv = document.createElement('div');
                eventDiv.classList.add('event-lozenge', event.color);
                eventDiv.textContent = event.title;
                dayCell.appendChild(eventDiv);
            });

            dayCell.addEventListener('click', () => {
                eventDateInput.value = dateStr; // Pre-fill date
                document.getElementById('eventTitle').value = ''; // Clear previous title
                eventDescriptionInput.value = ''; // Clear previous description
                document.getElementById('eventTime').value = ''; // Clear time
                eventModal.style.display = 'block';
            });
            calendarGrid.appendChild(dayCell);
        }
        // Add empty cells for days after the last of the month
        const totalCells = firstDayOfMonth + daysInMonth;
        const remainingCells = (7 - (totalCells % 7)) % 7;
        for (let i = 0; i < remainingCells; i++) {
            const emptyCell = document.createElement('div');
            emptyCell.classList.add('calendar-day', 'other-month');
            calendarGrid.appendChild(emptyCell);
        }
    }

    newEventBtn.onclick = function() {
        eventForm.reset(); // Clear all form fields
        eventDateInput.valueAsDate = new Date(); // Default date to today
        eventModal.style.display = "block";
    }
    closeModalBtn.onclick = function() {
        eventModal.style.display = "none";
        eventForm.reset();
    }
    window.onclick = function(event) {
        if (event.target == eventModal) {
            eventModal.style.display = "none";
            eventForm.reset();
        }
    }

    eventForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const title = document.getElementById('eventTitle').value;
        const date = eventDateInput.value;
        const time = document.getElementById('eventTime').value;
        const color = document.getElementById('eventColor').value; // This is the CSS class like "event-blue"
        const description = eventDescriptionInput.value;


        if (title && date) {
            const eventData = {
                eventTitle: title,
                eventDate: date,
                eventTime: time,
                eventColor: color, // Sending the UI color class, backend TaskDTO will map this
                eventDescription: description
            };

            try {
                const response = await fetch('/api/tasks', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        // Spring Security CSRF might be needed if not disabled globally for APIs
                    },
                    body: JSON.stringify(eventData)
                });

                if (response.ok) {
                    await response.json(); // Process the created task if needed
                    renderCalendar(currentMonth, currentYear); // Re-render to show new event
                    eventModal.style.display = "none";
                    eventForm.reset();
                } else {
                    const errorText = await response.text();
                    alert("Error adding event: " + errorText);
                    console.error("Failed to add event:", response.status, errorText);
                }
            } catch (error) {
                console.error("Network or other error submitting event:", error);
                alert("An error occurred. Please try again.");
            }
        } else {
            alert("Please fill in the event title and date.");
        }
    });

    prevMonthBtn.addEventListener('click', () => {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        renderCalendar(currentMonth, currentYear);
    });

    nextMonthBtn.addEventListener('click', () => {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        renderCalendar(currentMonth, currentYear);
    });

    // Check if modal should be opened based on query param (from Thymeleaf)
    if (openModalParamInput && openModalParamInput.value === 'true') {
        newEventBtn.click(); // Open modal if param is true
    }

     async function loadSidebarEvents() {
        if (!sidebarEventsListDiv) return; // Only run if the sidebar element exists

        try {
            const response = await fetch('/api/tasks/all'); // Fetch all tasks
            if (!response.ok) {
                console.error("Sidebar: Failed to fetch events:", response.status);
                sidebarEventsListDiv.innerHTML = '<p style="text-align:center; color:var(--text-light);">Could not load.</p>';
                return;
            }
            let allEvents = await response.json();

            // Filter for upcoming/recent events (e.g., today onwards, or last few + next few)
            // For simplicity, we'll take the first few tasks (which are ordered by due date from API)
            // A more robust solution would filter by date more precisely.
            const todayStr = new Date().toISOString().split('T')[0];
            
            // Filter for events from today onwards, then sort by date, then take top N
            let upcomingEvents = allEvents
                .filter(event => event.tdueDate >= todayStr && !event.isCompleted) // Get pending events from today onwards
                .sort((a, b) => new Date(a.tdueDate) - new Date(b.tdueDate)) // Ensure ascending order
                .slice(0, 4); // Show top 4 upcoming pending events

            // If not enough upcoming pending, fill with most recent completed or past pending events
            if (upcomingEvents.length < 4) {
                const needed = 4 - upcomingEvents.length;
                let otherEvents = allEvents
                    .filter(event => !upcomingEvents.find(ue => ue.id === event.id)) // Exclude already selected
                    .sort((a,b) => new Date(b.tdueDate) - new Date(a.tdueDate)) // Sort recent first
                    .slice(0, needed);
                upcomingEvents = upcomingEvents.concat(otherEvents).sort((a,b) => new Date(a.tdueDate) - new Date(b.tdueDate));
            }


            sidebarEventsListDiv.innerHTML = ''; // Clear loading message

            if (!upcomingEvents || upcomingEvents.length === 0) {
                sidebarEventsListDiv.innerHTML = '<p style="text-align:center; color:var(--text-light); padding:10px 0;">No upcoming events.</p>';
                return;
            }

            upcomingEvents.forEach(event => {
                const eventItemDiv = document.createElement('div');
                eventItemDiv.classList.add('event-item'); // Re-use existing .event-item styles

                const eventDotSpan = document.createElement('span');
                eventDotSpan.classList.add('event-dot');
                if (event.color) { // event.color is like 'event-blue'
                    // We need to map this to the dot color classes (e.g., team-standup for blue)
                    // This mapping can be improved or made more direct if TaskDTO provided a dotClass
                    let dotClass = 'default-dot'; // Add a .default-dot style
                    if (event.color === 'event-blue') dotClass = 'team-standup';
                    else if (event.color === 'event-purple') dotClass = 'project-review';
                    else if (event.color === 'event-orange') dotClass = 'client-presentation';
                    else if (event.color === 'event-green') dotClass = 'gym-session';
                    eventDotSpan.classList.add(dotClass);
                }


                const eventDetailsDiv = document.createElement('div');

                const titleP = document.createElement('p');
                titleP.classList.add('event-title');
                titleP.textContent = event.taskName.length > 25 ? event.taskName.substring(0, 25) + '...' : event.taskName;

                const dateDetailsP = document.createElement('p');
                dateDetailsP.classList.add('event-details');
                const eventDate = new Date(event.tdueDate + 'T00:00:00');
                const formattedTime = eventDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: true });
                // Try to extract time from title if present (e.g. "Event (09:00 AM)")
                let displayTime = formattedTime; // Default to T00:00 if not in title
                const timeMatch = event.taskName.match(/\((\d{1,2}:\d{2}\s*(?:AM|PM)?)\)/i);
                if (timeMatch && timeMatch[1]) {
                    displayTime = timeMatch[1];
                }


                let dateTag = '';
                const eventDateOnly = new Date(event.tdueDate);
                const today = new Date();
                today.setHours(0,0,0,0); // Normalize today for date comparison
                eventDateOnly.setHours(0,0,0,0); // Normalize eventDate for date comparison

                if (eventDateOnly.getTime() === today.getTime()) {
                    dateTag = '<span class="event-tag today">Today</span>';
                } else if (eventDateOnly.getTime() === new Date(today.getTime() + 24 * 60 * 60 * 1000).getTime()) {
                    dateTag = '<span class="event-tag tomorrow">Tomorrow</span>';
                } else {
                     dateTag = `<span class="event-tag other-day">${eventDate.toLocaleDateString(undefined, { month: 'short', day: 'numeric' })}</span>`;
                }


                dateDetailsP.innerHTML = `<i class="far fa-clock"></i> ${displayTime} ${dateTag}`;
                // Add location/virtual if available in your Task model/DTO
                // const locationDetailsP = document.createElement('p');
                // locationDetailsP.classList.add('event-details');
                // locationDetailsP.innerHTML = `<i class="fas fa-map-marker-alt"></i> Conference Room A`;

                eventDetailsDiv.appendChild(titleP);
                eventDetailsDiv.appendChild(dateDetailsP);
                // eventDetailsDiv.appendChild(locationDetailsP);

                eventItemDiv.appendChild(eventDotSpan);
                eventItemDiv.appendChild(eventDetailsDiv);
                sidebarEventsListDiv.appendChild(eventItemDiv);
            });

        } catch (error) {
            console.error("Sidebar: Error fetching events:", error);
            if(sidebarEventsListDiv) sidebarEventsListDiv.innerHTML = '<p style="text-align:center; color:var(--text-light);">Error loading.</p>';
        }
    }

    // Initial Render of Calendar AND Sidebar
    renderCalendar(currentMonth, currentYear);
    loadSidebarEvents(); // Call this also on initial load

});