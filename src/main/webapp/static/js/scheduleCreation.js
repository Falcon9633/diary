/*<![CDATA[*/
const MIN_LESSON_NR = 0;
const MAX_LESSON_NR = 8;
const MIN_DAY_NR = 2;
const MAX_DAY_NR = 7;

var $mainTable;
var $bandSelect;
var $subjectsForm;
var $teachersForm;
var $selectedBandId;

var bands;
var subjects;
var teachers;
var currentWeekSchedule;

function init() {
    $mainTable = $('div.mobile');
    $bandSelect = $('select[name=bandId]');

    $mainTable.hide();
    $bandSelect.prop('disabled', true);
    loadData();

    $('form').submit(handleSubmitForm);

    $(document).ajaxStop(createSchedule);
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

    $.getJSON("/getAllScheduleCurrentWeek", function (json) {
        currentWeekSchedule = json;
    });
}

function handleSubmitForm(event){
    if (!$selectedBandId){
        let $errorElement = $('#bandSelectError');
        $errorElement.show();
        alert($errorElement.text());
        event.preventDefault();
    }
}

function createSchedule() {
    $bandSelect.prop('disabled', false);
    createSubjectTeacherForm();
    $bandSelect.on('change', handleBandSelectChange);
}

function createSubjectTeacherForm() {
    let $scheduleMainTable = $('tbody>tr>td');
    let $scheduleSubjectForm = $('<div>').addClass('scheduleSubjectForm');
    let $scheduleTeacherForm = $('<div>').addClass('scheduleTeacherForm');

    $scheduleMainTable.append($scheduleSubjectForm).append($scheduleTeacherForm);
}

function emptySubjectTeacherForm() {
    $('div.scheduleSubjectForm').empty();
    $('div.scheduleTeacherForm').empty();
}

function handleBandSelectChange() {
    $selectedBandId = $(this).val();

    if ($selectedBandId === '') {
        $mainTable.hide();
        return;
    }

    emptySubjectTeacherForm();
    $mainTable.show();
    createSubjectsTeachersSelects(parseInt($selectedBandId));
}

function handleSubjectSelectChange(event) {
    let selectedSubjectId = $(this).val();
    let dayNr = event.data.dayNr;
    let lessonNr = event.data.lessonNr;
    let teacherSelectName = `teacher_${dayNr}_${lessonNr}`;
    $(`select[name=${teacherSelectName}]`).empty().append(createOption('', '-- Оберіть вчителя --', true));

    if (selectedSubjectId === '') {
        return;
    }

    updateTeacherSelect(subjects, teachers, parseInt(selectedSubjectId), teacherSelectName);
}

function createSubjectsTeachersSelects(selectedBandId) {
    $subjectsForm = $('div.scheduleSubjectForm');
    $teachersForm = $('div.scheduleTeacherForm');
    for (let dayNr = MIN_DAY_NR; dayNr <= MAX_DAY_NR; dayNr++) {
        generateSubjectForm($subjectsForm[dayNr - MIN_DAY_NR], selectedBandId, dayNr);
        generateTeacherForm($teachersForm[dayNr - MIN_DAY_NR], selectedBandId, dayNr);
    }
}

function generateSubjectForm($subjectsForm, selectedBandId, dayNr) {
    let $subjectsDayContainer = $('<ol>').attr('start', 0).appendTo($subjectsForm);
    let availableSubjects = getSubjectsForBand(bands, subjects, selectedBandId);

    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let currentSubject = function(){
            let currentLesson = getCurrentLessonSchedule(dayNr, lessonNr, selectedBandId);

            if (currentLesson){
                return currentLesson.subject;
            }

            return null;
        }();

        let $li = $('<li>').appendTo($subjectsDayContainer);
        let $subjectSelect = createSelect(`subject_${dayNr}_${lessonNr}`, 'subjectSelect', availableSubjects, currentSubject,
            '-- Оберіть предмет --');

        $subjectSelect.on('change', {dayNr, lessonNr}, handleSubjectSelectChange);

        $li.append($subjectSelect);
    }
}

function generateTeacherForm($teachersForm, selectedBandId, dayNr) {
    let $teacherDayContainer = $('<ul>').attr({'type': 'none', 'style': 'margin: 0'}).appendTo($teachersForm);

    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let selectedSubjectId = parseInt($(`select[name='subject_${dayNr}_${lessonNr}']`).val());
        let availableTeachers = getTeachersForSubject(subjects, teachers, selectedSubjectId);
        let currentTeacher = function(){
            let currentLesson = getCurrentLessonSchedule(dayNr, lessonNr, selectedBandId);

            if (currentLesson){
                return currentLesson.teacher;
            }

            return null;
        }();

        let $li = $('<li>').appendTo($teacherDayContainer);
        let $teacherSelect = createSelect(`teacher_${dayNr}_${lessonNr}`, 'teacherSelect', availableTeachers, currentTeacher, '-- Оберіть вчителя --');

        $li.append($teacherSelect);
    }
}

function getCurrentLessonSchedule(dayNr, lessonNr, selectedBandId) {
    return currentWeekSchedule.find(function (item) {
        return item.dayOfWeek === dayNr && item.numberOfLesson === lessonNr && item.band.id === selectedBandId;
    });
}

$(document).ready(init);


/*]]>*/