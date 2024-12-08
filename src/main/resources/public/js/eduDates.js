document.addEventListener("DOMContentLoaded", function () {
    const yearFromInput = document.getElementById("year_from");
    const yearToInput = document.getElementById("year_to");

    yearFromInput.addEventListener("input", function () {
        const yearFromValue = parseInt(yearFromInput.value, 10);

        if (!isNaN(yearFromValue)) {
            yearToInput.min = yearFromValue;

            yearToInput.value = yearFromValue;
        }
    });
});