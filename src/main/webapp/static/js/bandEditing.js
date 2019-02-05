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
    getBandStudentList(selectedBand);
    getBandSubjectList(selectedBand);
}

function getBandName(selectedBand) {
    $('h2[data-container="band-name"]').text("Назва класу:\xa0\xa0\xa0\xa0" + selectedBand.name + "\xa0\xa0\xa0\xa0--->\xa0\xa0\xa0\xa0");
}

function getBandStudentList(selectedBand) {
    let $bandStudentListContainer = $('[data-container="band-studentList"]');
    $bandStudentListContainer.empty();

    if (selectedBand.studentsList.length === 0) {
        $bandStudentListContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let studentList of selectedBand.studentsList) {
        let $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `student_id_${studentList.id}`,
            'value': studentList.id,
            'checked': ''
        })
            .addClass('student');
        let $label = $('<label/>')
            .attr('for', $inputCheckbox.attr('id'))
            .addClass('label-right')
            .text(studentList.name + ' ' + studentList.surname);
        $bandStudentListContainer
            .append($inputCheckbox)
            .append($label)
            .append($('<br/>'));
    }
}

function getBandSubjectList(selectedBand) {
    let $bandSubjectListContainer = $('[data-container="band-subjectList"]');
    $bandSubjectListContainer.empty();

    if (selectedBand.subjectList.length === 0) {
        $bandSubjectListContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let subjectList of selectedBand.subjectList) {
        let $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `subject_id_${subjectList.id}`,
            'value': subjectList.id,
            'checked': ''
        })
            .addClass('subject');
        let $label = $('<label/>').attr('for', $inputCheckbox.attr('id')).addClass('label-right').text(subjectList.name);
        $bandSubjectListContainer.append($inputCheckbox);
        $bandSubjectListContainer.append($label);
        $bandSubjectListContainer.append($('<br/>'));
    }
}

function handleSaveButton() {
    let editedBand = selectedBand;
    editedBand.name = setBandName(selectedBand);
    editedBand.studentsList = setBandStudentList(editedBand);
    editedBand.subjectList = setBandSubjectList(editedBand);

    editBand(editedBand);
}

function setBandName() {
    let editedName = $('input[data-container="band-name"]').val();

    if (editedName === '') {
        return selectedBand.name;
    }

    return editedName;
}

function setBandStudentList(editedBand) {
    let unselectedStudentId = [];
    let $selectedStudentsInput = $('input.student');

    $selectedStudentsInput.each(function () {
        if ($(this).is(':not(:checked)')) {
            unselectedStudentId.push(parseInt($(this).val()));
        }
    });

    return editedBand.studentsList.filter(function (item) {
        return unselectedStudentId.indexOf(item.id) !== -1;
    })
}

function setBandSubjectList(editedBand) {
    let selectedSubjectId = [];
    let $selectedSubjectsInput = $('input.subject');

    $selectedSubjectsInput.each(function () {
        if ($(this).is(':checked')) {
            selectedSubjectId.push(parseInt($(this).val()));
        }
    });

    return editedBand.subjectList.filter(function (item) {
        return selectedSubjectId.indexOf(item.id) !== -1;
    });
}

function editBand(editedBand) {
    $.ajax({
        url: '/editBand',
        type: 'POST',
        data: JSON.stringify(editedBand),
        contentType: 'application/json',
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