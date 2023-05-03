export function checkAuth(response) {
    if (response.type === "opaque") {
        window.location.href = "http://localhost:8080/login";
    } else {
        return response.json();
    }
}