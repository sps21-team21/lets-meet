// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function redirectToCalendar() {
    window.location.replace(`calendar.html${window.location.search}`);
}

/** Creates and manages map */
function createMap() {
    const url = new URL(window.location.href);
    const user = url.searchParams.get('user');
    const event = url.searchParams.get('event');
    const params = new URLSearchParams({
        event: event,
        user: user
    });

    var coords = new google.maps.LatLng(37.422, -122.084);

    if (getLocation() != null) {
        var coords = Object.values(getLocation());
        coords = new google.maps.LatLng(coords[0], coords[1]);
    }

    const map = new google.maps.Map(
        document.getElementById('map'),
        {center: coords, zoom: 16});

    var pos_marker = new google.maps.Marker({
        position: coords,
        map: map,
        title: 'User location'
    });

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            userLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            map.setCenter(userLocation);
            pos_marker.setMap(null);
            pos_marker.setMap(map);
            pos_marker.position = userLocation;
            pos_marker.center = userLocation;

        });
    }

    google.maps.event.addListener(map, 'click', function(event){
        pos_marker.setMap(null);
        coords = new google.maps.LatLng(event.latLng.lat(), event.latLng.lng());
        pos_marker.setMap(map);
        pos_marker.position = coords;
        pos_marker.center = coords;
        saveLocation(event.latLng.lat(), event.latLng.lng());
    });
}

function saveLocation(lat, lng) {
    const params = new URLSearchParams();
    params.append('lat', lat);
    params.append('lng', lng);

    fetch('/translate', {
        method: 'POST',
        body: params
    });
}

async function getLocation() {
    const responseServ = await fetch(`/api/maps${window.location.search}`);
    const respObj = await responseServ.json();
    return respObj;
}
