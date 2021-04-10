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

function showData(data) {
    // Add containers to main page
    const IDContainer = document.getElementById(data.IDContainer);
    const messageContainer = document.getElementById(data.messageContainer);
    const buttonClipboardContainer = document.getElementById(data.clipboardContainer);
    // Add info paragraph
    var paragraph = document.createElement('h3');
    paragraph.append(data.infoText)
    messageContainer.appendChild(paragraph)
    // Add header with info to copy-paste
    var header = document.createElement('h4');
    header.id = data.headerID;
    header.append(data.textToCopy);
    IDContainer.appendChild(header);
    // Add button
    var button = document.createElement("BUTTON");
    button.id = data.clipboardContainer;
    button.onclick = CopyToClipboard(data.headerID);
    button.className = "clipboard-button";
    // Add button image
    var clipboardImage = document.createElement("img");
    clipboardImage.src = "media/clipboard.svg";
    button.appendChild(clipboardImage);
    buttonClipboardContainer.appendChild(button);
}

/** Fetches the event ID created from the server. */
async function getRoom() {
    const responseFromServer = await fetch('/create-event');
    const eventData = await responseFromServer.json();
    const eventIDInfo = new Object();
    eventIDInfo.IDContainer = 'event-ID-container';
    eventIDInfo.messageContainer = 'event-message-container';
    eventIDInfo.clipboardContainer = 'button-clipboard-container';
    eventIDInfo.infoText = "This is your new Event ID for you to share!";
    eventIDInfo.headerID = "header";
    eventIDInfo.textToCopy = eventData.id;
    const eventLinkInfo = new Object;
    eventLinkInfo.IDContainer = 'event-link-container';
    eventLinkInfo.messageContainer = 'link-message-container';
    eventLinkInfo.clipboardContainer = 'link-clipboard-container';
    eventLinkInfo.infoText = "Or you can share this link!";
    eventLinkInfo.headerID = "header-link";
    eventLinkInfo.textToCopy = `${window.location.hostname}/enter-room?event-id=${eventData.id}`;
    showData(eventIDInfo);
    showData(eventLinkInfo);
}