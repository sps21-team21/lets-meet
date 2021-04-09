/*Copies text in container to clipboard*/
function CopyToClipboard(containerid) {
  if (document.selection) {
    var range = document.body.createTextRange();
    range.moveToElementText(document.getElementById(containerid));
    range.select().createTextRange();
    document.execCommand("copy");
  } else if (window.getSelection) {
    var range = document.createRange();
    range.selectNode(document.getElementById(containerid));
    window.getSelection().removeAllRanges();
    window.getSelection().addRange(range);
    document.execCommand("copy");
    window.getSelection().removeAllRanges();
  }
  // Disable button
  const button = document.querySelector('button');
  button.disabled = true;
}
/** Fetches the event ID created from the server. */
async function getRoom() {
    const responseFromServer = await fetch('/create-event');
    const eventData = await responseFromServer.json();
    // Add event to main page
    const eventIDContainer = document.getElementById('event-ID-container');
    const eventMessageContainer = document.getElementById('event-message-container');
    const buttonClipboardContainer = document.getElementById('button-clipboard-container');
    // Add info paragraph
    var paragraph = document.createElement('h3');
    paragraph.append("This is your new Event ID for you to share!")
    eventMessageContainer.appendChild(paragraph)
    // Add header with EventID
    var header = document.createElement('h4');
    header.id = 'header';
    header.append(eventData.id);
    eventIDContainer.appendChild(header);
    // Add button
    var button = document.createElement("BUTTON");
    button.id = "clipboard-button";
    button.onclick = CopyToClipboard('header');
    button.className = "clipboard-button";
    // Add button image
    var clipboardImage = document.createElement("img");
    clipboardImage.src = "media/clipboard.svg";
    button.appendChild(clipboardImage);
    buttonClipboardContainer.appendChild(button);
}