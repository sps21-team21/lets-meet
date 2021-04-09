function getUserData() {
    // parse URL to get data
    const url = new URL(window.location.href);
    const user = url.searchParams.get('user');
    const event = url.searchParams.get('event');
    // Send user to calendar page
    const params = new URLSearchParams({
        event: event,
        user: user
    });
    return params.toString();
}

/** Fetches the user data from URL and sends it to calendar page. */
function redirectToCalendarPage() {
    window.location.replace("calendar.html?" + getUserData());
}

/** Fetches the user data from URL and sends it to location page. */
function redirectToLocationPage() {
    window.location.replace("choose-loc.html?" + getUserData());
}