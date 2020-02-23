moment.updateLocale('uk', {
    week: {
        dow: 1,
    },
});
moment.locale('uk');

const DIARY_CALENDAR_TEMPLATE =
    "<div class='clndr-controls'>" +
    "<div class='clndr-previous-button'>&lsaquo;</div>" +
    "<div class='month'><%= month %> <%= year %></div>" +
    "<div class='clndr-next-button'>&rsaquo;</div>" +
    "</div>" +
    "<div class='clndr-grid'>" +
    "<div class='days-of-the-week'>" +
    "<% _.each(daysOfTheWeek, function(day) { %>" +
    "<div class='header-day'><%= day %></div>" +
    "<% }); %>" +
    "</div>" +
    "<div class='days'>" +
    "<% for(var i = 0; i < numberOfRows; i++){ %>" +
    "<div class='week'>" +
    "<% for(var j = 0; j < 7; j++){ %>" +
    "<% var d = j + i * 7; %>" +
    "<div class='<%= days[d].classes %>'><%= days[d].day %></div>" +
    "<% } %>" +
    "</div>" +
    "<% } %>" +
    "</div>" +
    "</div>";

const $DATE_HOLDER = $('.date-holder');
const $CALENDAR = $('#diaryCalendar');
const $INPUT_DATE_PICKER = $('#date-picker');
const $CONTENT_CONTAINER = $('#diaryContent');
const DIARY_DAYS_PERIOD = 7;
const TODAY = moment(new Date(), 'DD/MM/YYYY', true);
const MIN_LESSON_NR = 0;
const MAX_LESSON_NR = 8;

let availableDays = [];
let student,
    subjects,
    notes;
let schedules,
    lessons;

function init() {
    $CALENDAR.hide();
    calendarVisibilityHandler();
    loadData();
    $(document).ajaxStop(initCalendar);
}

function calendarVisibilityHandler() {
    $DATE_HOLDER.hover(
        function () {
            $CALENDAR.show();
        },
        function () {
            $CALENDAR.hide();
        });
}

function loadData() {
    $.getJSON("/student/getAuthenticatedUser", function (json) {
        student = json;
    });

    $.getJSON("/student/getAllSubjects", function (json) {
        subjects = json;
    });

    $.getJSON("/student/getAllStudentsNotes", function (json) {
        notes = json;
    })
}

function initCalendar() {

    $CALENDAR.clndr({
        moment: moment,
        template: DIARY_CALENDAR_TEMPLATE,
        constraints: {
            startDate: getCalendarStartDate(),
            endDate: getCalendarEndDate()
        },

        doneRendering: function () {
            $CALENDAR.on('click', 'div.week', handleWeekSelection);
            contentInit(TODAY);
        },

        clickEvents: {
            click: function (target) {
                contentInit(target.date);
            }
        }
    });
}

function getCalendarStartDate() {
    if (moment().month() >= 9) {
        return moment().year() + "-09-01";
    }
    return moment().year() - 1 + "-09-01";
}

function getCalendarEndDate() {
    if (moment().month() >= 9) {
        return moment().year() + 1 + "-05-31";
    }
    return moment().year() + "-05-31";
}

function handleWeekSelection() {
    $('div.days div.week.selected').removeClass('selected');

    $(this).addClass('selected');
}

function contentInit(moment) {
    getSchedulesBySelectedDayAndBand(moment.valueOf(), student.band.id);
    getLessonsBySelectedDayAndBand(moment.valueOf(), student.band.id);
    changeInputDatePickerTitle(moment);
    emptyContentContainer();
    getAvailableDays(moment);
    $(document).one('ajaxStop', createContent);
}

function getSchedulesBySelectedDayAndBand(millis, bandId) {
    $.getJSON("/student/getWeeksScheduleBySelectedDayAndBand", {
        selectedDateMs: millis,
        bandId: bandId
    }, function (json) {
        schedules = json;
    })
}

