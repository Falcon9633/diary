/*<![CDATA[*/
const $SUBJECT_EDITING_MODAL = $('#subjectEditingModal');
const $SUBJECT_SELECT = $('select[name=subjectId]');

var subjects;
var $selectedSubjectId;
var selectedSubject;

function init() {
    let $showModalButton = $('#showModalButton');
    let $saveButton = $('#saveButton');

    $SUBJECT_EDITING_MODAL.modal({'show': false});
    getAllSubjects();

    $SUBJECT_SELECT.on('change', getSelectedSubjectId);
    $showModalButton.on('click', showModalValidator);
    $saveButton.on('click', handleSaveButton);
}

function getAllSubjects() {
    $.getJSON("/getAllSubjects", function (json) {
        subjects = json;
    });
}

function getSelectedSubjectId() {
    $selectedSubjectId = $(this).val();

    if (!$selectedSubjectId){
        $SUBJECT_EDITING_MODAL.modal('hide');
        return;
    }

    initModalContent();
}

function showModalValidator() {
    if (!$selectedSubjectId) {
        let $errorElement = $('#subjectSelectError');
        $errorElement.show();
        alert($errorElement.text());
        return;
    }

    $SUBJECT_EDITING_MODAL.modal('show');
}

function initModalContent() {
    if (!$SUBJECT_SELECT){
        return;
    }

    selectedSubject = subjects.find(function (item) {
        return item.id === parseInt($selectedSubjectId);
    });

    getSubjectName(selectedSubject);
    getSubjectBandSet(selectedSubject);
    getSubjectTeacherSet(selectedSubject);
}

function getSubjectName(selectedSubject) {
    $('h2[data-container="subject-name"]').text("Назва предмету:\xa0\xa0\xa0\xa0" + selectedSubject.name + "\xa0\xa0\xa0\xa0--->\xa0\xa0\xa0\xa0");
}

function getSubjectBandSet(selectedSubject) {
    var $subjectBandSetContainer = $('[data-container="subject-bandSet"]');
    $subjectBandSetContainer.empty();

    if (selectedSubject.bandSet.length === 0) {
        $subjectBandSetContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let bandSet of selectedSubject.bandSet) {
        var $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `band_id_${bandSet.id}`,
            'value': bandSet.id,
            'checked': ''
        })
            .addClass('band');
        var $label = $('<label/>')
            .attr('for', $inputCheckbox.attr('id'))
            .addClass('label-right')
            .text(bandSet.name);
        $subjectBandSetContainer
            .append($inputCheckbox)
            .append($label)
            .append($('<br/>'));
    }
}

function getSubjectTeacherSet(selectedSubject) {
    var $subjectTeacherSetContainer = $('[data-container="subject-teacherSet"]');
    $subjectTeacherSetContainer.empty();

    if (selectedSubject.teacherSet.length === 0) {
        $subjectTeacherSetContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let teacherSet of selectedSubject.teacherSet) {
        var $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `teacher_id_${teacherSet.id}`,
            'value': teacherSet.id,
            'checked': ''
        })
            .addClass('teacher');
        var $label = $('<label/>')
            .attr('for', $inputCheckbox.attr('id'))
            .addClass('label-right')
            .text(teacherSet.name + ' ' + teacherSet.surname);
        $subjectTeacherSetContainer
            .append($inputCheckbox)
            .append($label)
            .append($('<br/>'));
    }
}

function handleSaveButton() {
    var editedSubject = selectedSubject;
    editedSubject.name = setSubjectName(selectedSubject);
    editedSubject.bandSet = setSubjectBandSet(editedSubject);
    editedSubject.teacherSet = setSubjectTeacherSet(editedSubject);

    editSubject(editedSubject);
}

function setSubjectName() {
    var editedName = $('input[data-container="subject-name"]').val();

    if (editedName === '') {
        return selectedSubject.name;
    }

    return editedName;
}

function setSubjectBandSet(editedSubject) {
    var selectedBandId = [];
    var $selectedBandInput = $('input.band');

    $selectedBandInput.each(function () {
        if ($(this).is(':checked')) {
            selectedBandId.push(parseInt($(this).val()));
        }
    });

    return editedSubject.bandSet.filter(function (item) {
        return selectedBandId.indexOf(item.id) !== -1;
    })
}

function setSubjectTeacherSet(editedSubject) {
    var selectedTeacherId = [];
    var $selectedTeacherInput = $('input.teacher');

    $selectedTeacherInput.each(function () {
        if ($(this).is(':checked')) {
            selectedTeacherId.push(parseInt($(this).val()));
        }
    });

    return editedSubject.teacherSet.filter(function (item) {
        return selectedTeacherId.indexOf(item.id) !== -1;
    })
}

function editSubject(editedSubject) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    console.log(editedSubject);
    $.ajax({
        url: '/editSubject',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(editedSubject),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            getAllSubjects();
            $(document).ajaxComplete(function(){
                initModalContent()
            });
        },
        error: function (e) {
            console.log(e);
            alert("Сталась помилка");
        }
    })
}

$(document).ready(init);
/*]]>*/