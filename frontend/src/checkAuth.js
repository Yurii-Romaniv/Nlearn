
export function checkAuth(response) {
    if (response.type === "opaque") {
        window.location.href = process.env.REACT_APP_SERVER_ENV + "/login";
    } else {
        return response.json();
    }
}