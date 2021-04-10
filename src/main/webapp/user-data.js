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

function loadResults() {
    const params = new URLSearchParams(window.location.search);
    const event = params.get('event');
    fetch(`/MatchingAlgoDate?text-input=${event}`)
        .then(res => res.json())
        .then(dates => {
            const dateFormatter = new Intl.DateTimeFormat(undefined, {
                weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'
            });
            const dateElems = dates
                .sort().map(date => new Date(date))
                .map(date => dateFormatter.format(date))
                .map(dateStr => `<li>${dateStr}</li>`);
            const datesContainer = document.getElementById('dates');
            if (dateElems.length === 0) {
                datesContainer.innerHTML += `<h4>It seems like there aren't any!</h4>`
            } else {
                datesContainer.innerHTML += `<ul>${dateElems.join('')}</ul>`;
            }
        });
}
