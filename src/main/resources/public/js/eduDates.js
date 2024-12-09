document.addEventListener("DOMContentLoaded", function () {
    const yearFromInput = document.getElementById("year_from");
    const yearToInput = document.getElementById("year_to");

    yearFromInput.addEventListener("input", function () {
        const yearFromValue = parseInt(yearFromInput.value, 10);

        if (!isNaN(yearFromValue)) {
            // Устанавливаем минимальное значение для year_to
            yearToInput.min = yearFromValue;

            // Если текущее значение year_to меньше year_from, обновляем его
            if (parseInt(yearToInput.value, 10) < yearFromValue || isNaN(parseInt(yearToInput.value, 10))) {
                yearToInput.value = yearFromValue;
            }
        }
    });
});