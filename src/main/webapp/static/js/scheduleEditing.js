/*<![CDATA[*/

moment.updateLocale('uk', {
    week: {
        dow: 0,
    }
});
moment.locale('uk');

const MIN_LESSON_NR = 0;
const MAX_LESSON_NR = 8;
const TODAY = moment(new Date(), 'DD/MM/YYYY', true);

var $subjectsForm;
var $teachersForm;

var currentBandSchedule;
var bands;
var subjects;
var teachers;
var $selectedBandId;
var selectedDate;

function init() {
    loadData();
    setWOYandDOWinputsValue(TODAY);

    let $classSelectContainer = $('#class-select-container');

    $classSelectContainer.on('change', 'select[name=bandId]', handleBandSelectChange);
    $('form').submit(handleSubmitForm);

    $(document).ajaxComplete(function () {
        $('#calendar').clndr({
            moment: moment,
            adjacentDaysChangeMonth: true,
            forceSixRows: true,
            trackSelectedDate: true,

            doneRendering: function () {
                createEventForm();
            },

            clickEvents: {
                onMonthChange: function (month) {
                    changeEventListingTitle(selectedDate);
                    emptySubjectTeacherForm();
                },

                click: function (target) {
                    selectedDate = target.date;
                    changeEventListingTitle(target.date);
                    if (!$selectedBandId){
                        let $errorElement = $('#bandSelectError');
                        $errorElement.show();
                        alert($errorElement.text());
                        return;
                    }
                    emptySubjectTeacherForm();
                    getBandScheduleByDate($selectedBandId, target.date);
                    setWOYandDOWinputsValue(target.date);
                }
            }
        });
    })
}

function loadData() {
    $.getJSON("/getAllBands", function (json) {
        bands = json;
    });

    $.getJSON("/getAllSubjects", function (json) {
        subjects = json;
    });

    $.getJSON("/getAllTeachers", function (json) {
        teachers = json;
    });
}

function setWOYandDOWinputsValue(moment) {
    $('input[name=weekOfYear]').val(moment.week());
    $('input[name=dayOfWeek]').val(moment.weekday() + 1)
}

function handleSubmitForm(event){
    if (!$selectedBandId){
        let $errorElement = $('#bandSelectError');
        $errorElement.show();
        alert($errorElement.text());
        event.preventDefault();
    }
}

function createEventForm() {
    let $eventListing = $('<div>').addClass('event-listing');
    let $eventListingTitle = $('<div>').addClass('event-listing-title').text("Розклад на: " + TODAY.format('DD/MM/YYYY'));
    let $eventItems = $('<div>').addClass('event-items');
    $eventListing.append($eventListingTitle).append($eventItems);
    $('div.clndr').append($eventListing);
    createSubjectTeacherContainers($eventItems);
}

function changeEventListingTitle(moment) {
    $('div.event-listing-title').text("Розклад на: " + moment.format('DD/MM/YYYY'));
}

function createSubjectTeacherContainers($eventItems) {
    $subjectsForm = $('<div>').addClass('scheduleSubjectForm');
    $teachersForm = $('<div>').addClass('scheduleTeacherForm');

    $eventItems.append($subjectsForm).append($teachersForm);

    emptySubjectTeacherForm();
    createEmptyEventSelects($subjectsForm, $teachersForm);
}

function createEmptyEventSelects($subjectsForm, $teachersForm) {
    let $subjectsContainer = createSubjectContainer().appendTo($subjectsForm);
    let $teachersContainer = createTeacherContainer().appendTo($teachersForm);

    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let $$liSubject = $('<li>').appendTo($subjectsContainer);
        let $liTeacher = $('<li>').appendTo($teachersContainer);
        let $subjectSelect = createSelect(`subject_${lessonNr}`, 'calendar-select subjectSelect', [], null, '-- Немає --');
        let $teacherSelect = createSelect(`teacher_${lessonNr}`, 'calendar-select teacherSelect', [], null, '-- Немає --');
        $$liSubject.append($subjectSelect);
        $liTeacher.append($teacherSelect);
    }


}

function createSubjectContainer() {
    return $('<ol>').attr({'start': 0, 'style': 'margin-right: -10px'});
}

function createTeacherContainer() {
    return $('<ul>').attr({'type': 'none', 'style': 'margin-left: 3px'});
}

function handleBandSelectChange() {
    $selectedBandId = $(this).val();

    $('.selected').removeClass('selected');

    emptySubjectTeacherForm();

    if ($selectedBandId === '') {
        createEmptyEventSelects($subjectsForm, $teachersForm);
        return;
    }

    getBandScheduleByDate($selectedBandId, TODAY);
}

function emptySubjectTeacherForm() {
    $subjectsForm.empty();
    $teachersForm.empty();
}

function getBandScheduleByDate(selectedBandId, moment) {
    let weekOfYear = moment.week();
    let dayOfWeek = moment.weekday() + 1;

    $.getJSON("/getBandScheduleByDate",
        {bandId: parseInt(selectedBandId), weekOfYear: parseInt(weekOfYear), dayOfWeek: parseInt(dayOfWeek)},
        function (json) {
            currentBandSchedule = json;
            createEventSelects(parseInt(selectedBandId))
        });
}

function createEventSelects(selectedBandId) {
    generateSubjectEventSelects(selectedBandId);
    generateTeacherEventSelects();
}

function generateSubjectEventSelects(selectedBandId) {
    let $subjectContainer = createSubjectContainer().appendTo($subjectsForm);
    let availableSubjects = getSubjectsForBand(bands, subjects, selectedBandId);

    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let currentSubject = function () {
            let currentLesson = getCurrentLesson(lessonNr);

            if (currentLesson) {
                return currentLesson.subject;
            }

            return null;
        }();

        let $li = $('<li>').appendTo($subjectContainer);
        let $subjectSelect = createSelect(`subject_${lessonNr}`, 'calendar-select subjectSelect',
            availableSubjects, currentSubject, '-- Немає --');

        $subjectSelect.on('change', {lessonNr}, handleSubjectSelectChange);

        $li.append($subjectSelect);
    }
}

function generateTeacherEventSelects() {
    let $teacherContainer = $('<ul>').attr({'type': 'none', 'style': 'margin: 0'}).appendTo($teachersForm);

    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let selectedSubjectId = parseInt($(`select[name='subject_${lessonNr}']`).val());
        let availableTeachers = getTeachersForSubject(subjects, teachers, selectedSubjectId);
        let currentTeacher = function () {
            let currentLesson = getCurrentLesson(lessonNr);

            if (currentLesson) {
                return currentLesson.teacher;
            }

            return null;
        }();

        let $li = $('<li>').appendTo($teacherContainer);
        let $teacherSelect = createSelect(`teacher_${lessonNr}`, 'calendar-select teacherSelect', availableTeachers,
            currentTeacher, '-- Немає --');

        $li.append($teacherSelect);
    }
}

function getCurrentLesson(lessonNr) {
    return currentBandSchedule.find(function (item) {
        return item.numberOfLesson === lessonNr;
    });
}

function handleSubjectSelectChange(event) {
    let selectedSubjectId = $(this).val();
    let lessonNr = event.data.lessonNr;
    let teacherSelectName = `teacher_${lessonNr}`;
    $(`select[name=${teacherSelectName}]`).empty().append(createOption('', '-- Немає --', true));

    if (selectedSubjectId === '') {
        return;
    }

    updateTeacherSelect(subjects, teachers, parseInt(selectedSubjectId), teacherSelectName);
}

$(document).ready(init);

/*]]>*/