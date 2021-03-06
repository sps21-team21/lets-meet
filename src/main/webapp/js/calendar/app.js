const backBtn = document.getElementById('back-btn');
backBtn.addEventListener('click', () => {
    // parse URL to get data
    const url = new URL(window.location.href);
    const user = url.searchParams.get('user');
    const event = url.searchParams.get('event');
    // Send user to calendar page
    const params = new URLSearchParams({
        event: event,
        user: user
    });
    window.location.replace("user.html?" + params.toString())
});

const year = new Date().getFullYear();
const container = document.getElementsByClassName('year')[0];
const daysList = renderYear(year, container);

const apiUrl = `/api/calendar${window.location.search}`;

fetch(apiUrl)
  .then((res) => res.json())
  .then((res) => {
    const daysDateList = generateMonthsSizesList(
      year,
    ).flatMap((monthSize, monthIdx) =>
      [...Array(monthSize).keys()].map((dayIdx) =>
        new Date(year, monthIdx, dayIdx + 1).getTime(),
      ),
    );
    res
      .filter((day) => daysDateList.includes(day))
      .map((day) => daysDateList.findIndex((d) => d === day))
      .forEach((dayIdx) => {
        daysList[dayIdx].classList.add('selected');
      });
  });

let selection = undefined;

daysList.forEach((dayElement, index) => {
  dayElement.addEventListener('mousedown', () => {
    selection = [index, dayElement.classList.contains('selected')];
    dayElement.classList.toggle('selected');
  });
  dayElement.addEventListener('mouseover', () => {
    if (selection) {
      const { low, high } =
        selection[0] > index
          ? { low: index, high: selection[0] }
          : { low: selection[0], high: index };
      if (selection[1]) {
        daysList
          .slice(low, high + 1)
          .forEach((dayElement) => dayElement.classList.remove('selected'));
      } else {
        daysList
          .slice(low, high + 1)
          .forEach((dayElement) => dayElement.classList.add('selected'));
      }
    }
  });
});

document.addEventListener('mouseup', () => {
  selection = undefined;
});

const saveBtn = document.getElementById('save-btn');

saveBtn.addEventListener('click', () => {
  const selectedDays = daysList
    .map((elem, idx) => [
      elem.classList.contains('selected'),
      new Date(year, 0, idx + 1),
    ])
    .filter(([isSelected]) => isSelected)
    .map(([_, date]) => date.getTime());

  fetch(apiUrl, {
    method: 'POST',
    body: JSON.stringify(selectedDays),
  }).then((res) => {
    if (res.ok) {
      alert('Calendar saved successfully!');
    } else {
      alert('Error while saving the calendar');
    }
    window.location.replace(`user.html${window.location.search}`);
  });
});
