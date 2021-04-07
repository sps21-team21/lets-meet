/** Fetches the event ID created from the server. */
async function getRoom() {
    const responseFromServer = await fetch('/create-event');
    const eventData = await responseFromServer.json();
    // Add event to main page
    const eventIDContainer = document.getElementById('event-ID-container');
    const eventMessageContainer = document.getElementById('event-message-container');
    var header = document.createElement('h2');
    header.append(eventData.id);
    eventIDContainer.appendChild(header);
    var paragraph = document.createElement('p');
    paragraph.append("This is your new Event ID for you to share!")
    eventMessageContainer.appendChild(paragraph)
}