const generateMonthsSizesList = (year) =>
  [...Array(12).keys()].map(
    (monthIdx) =>
      [...Array(31).keys()]
        .map((dayIdx) => new Date(year, monthIdx, dayIdx + 1))
        .filter((date) => date.getMonth() === monthIdx).length,
  );

const renderYear = (year, container) => {
  container.innerHTML = `<h1>${year}</h1>`;

  const monthNamesFormatter = new Intl.DateTimeFormat(undefined, {
    month: 'long',
  });
  const monthElements = [...Array(12).keys()]
    .map((monthIdx) => new Date(2021, monthIdx, 1))
    .map((monthDate) => monthNamesFormatter.formatToParts(monthDate))
    .map((monthDateParts) => monthDateParts[0].value)
    .map((month) => {
      const monthElement = document.createElement('div');
      monthElement.classList.add('month');
      monthElement.innerHTML = `<h2>${month}</h2><div></div>`;
      container.appendChild(monthElement);
      return monthElement;
    });

  const daysList = generateMonthsSizesList(year)
    .map((daysCount) =>
      [...Array(daysCount).keys()].map((dayIdx) => {
        const dayElement = document.createElement('div');
        dayElement.classList.add('day');
        dayElement.textContent = dayIdx + 1;
        return dayElement;
      }),
    )
    .flatMap((dayElements, monthIdx) => {
      const monthElement = monthElements[monthIdx].querySelector('div');

      weekdayInitials.forEach((initial) => {
        const element = document.createElement('div');
        element.classList.add('weekday-initial');
        element.textContent = initial;
        monthElement.appendChild(element);
      });

      const firstDay = new Date(year, monthIdx, 1);
      const startingDay = weekdaysFormatter.formatToParts(firstDay)[0].value;
      const startingIdx = weekdays.findIndex((day) => day === startingDay);
      dayElements[0].style.gridColumnStart = startingIdx;
      dayElements.forEach((dayElement) => {
        monthElement.appendChild(dayElement);
      });
      return dayElements;
    });

  return daysList;
};
