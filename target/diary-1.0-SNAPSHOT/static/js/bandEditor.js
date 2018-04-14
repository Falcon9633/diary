/*<![CDATA[*/

var bands;
var $select = $('select');
var $selectedBandId = '';
var selectedBand;
var $editButton = $('button[data-target]');
var $saveButton = $('div.modal-footer>:not(button[data-dismiss])');

function init() {
    getAllBands();
    $select.on('change', getSelectedBand);
    $editButton.on('click', handleEditButton);
    $saveButton.on('click', handleSaveButton);
}

function getAllBands() {
    $.getJSON("/getAllBands", function (json) {
        bands = json;
    });
}

function getSelectedBand() {
    $selectedBandId = $(this).val();
}

function handleEditButton() {
    if ($select.val() === '') {
        return;
    }
    initModalContent();
}

function initModalContent() {
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
    var $bandStudentListContainer = $('[data-container="band-studentList"]');
    $bandStudentListContainer.empty();

    if (selectedBand.studentsList.length === 0) {
        $bandStudentListContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let studentList of selectedBand.studentsList) {
        var $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `student_id_${studentList.id}`,
            'value': studentList.id,
            'checked': ''
        })
            .addClass('student');
        var $label = $('<label/>').attr('for', $inputCheckbox.attr('id')).addClass('label-right').text(studentList.name + ' ' + studentList.surname);
        $bandStudentListContainer.append($inputCheckbox);
        $bandStudentListContainer.append($label);
        $bandStudentListContainer.append($('<br/>'));
    }
}

function getBandSubjectList(selectedBand) {
    var $bandSubjectListContainer = $('[data-container="band-subjectList"]');
    $bandSubjectListContainer.empty();

    if (selectedBand.subjectList.length === 0) {
        $bandSubjectListContainer.append($('<label/>').text('Немає'));
        return;
    }

    for (let subjectList of selectedBand.subjectList) {
        var $inputCheckbox = $('<input/>').attr({
            'type': 'checkbox',
            'id': `subject_id_${subjectList.id}`,
            'value': subjectList.id,
            'checked': ''
        })
            .addClass('subject');
        var $label = $('<label/>').attr('for', $inputCheckbox.attr('id')).addClass('label-right').text(subjectList.name);
        $bandSubjectListContainer.append($inputCheckbox);
        $bandSubjectListContainer.append($label);
        $bandSubjectListContainer.append($('<br/>'));
    }
}

function handleSaveButton() {
    var editedBand = selectedBand;
    editedBand.name = setBandName(selectedBand);
    editedBand.studentsList = setBandStudentList(editedBand);
    editedBand.subjectList = setBandSubjectList(editedBand);

    editBand(editedBand);
    console.log(editedBand);
}

function setBandName() {
    var editedName = $('input[data-container="band-name"]').val();

    if (editedName === '') {
        return selectedBand.name;
    }

    return editedName;
}

function setBandStudentList(editedBand) {
    var selectedStudentId = [];
    var $selectedStudentsInput = $('input.student');

    $selectedStudentsInput.each(function () {
        if ($(this).is(':checked')) {
            selectedStudentId.push(parseInt($(this).val()));
        }
    });

    return editedBand.studentsList.filter(function (item) {
        return selectedStudentId.indexOf(item.id) !== -1;
    })
}

function setBandSubjectList(editedBand) {
    var selectedSubjectId = [];
    var $selectedSubjectsInput = $('input.subject');

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
            $(document).ajaxComplete(initModalContent());
        },
        error: function () {
            alert("Сталась помилка");
        }
    })
}

$(document).ready(init());
/*]]>*/