/*<![CDATA[*/

var subjects;
var $select = $('select');
var $selectedSubjectId = '';
var selectedSubject;
var $editButton = $('button[data-target]');
var $saveButton = $('div.modal-footer>:not(button[data-dismiss])');

function init() {
    getAllSubjects();
    $select.on('change', getSelectedSubjectId);
    $editButton.on('click', handleEditButton);
    $saveButton.on('click', handleSaveButton);
}

function getAllSubjects() {
    $.getJSON("/getAllSubjects", function (json) {
        subjects = json;
    });
}

function getSelectedSubjectId() {
    $selectedSubjectId = $(this).val();
}

function handleEditButton() {
    if ($select.val() === '') {
        return;
    }
    initModalContent();
}

function initModalContent() {
    selectedSubject = subjects.find(function (item) {
        return item.id === parseInt($selectedSubjectId);
    });

    getSubjectName(selectedSubject);
    getSubjectBandList(selectedSubject);
    getSubjectTeacherList(selectedSubject);
}

function getSubjectName(selectedSubject) {
    $('h2[data-container="subject-name"]').text("Назва класу:\xa0\xa0\xa0\xa0" + selectedSubject.name + "\xa0\xa0\xa0\xa0--->\xa0\xa0\xa0\xa0");
}

function getSubjectBandList(selectedSubject) {
    var $subjectBandListContainer = $('[data-container="subject-bandList"]');
    $subjectBandListContainer.empty();

    if (selectedSubject.bandList.length === 0) {
        $subjectBandListContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let bandList of selectedSubject.bandList) {
        var $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `band_id_${bandList.id}`,
            'value': bandList.id,
            'checked': ''
        })
            .addClass('band');
        var $label = $('<label/>')
            .attr('for', $inputCheckbox.attr('id'))
            .addClass('label-right')
            .text(bandList.name);
        $subjectBandListContainer
            .append($inputCheckbox)
            .append($label)
            .append($('<br/>'));
    }
}

function getSubjectTeacherList(selectedSubject) {
    var $subjectTeacherListContainer = $('[data-container="subject-teacherList"]');
    $subjectTeacherListContainer.empty();

    if (selectedSubject.teacherList.length === 0) {
        $subjectTeacherListContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let teacherList of selectedSubject.teacherList) {
        var $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `teacher_id_${teacherList.id}`,
            'value': teacherList.id,
            'checked': ''
        })
            .addClass('teacher');
        var $label = $('<label/>')
            .attr('for', $inputCheckbox.attr('id'))
            .addClass('label-right')
            .text(teacherList.name + ' ' + teacherList.surname);
        $subjectTeacherListContainer
            .append($inputCheckbox)
            .append($label)
            .append($('<br/>'));
    }
}

function handleSaveButton() {
    var editedSubject = selectedSubject;
    editedSubject.name = setSubjectName(selectedSubject);
    editedSubject.bandList = setSubjectBandList(editedSubject);
    editedSubject.teacherList = setSubjectTeacherList(editedSubject);

    editSubject(editedSubject);
}

function setSubjectName() {
    var editedName = $('input[data-container="subject-name"]').val();

    if (editedName === '') {
        return selectedSubject.name;
    }

    return editedName;
}

function setSubjectBandList(editedSubject) {
    var selectedBandId = [];
    var $selectedBandInput = $('input.band');

    $selectedBandInput.each(function () {
        if ($(this).is(':checked')) {
            selectedBandId.push(parseInt($(this).val()));
        }
    });

    return editedSubject.bandList.filter(function (item) {
        return selectedBandId.indexOf(item.id) !== -1;
    })
}

function setSubjectTeacherList(editedSubject) {
    var selectedTeacherId = [];
    var $selectedTeacherInput = $('input.teacher');

    $selectedTeacherInput.each(function () {
        if ($(this).is(':checked')) {
            selectedTeacherId.push(parseInt($(this).val()));
        }
    });

    return editedSubject.teacherList.filter(function (item) {
        return selectedTeacherId.indexOf(item.id) !== -1;
    })
}

function editSubject(editedSubject) {
    $.ajax({
        url: '/editSubject',
        type: 'POST',
        data: JSON.stringify(editedSubject),
        contentType: 'application/json',
        success: function () {
            getAllSubjects();
            $(document).ajaxComplete(function(){
                initModalContent()
            });
        },
        error: function () {
            alert("Сталась помилка");
        }
    })
}

$(document).ready(init());
/*]]>*/