function exportData() {
    const currentType = document.getElementById("educationId").innerText;
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    console.log(currentType)
    fetch('/report/educationPlan?eduPlan=' + currentType, {
        method: 'GET',
        headers: {
            'X-CSRF-Token': csrfToken
        }
    }).then(response => {
        if (response.ok) {
            console.log(response)
            return response.blob();
        } else {
            throw new Error('Ошибка при выгрузке данных!');
        }
    }).then(async blob => {
        const url = window.URL.createObjectURL(await blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = currentType.slice(1) + '-data.csv';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
    }).catch(error => {
        alert(error.message);
    });
}