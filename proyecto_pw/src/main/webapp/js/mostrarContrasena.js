// register.js
function togglePasswordVisibility() {
    const passwordField = document.getElementById("password");
    const toggleCheckbox = document.getElementById("showPassword");
    if (toggleCheckbox.checked) {
        passwordField.type = "text"; // Mostrar contraseña
    } else {
        passwordField.type = "password"; // Ocultar contraseña
    }
}
