/** Fetches the user ID created from the server. */
async function showUserData() {
    // parse URL to get data
    const url = new URL(window.location.href);
    const user = url.searchParams.get('user');
    const room = url.searchParams.get('room');
    // Add userID to user page
    const userDataContainer = document.getElementById('user-ID-container');
    var headerUser = document.createElement('h2');
    var headerRoom = document.createElement('h2');
    headerUser.append("User ID: " + user);
    headerRoom.append("Room ID: " + room);
    userDataContainer.appendChild(headerUser);
    userDataContainer.appendChild(headerRoom);
}