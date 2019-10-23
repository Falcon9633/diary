/*<![CDATA[*/
const $BAND_EDITING_MODAL = $('#bandEditingModal');
const $BAND_SELECT = $('select[name=bandId]');

var bands;
var $selectedBandId;
var selectedBand;

function init() {
    let $showModalButton = $('#showModalButton');
    let $saveButton = $('#saveButton');

    $BAND_EDITING_MODAL.modal({'show': false});
    getAllBands();

    $BAND_SELECT.on('change', getSelectedBandId);
    $showModalButton.on('click', showModalValidator);
    $saveButton.on('click', handleSaveButton);
}

function getAllBands() {
    $.getJSON("/getAllBands", function (json) {
        bands = json;
    });
}

function getSelectedBandId() {
    $selectedBandId = $(this).val();

    if (!$selectedBandId){
        $BAND_EDITING_MODAL.modal('hide');
        return;
    }

    initModalContent();
}

function showModalValidator() {
    if (!$selectedBandId) {
        let $errorElement = $('#bandSelectError');
        $errorElement.show();
        alert($errorElement.text());
        return;
    }

    $BAND_EDITING_MODAL.modal('show');
}

function initModalContent() {
    if (!$BAND_SELECT){
        return;
    }

    selectedBand = bands.find(function (item) {
        return item.id === parseInt($selectedBandId);
    });

    getBandName(selectedBand);
    getBandStudentSet(selectedBand);
    getBandSubjectSet(selectedBand);
}

function getBandName(selectedBand) {
    $('h2[data-container="band-name"]').text("Назва класу:\xa0\xa0\xa0\xa0" + selectedBand.name + "\xa0\xa0\xa0\xa0--->\xa0\xa0\xa0\xa0");
}

function getBandStudentSet(selectedBand) {
    let $bandStudentSetContainer = $('[data-container="band-studentSet"]');
    $bandStudentSetContainer.empty();

    if (selectedBand.studentsSet.length === 0) {
        $bandStudentSetContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let studentSet of selectedBand.studentsSet) {
        let $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `student_id_${studentSet.id}`,
            'value': studentSet.id,
            'checked': ''
        })
            .addClass('student');
        let $label = $('<label/>')
            .attr('for', $inputCheckbox.attr('id'))
            .addClass('label-right')
            .text(studentSet.name + ' ' + studentSet.surname);
        $bandStudentSetContainer
            .append($inputCheckbox)
            .append($label)
            .append($('<br/>'));
    }
}

function getBandSubjectSet(selectedBand) {
    let $bandSubjectSetContainer = $('[data-container="band-subjectSet"]');
    $bandSubjectSetContainer.empty();

    if (selectedBand.subjectSet.length === 0) {
        $bandSubjectSetContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let subjectSet of selectedBand.subjectSet) {
        let $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `subject_id_${subjectSet.id}`,
            'value': subjectSet.id,
            'checked': ''
        })
            .addClass('subject');
        let $label = $('<label/>').attr('for', $inputCheckbox.attr('id')).addClass('label-right').text(subjectSet.name);
        $bandSubjectSetContainer.append($inputCheckbox);
        $bandSubjectSetContainer.append($label);
        $bandSubjectSetContainer.append($('<br/>'));
    }
}

function handleSaveButton() {
    let editedBand = selectedBand;
    editedBand.name = setBandName(selectedBand);
    editedBand.studentsSet = setBandStudentSet(editedBand);
    editedBand.subjectSet = setBandSubjectSet(editedBand);

    editBand(editedBand);
}

function setBandName() {
    let editedName = $('input[data-container="band-name"]').val();

    if (editedName === '') {
        return selectedBand.name;
    }

    return editedName;
}

function setBandStudentSet(editedBand) {
    let unselectedStudentId = [];
    let $selectedStudentsInput = $('input.student');

    $selectedStudentsInput.each(function () {
        if ($(this).is(':not(:checked)')) {
            unselectedStudentId.push(parseInt($(this).val()));
        }
    });

    return editedBand.studentsSet.filter(function (item) {
        return unselectedStudentId.indexOf(item.id) !== -1;
    })
}

function setBandSubjectSet(editedBand) {
    let selectedSubjectId = [];
    let $selectedSubjectsInput = $('input.subject');

    $selectedSubjectsInput.each(function () {
        if ($(this).is(':checked')) {
            selectedSubjectId.push(parseInt($(this).val()));
        }
    });

    return editedBand.subjectSet.filter(function (item) {
        return selectedSubjectId.indexOf(item.id) !== -1;
    });
}

function editBand(editedBand) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: '/editBand',
        type: 'POST',
        data: JSON.stringify(editedBand),
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            getAllBands();
            $(document).ajaxComplete(function(){
                initModalContent();
            });
        },
        error: function () {
            alert("Сталась помилка");
        }
    })
}

$(document).ready(init);
/*]]>*/