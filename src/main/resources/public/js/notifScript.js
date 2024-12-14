document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("modal");
    const modalContent = document.getElementById("full-text");
    const span = document.getElementsByClassName("close")[0];

    span.onclick = function () {
        modal.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

    document.querySelectorAll(".notification-list li").forEach(item => {
        item.addEventListener("click", function (event) {
            if (!event.target.closest(".checkbox-container")) {
                const fullText = this.getAttribute("data-fulltext");
                const uuid = this.getAttribute("data-uuid");
                modalContent.textContent = fullText;
                modal.style.display = "block";

                const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

                fetch('http://localhost:8080/api/notifications/' + uuid, {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-Token': csrfToken
                    }
                }).then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    item.classList.add('notification-read');
                    return response.json();
                }).then((hasUnreadNotifications) => {
                    if (!hasUnreadNotifications)
                        document.getElementById("notificationBadge").style.display = 'none'
                }).catch(error => {
                    console.error('There was a problem with your fetch operation:', error);
                });
            }
        });
    });

    document.getElementById("deleteSelected").addEventListener("click", function () {
        const selectedCheckboxes = document.querySelectorAll('.notification-checkbox:checked');
        const selectedIds = Array.from(selectedCheckboxes).map(checkbox => checkbox.getAttribute('data-uuid'));

        if (selectedIds.length > 0) {
            if (confirm('Удалить выбранные уведомления?')) {
                const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

                fetch('http://localhost:8080/api/notifications/delete', {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-Token': csrfToken
                    },
                    body: JSON.stringify(selectedIds)
                }).then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    window.location.reload();
                }).catch(error => {
                    console.error('There was a problem with your fetch operation:', error);
                });
            }
        } else {
            alert('Выберите хотя бы одно уведомление для удаления.');
        }
    });
});