const weekdaysFormatter = new Intl.DateTimeFormat(undefined, {
  weekday: 'long',
});
const weekdays = [...Array(7).keys()]
  .map((weekdayIdx) => new Date(2021, 7, weekdayIdx + 1))
  .map((weekdayDate) => weekdaysFormatter.formatToParts(weekdayDate))
  .map((weekdayParts) => weekdayParts[0].value);

const weekdayInitialsFormatter = new Intl.DateTimeFormat(undefined, {
  weekday: 'narrow',
});
const weekdayInitials = [...Array(7).keys()]
  .map((weekdayIdx) => new Date(2021, 7, weekdayIdx + 1))
  .map((weekdayDate) => weekdayInitialsFormatter.formatToParts(weekdayDate))
  .map((weekdayParts) => weekdayParts[0].value);
