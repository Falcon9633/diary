/*<![CDATA[*/
const MIN_LESSON_NR = 0;
const MAX_LESSON_NR = 8;
const MIN_DAY_NR = 2;
const MAX_DAY_NR = 7;

var $mainTable = $('div.mobile');
var $bandSelect = $('select[name=idBand]');
var $subjectsForm;
var $teachersForm;

var bands;
var subjects;
var teachers;
var currentWeekSchedule;

function init() {
    let count = 0;
    const INIT_VAR_COUNT = 4;

    $mainTable.hide();

    $.getJSON("/getAllBands", function (json) {
        bands = json;
        count++;
        mayBeRunApp();
    });

    $.getJSON("/getAllSubjects", function (json) {
        subjects = json;
        count++;
        mayBeRunApp();
    });

    $.getJSON("/getAllTeachers", function (json) {
        teachers = json;
        count++;
        mayBeRunApp();
    });

    $.getJSON("/getAllScheduleCurrentWeek", function (json) {
        currentWeekSchedule = json;
        count++;
        mayBeRunApp();
    });

    function mayBeRunApp() {
        if (count === INIT_VAR_COUNT) {
            createSchedule();
        }
    }
}

function createSchedule() {
    createSubjectTeacherForm();
    $bandSelect.on('change', handleBandSelectChange);
}

function createOption(value, text, selected) {
    let $option = $('<option>').attr('value', value).text(text);

    if (selected) {
        $option.attr('selected', 'selected');
    }

    return $option;
}

function createSelect(name, className, options, currentOption, prompt) {
    let $select = $('<select>').attr('name', name).addClass(className);

    $select.append(createOption('', prompt, !currentOption));

    options.map(function (item) {
        if (typeof item.surname !== "undefined") {
            $select.append(createOption(item.id, item.surname + ' ' + item.name, compare2Objects(item, currentOption)));
        } else {
            $select.append(createOption(item.id, item.name, compare2Objects(item, currentOption)));
        }
    });

    return $select;
}

function updateSelect(name, options) {
    let $select = $(`select[name=${name}]`);

    options.map(function (item) {
        if (typeof item.surname !== "undefined") {
            $select.append(createOption(item.id, item.surname + ' ' + item.name, false));
        } else {
            $select.append(createOption(item.id, item.name, false));
        }
    });

    return $select;
}

function getCurrentLessonSchedule(dayNr, lessonNr) {
    return currentWeekSchedule.find(function (item) {
        return item.dayOfWeek === dayNr && item.numberOfLesson === lessonNr;
    });
}

function compare2Objects(obj1, obj2) {
    if (obj1 && obj2) {
        return obj1.id === obj2.id;
    }
    return false;
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
    let $selectedBandId = $(this).val();

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

    updateTeacherSelect(parseInt(selectedSubjectId), teacherSelectName);
}

function createSubjectsTeachersSelects(selectedBandId) {
    $subjectsForm = $('div.scheduleSubjectForm');
    $teachersForm = $('div.scheduleTeacherForm');
    for (let dayNr = MIN_DAY_NR; dayNr <= MAX_DAY_NR; dayNr++) {
        generateSubjectForm($subjectsForm[dayNr - MIN_DAY_NR], selectedBandId, dayNr);
        generateTeacherForm($teachersForm[dayNr - MIN_DAY_NR], dayNr);
    }
}

function generateSubjectForm($subjectsForm, selectedBandId, dayNr) {
    let $subjectsDayContainer = $('<ol>').attr('start', 0).appendTo($subjectsForm);
    let availableSubjects = getSubjectsForBand(selectedBandId);

    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let currentSubject = function(){
            let currentSchedule = getCurrentLessonSchedule(dayNr, lessonNr);
            if (currentSchedule){
                return currentSchedule.subject;
            }
            return null;
        };

        let $li = $('<li>').appendTo($subjectsDayContainer);
        let $subjectSelect = createSelect(`subject_${dayNr}_${lessonNr}`, 'subjectSelect', availableSubjects, currentSubject,
            '-- Оберіть предмет --');

        $subjectSelect.on('change', {dayNr, lessonNr}, handleSubjectSelectChange);

        $li.append($subjectSelect);
    }
}

function generateTeacherForm($teachersForm, dayNr) {
    let $teacherDayContainer = $('<ul>').attr({'type': 'none', 'style': 'margin: 0'}).appendTo($teachersForm);


    for (let lessonNr = MIN_LESSON_NR; lessonNr <= MAX_LESSON_NR; lessonNr++) {
        let availableTeachers = getTeachersForSubject(parseInt($(`select[name='subject_${dayNr}_${lessonNr}']`).val()));
        let currentTeacher = function(){
            let currentSchedule = getCurrentLessonSchedule(dayNr, lessonNr);
            if (currentSchedule){
                return currentSchedule.teacher;
            }
            return null;
        };
        let $li = $('<li>').appendTo($teacherDayContainer);
        let $teacherSelect = createSelect(`teacher_${dayNr}_${lessonNr}`, 'teacherSelect', availableTeachers, currentTeacher, '-- Оберіть вчителя --');
        $li.append($teacherSelect);
    }
}

function getSubjectsForBand(selectedBandId) {
    let selectedBand = bands.find(function (item) {
        return item.id === selectedBandId;
    });

    if (selectedBand === undefined) {
        return [];
    }

    return subjects.filter(function (item) {
        let selectedBandSubjectId = selectedBand.subjectList.map(function (item) {
            return item.id;
        });
        return selectedBandSubjectId.indexOf(item.id) !== -1;
    })
}

function getTeachersForSubject(selectedSubjectId) {
    let selectedSubject = subjects.find(function (item) {
        return item.id === selectedSubjectId;
    });

    if (selectedSubject === undefined) {
        return [];
    }

    return teachers.filter(function (item) {
        let selectedSubjectTeacherId = selectedSubject.teacherList.map(function (item) {
            return item.id;
        });
        return selectedSubjectTeacherId.indexOf(item.id) !== -1;
    })
}

function updateTeacherSelect(selectedSubjectId, teacherSelectName) {
    let availableTeachers = getTeachersForSubject(selectedSubjectId);
    updateSelect(teacherSelectName, availableTeachers);
}

$(document).ready(init());


/*]]>*/