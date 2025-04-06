const handleCellClick = (e , cellId) => {
    const cell = document.getElementById(cellId);
    const dialogForm= `
    <dialog id="dialog-${cellId}" class="dialog container">
    <form id="dialog-form-${cellId}" class="dialog-form" method="POST">
    <label for="taskName">Task Name:</label>
    <input type="text" id="taskName" name="taskName" required>
    <label for="TdueDate">Task End Date:</label>
    <input type="date" id="TdueDate" name="TdueDate" required>
    <label for="task-description">Task Description:</label>
    <textarea id="task-description" name="taskDescription" rows="4" required></textarea>
    <button type="submit" class="btn btn-primary">Create Task</button>
    </dialog>
`;
cell.insertAdjacentHTML('beforeend', dialogForm);
    const dialog = document.getElementById(`dialog-${cellId}`);
    dialog.showModal();

    const form = document.getElementById(`dialog-form-${cellId}`);
    form.onsubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(form);
        try {
            const response = await fetch('/calendar/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(Object.fromEntries(formData))
            });
            if (response.ok) {
                dialog.close();
                cell.removeChild(dialog);
            }
        } catch (error) {
            console.error('Error:', error);
            dialog.close();
        }
    };
    
    const closeBtn = document.createElement('button');
    closeBtn.textContent = 'Close';
    closeBtn.onclick = () => {
        dialog.close();
        cell.removeChild(dialog);
    };
    
}

document.addEventListener("DOMContentLoaded", function() {
    const tbody = document.querySelector('#calendar-body');
    tbody.innerHTML = ''; // clear content
    
    let currentRow = null;
        for (let i = 0; i < 30; i++) {
        if (i % 7 === 0) {
            currentRow = document.createElement('tr');
            tbody.appendChild(currentRow);
        }
       
        const cell = document.createElement('td');
        cell.className = 'calendar-cell';
        cell.id = `cell-${i + 1}`;
        cell.textContent = i + 1;
        cell.addEventListener('click', (e) => handleCellClick(e, cell.id));
        cell.style.cursor = 'pointer';
        currentRow.appendChild(cell);
    }
});

/**
 *  cell.addEventListener('click', (e) => {
            const modal = document.createElement('div');
            modal.className = 'modal';
            modal.innerHTML = `
                <div class="modal-content">
                    <span class="close">&times;</span>
                    ${modalForm}
                    <button type="submit">Submit</button>
                </div>
            `;
            document.body.appendChild(modal);

            const closeBtn = modal.querySelector('.close');
            closeBtn.onclick = () => modal.remove();

            modal.querySelector('form').onsubmit = async (e) => {
                e.preventDefault();
                const formData = new FormData(e.target);
                try {
                    const response = await fetch('/api/tasks', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(Object.fromEntries(formData))
                    });
                    if (response.ok) {
                        modal.remove();
                    }
                } catch (error) {
                    console.error('Error:', error);
                }
            };
        });
 */