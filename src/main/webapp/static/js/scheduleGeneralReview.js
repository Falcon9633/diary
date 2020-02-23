moment.updateLocale('uk', {
    week: {
        dow: 1,
    },
});
moment.locale('uk');

const SCHEDULE_GENERAL_REVIEW_TEMPLATE =
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
const $CALENDAR = $('#scheduleGeneralReview');
const $INPUT_DATE_PICKER = $('#date-picker');
const $SELECT = $('#select');
const $CONTENT_CONTAINER = $('#scheduleGeneralReviewContent');
const SCHEDULE_DAYS_PERIOD = 7;
const MIN_LESSON_NR = 0;
const MAX_LESSON_NR = 8;

let schedules;
let pathname = window.location.pathname;
let $selectedObjectId;
let availableDays = [];

function init() {
    $DATE_HOLDER.hide();
    $CALENDAR.hide();

    $SELECT.on('change', handleBandSelectChange);
    calendarVisibilityHandler();
    initCalendar();
}

function getScheduleWeekBySelectedDay(millis) {
    if (pathname.includes('student/schedule')){
        $.getJSON("/student/getScheduleWeekBySelectedDay",{selectedDateMs : millis}, function (json) {
            schedules = json;
        })
    }

    if (pathname.includes('teacher/schedule')){
        $.getJSON("/teacher/getScheduleWeekBySelectedDay",{selectedDateMs : millis}, function (json) {
            schedules = json;
        })
    }
}

function handleBandSelectChange() {
    $selectedObjectId = $(this).val();

    emptyContentContainer();

    if ($selectedObjectId === ''){
        $DATE_HOLDER.hide();
        $CONTENT_CONTAINER.hide();
    } else {
        $DATE_HOLDER.show();
        $CONTENT_CONTAINER.show();
        $selectedObjectId = parseInt($selectedObjectId);
    }
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

function initCalendar() {

    $CALENDAR.clndr({
        moment: moment,
        template: SCHEDULE_GENERAL_REVIEW_TEMPLATE,
        constraints: {
            startDate: getCalendarStartDate(),
            endDate: getCalendarEndDate()
        },

        doneRendering: function () {
            $CALENDAR.on('click', 'div.week', handleWeekSelection);
        },

        clickEvents : {
            click : function (target) {
                contentInit(target.date);
            }
        }
    });
}

function getCalendarStartDate() {
    if (moment().month()>=9){
        return moment().year() + "-09-01";
    }
    return moment().year() - 1 + "-09-01";
}

function getCalendarEndDate() {
    if (moment().month()>=9){
        return moment().year() + 1 + "-05-31";
    }
    return moment().year() + "-05-31";
}

function handleWeekSelection() {
    $('div.days div.week.selected').removeClass('selected');

    $(this).addClass('selected');
}

function contentInit(moment) {
    getScheduleWeekBySelectedDay(moment.valueOf());
    changeInputDatePickerTitle(moment);
    emptyContentContainer();
    getAvailableDays(moment);
    $(document).one('ajaxStop', createContent);
}

function changeInputDatePickerTitle(moment) {
    $INPUT_DATE_PICKER.val(getMonday(moment).toLocaleDateString(undefined, {year: 'numeric', month: '2-digit', day: '2-digit'}) + ' - ' +
        getFriday(moment).toLocaleDateString(undefined, {year: 'numeric', month: '2-digit', day: '2-digit'}));
}

function emptyContentContainer() {
    $CONTENT_CONTAINER.empty();
}

function getAvailableDays(moment) {
    availableDays = [];
    let monday = getMonday(moment);
    for (let currentDayOfWeek = 0; currentDayOfWeek < SCHEDULE_DAYS_PERIOD; currentDayOfWeek++) {
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
    let $table = $('<table>').addClass('bordered scheduleReview');
    if (pathname.includes('scheduleTeachersGeneralReview')){
        return $table.append(createTableHeader(date), createTableBody(date, findTeacherCurrentLessonSchedule, generateTeacherTD));
    }
    return $table.append(createTableHeader(date), createTableBody(date, findBandCurrentLessonSchedule, generateBandTD));
}

function createTableHeader(date) {
    let $thead = $('<thead>').append();
    let $tr = $('<tr>');
    let $th = $('<th>');

    $th.text(getDayName(date)).appendTo($tr);
    return $thead.append($tr)
}

function createTableBody(date, findCurrentLessonCallback, generateTDCallback) {
    let $tbody = $('<tbody>');

    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let $tr = $('<tr>');
        let $td = $('<td>');

        let currentLessonSchedule = findCurrentLessonCallback(date, lessonNr);

        if (currentLessonSchedule && currentLessonSchedule.subject) {
            $td = generateTDCallback(lessonNr, currentLessonSchedule);
        } else {
            $td.text(lessonNr + '. ');
        }

        $tr.append($td).appendTo($tbody);
    }

    return $tbody;
}

function findBandCurrentLessonSchedule(date, lessonNr) {
    return schedules.find(function (element) {
        let scheduleDate = new Date(element.calendar);
        return date.getDate() === scheduleDate.getDate() && element.numberOfLesson === lessonNr && element.band.id === $selectedObjectId;
    });
}

function findTeacherCurrentLessonSchedule(date, lessonNr) {
    return schedules.find(function (element) {
        if (element.teacher === null){
            return;
        }
        let scheduleDate = new Date(element.calendar);
        return date.getDate() === scheduleDate.getDate() && element.numberOfLesson === lessonNr && element.teacher.id === $selectedObjectId;
    });
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