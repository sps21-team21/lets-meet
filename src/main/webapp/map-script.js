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

let pos_marker;

function redirectToCalendar() {
    const params = new URLSearchParams(window.location.search);
    params.append('lat', pos_marker.position.lat());
    params.append('lng', pos_marker.position.lng());

    fetch(`/api/maps?${params.toString()}`, {
        method: 'POST',
    }).then(() => {
        window.location.replace(`calendar.html${window.location.search}`);
    });
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

    let coords = new google.maps.LatLng(37.422, -122.084);

    if (getLocation() != null) {
        const new_coords = Object.values(getLocation());
        if(new_coords.length > 1){ 
            coords = new google.maps.LatLng(new_coords[0], new_coords[1]);
        }
    }

    const map = new google.maps.Map(
        document.getElementById('map'),
        {center: coords, zoom: 16}
    );

    pos_marker = new google.maps.Marker({
        position: coords,
        map: map,
        title: 'User location'
    });

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            let userLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
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
    });
}

async function getLocation() {
    const responseServ = await fetch(`/api/maps${window.location.search}`);
    return await responseServ.json();
}
