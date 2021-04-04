const year = new Date().getFullYear();
const container = document.getElementsByClassName('year')[0];
const daysList = renderYear(year, container);

const eventId = '--- event id ---';
const userId = '--- user id ---';

fetch(`/api/calendar?event=${eventId}&user=${userId}`)
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

  const requestBody = {
    event: eventId,
    user: userId,
    days: selectedDays,
  };

  fetch('/api/calendar', {
    method: 'POST',
    body: JSON.stringify(requestBody),
  }).then((res) => {
    if (res.ok) {
      alert('Calendar saved successfully!');
    } else {
      alert('Error while saving the calendar');
    }
  });
});
