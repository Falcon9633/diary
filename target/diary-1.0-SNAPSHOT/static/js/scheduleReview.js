const SCHEDULE_DAYS_PERIOD = 14;
const SCHEDULE_REVIEW_CONTAINER = $('#scheduleReview');
const MIN_LESSON_NR = 0;
const MAX_LESSON_NR = 8;
const TODAY = new Date();

let pathname = window.location.pathname;
let schedules;
let availableDays = [];

function init() {
    loadData();
    $(document).ajaxStop(createContent);
}

function loadData() {
    getSchedules(pathname);

    getAvailableDays();
}

function getSchedules(pathname) {
    if (pathname.includes('teacher')) {
        $.getJSON("/teacher/getSchedulesReviewCurrentWeek", function (json) {
            schedules = json;
        });
    }

    if (pathname.includes('student')) {
        $.getJSON("/student/getSchedulesReviewCurrentWeek", function (json) {
            schedules = json;
        })
    }
}

function getMonday(date) {
    let day = date.getDay(),
        diff = date.getDate() - day + (day === 0 ? -6 : 1); // adjust when day is sunday
    return new Date(date.setDate(diff));
}

function getAvailableDays() {
    let monday = getMonday(new Date());
    for (let currentDayOfWeek = 0; currentDayOfWeek < SCHEDULE_DAYS_PERIOD; currentDayOfWeek++) {
        let day = new Date(monday.getFullYear(), monday.getMonth(), monday.getDate() + currentDayOfWeek);
        if (day.getDay() !== 0) {  // expel if sunday
            availableDays.push(day)
        }
    }
}

function createContent() {
    availableDays.forEach(function (element) {
        SCHEDULE_REVIEW_CONTAINER.append(createTable(element));
    })
}

function createTable(date) {
    let $table = $('<table>').addClass('bordered scheduleReview');

    return $table.append(createTableHeader(date), pathname.includes('/teacher/') ? createTableBody(date, generateTeacherTD) : createTableBody(date, generateBandTD));
}

function createTableHeader(date) {
    let $thead = $('<thead>').append();
    let $tr = $('<tr>');
    let $th = $('<th>');

    if (date.getDate() === TODAY.getDate()) {
        $th.addClass('today')
    }
    $th.text(getDayName(date)).appendTo($tr);
    return $thead.append($tr)
}

function createTableBody(date, callback) {
    let $tbody = $('<tbody>');

    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let $tr = $('<tr>');
        let $td = $('<td>');

        let currentLessonSchedule = schedules.find(function (element) {
            let scheduleDate = new Date(element.calendar);
            return date.getDate() === scheduleDate.getDate() && element.numberOfLesson === lessonNr;
        });

        if (currentLessonSchedule && currentLessonSchedule.subject) {
            $td = callback(lessonNr, currentLessonSchedule);
        } else {
            $td.text(lessonNr + '. ');
        }

        $tr.append($td).appendTo($tbody);
    }

    return $tbody;
}

function generateTeacherTD(lessonNr, currentLessonSchedule) {
    return $('<td>').text(lessonNr + '. ' + currentLessonSchedule.subject.name + ' (' + currentLessonSchedule.band.name + ')');
}

function generateBandTD(lessonNr, currentLessonSchedule) {
    return $('<td>').text(lessonNr + '. ' + currentLessonSchedule.subject.name + ' (' + currentLessonSchedule.teacher.surname + ')');
}

function getDayName(date) {
    let options = {weekday: 'long', year: 'numeric', month: '2-digit', day: '2-digit'};
    return date.toLocaleDateString(undefined, options);
}

$(document).ready(init);