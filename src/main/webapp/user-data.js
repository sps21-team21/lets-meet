// function changeURL() {
//     var theURL = window.location.pathname;
//     return  theURL.replace("/url_part_to_change/", "/new_url_part/");
// }

/** Fetches the user data from URL and sends it to calendar page. */
function redirectToCalendarPage() {
    // parse URL to get data
    const url = new URL(window.location.href);
    const user = url.searchParams.get('user');
    const event = url.searchParams.get('event');
    // Send user to calendar page
    const params = new URLSearchParams({
        event: event,
        user: user
    });
    window.location.replace("calendar.html?" + params.toString())
}