function getLessonsBySelectedDayAndBand(millis, bandId) {
    $.getJSON("/student/getWeeksLessonsBySelectedDayAndBand", {
        selectedDateMs: millis,
        bandId: bandId
    }, function (json) {
        lessons = json;
    })
}

function changeInputDatePickerTitle(moment) {
    $INPUT_DATE_PICKER.val(getMonday(moment).toLocaleDateString(undefined, {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit'
        }) + ' - ' +
        getFriday(moment).toLocaleDateString(undefined, {year: 'numeric', month: '2-digit', day: '2-digit'}));
}

function emptyContentContainer() {
    $CONTENT_CONTAINER.empty();
}

function getAvailableDays(moment) {
    availableDays = [];
    let monday = getMonday(moment);
    for (let currentDayOfWeek = 0; currentDayOfWeek < DIARY_DAYS_PERIOD; currentDayOfWeek++) {
        let day = new Date(monday.getFullYear(), monday.getMonth(), monday.getDate() + currentDayOfWeek);
        if (day.getDay() !== 0) {  // expel if sunday
            availableDays.push(day)
        }
    }
}

function getMonday(moment) {
    let date = moment.toDate();
    let day = date.getDay(),
        diff = date.getDate() - day + (day === 0 ? -6 : 1); // adjust when day is sunday
    return new Date(date.setDate(diff));
}

function getFriday(moment) {
    let date = moment.toDate();
    let day = date.getDay(),
        diff = date.getDate() - day + (day === 0 ? -1 : 6); // adjust when day is sunday
    return new Date(date.setDate(diff));
}

function createContent() {
    availableDays.forEach(function (element) {
        $CONTENT_CONTAINER.append(createTable(element));
    })
}

function createTable(date) {
    let $table = $('<table>').addClass('bordered diaryTable');

    return $table.append(createTableHeader(date), createTableBody(date));
}

function createTableHeader(date) {
    let $thead = $('<thead>').append();
    let $tr = $('<tr>');
    let $th = $('<th>').attr('colspan', 4);

    $th.text(getDayName(date)).appendTo($tr);
    return $thead.append($tr);
}

function createTableBody(date) {
    let $tbody = $('<tbody>');

    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let $tr = $('<tr>');
        let currentSchedule = findCurrentSchedule(date, lessonNr);
        let currentNote = findCurrentNote(currentSchedule);
        let $lessonNr = $('<td>').text(lessonNr + '. ');
        let $subject = generateSubjectTD(currentSchedule);
        let $homework = generateHomeworkTD(currentSchedule);
        let $mark = generateMarkTD(currentNote);

        $tr.append($lessonNr, $subject, $homework, $mark).appendTo($tbody);
    }

    return $tbody;
}

function findCurrentSchedule(date, lessonNr) {
    return schedules.find(function (element) {
        let scheduleDate = new Date(element.calendar);
        return date.getDate() === scheduleDate.getDate() && element.numberOfLesson === lessonNr;
    });
}

function findCurrentNote(schedule) {
    return notes.find(function (element) {
        return element.lesson.id === schedule.lesson.id;
    })
}

function generateSubjectTD(schedule) {
    let subject = subjects.find(function (element) {
        if (schedule.subject){
            return element.id === schedule.subject.id;
        }
    });

    if (subject){
        return $('<td>').text(subject.name);
    } else {
        return $('<td>');
    }
}

function generateHomeworkTD(currentSchedule) {
    let lesson = lessons.find(function (element) {
        return element.id === currentSchedule.lesson.id;
    });

    if (lesson){
        return $('<td>').text(lesson.homework);
    } else {
        return $('<td>');
    }
}

function generateMarkTD(note) {
    if (note){
        if (note.absent) {
            return $('<td>').text('H');
        } else {
            return $('<td>').text(note.mark);
        }
    } else {
        return $('<td>');
    }
}

function getDayName(date) {
    let options = {weekday: 'long', year: 'numeric', month: '2-digit', day: '2-digit'};
    return date.toLocaleDateString(undefined, options);
}

$(document).ready(init);