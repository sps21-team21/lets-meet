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


    //now for location
    const geocoder = new google.maps.Geocoder();
    //issue at fetch
    fetch(`/MatchingAlgoLocation?text-input=${event}`)
        .then(res => res.json())
        .then(LatsLangs => {
            if(LatsLangs.length == 0){
                const mapPlace = document.getElementById("locationtext");
                mapPlace.innerHTML += "There are no locations!";
            }
            else{
                posit = { lat: parseFloat(LatsLangs[0]), lng: parseFloat(LatsLangs[1]) };
                geocoder.geocode({location : posit}, (results, status) => {
                    if (status == "OK") {
                        var formatted_address = results[0].formatted_address;
                        const mapPlace = document.getElementById("locationtext");
                        mapPlace.innerHTML += "Suggested meet up location is " + formatted_address;
                    } else {
                        window.alert("Geocoder failed due to: " + status);
                    }
                })
            }
        });



function geocodeLatLng(geocoder, posit) {
  geocoder.geocode({ location: posit }, (results, status) => {
    if (status == "OK") {
      if (results[0] != null) {
        return results[0].formatted_address;
      } 
      else {
        return "Error Loading Location" ;
      }
    } 
    else {
        return "Error Loading Location";
    }
  });
}
