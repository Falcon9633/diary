const JOURNAL_EDITING_MODAL = $('#journalEditingModal');
const SELECTED_MONTH_INDEX = $('input[name=selectedMonthIndex]').val();
const MAIN_TABLE = $('#journalMainTable');
const MAIN_TABLE_HEADER = MAIN_TABLE.find('thead th');
const MAIN_TABLE_BODY = MAIN_TABLE.find('tbody tr');

let selectedMonthLessons;

function getSelectedMonthLessons() {
    $.getJSON("/teacher/getSelectedMonthLessons", {selectedMonthIndex: parseInt(SELECTED_MONTH_INDEX)}, function (json) {
            selectedMonthLessons = json;
        }
    )
}


function init() {
    JOURNAL_EDITING_MODAL.modal({'show': false});
    getSelectedMonthLessons();

    MAIN_TABLE.find('tbody tr').each(function () {
        $(this).children().slice(2).on('click', showModal);
    })
}

function showModal() {
    JOURNAL_EDITING_MODAL.modal('show');

    let colIndex = $(this).index();
    let dayOfMonth = MAIN_TABLE_HEADER.eq(colIndex).find('div').eq(0).text();
    let lessonId = MAIN_TABLE_HEADER.eq(colIndex).find('input').val();
    let notesId = MAIN_TABLE_BODY.map(function () {
        return $(this).children().eq(colIndex).find('input').val();
    });
    let marks = MAIN_TABLE_BODY.map(function () {
        return $(this).children().eq(colIndex).text().trim();
    });

    console.log(marks);

    initModalContent(dayOfMonth, lessonId, notesId, marks);
}

function initModalContent(dayOfMonth, lessonId, notesId, marks) {
    setModalHeader(dayOfMonth);
    setLessonFields(parseInt(lessonId));
    setNoteIdMarkAndAbsent(notesId, marks)
}

function setModalHeader(dayOfMonth) {
    let modalTitle = $(".modal-title");
    modalTitle.text($('.simple-menu a').first().text() + ', ' + dayOfMonth);
}

function setLessonFields(lessonId) {
    let currentLesson = selectedMonthLessons.find(function (element) {
        return element.id === lessonId;
    });

    $('input[name=lessonId]').val(currentLesson.id);
    $('input[name=lessonTheme]').val(currentLesson.theme);
    $('textarea[name=lessonHomework]').val(currentLesson.homework);
}

function setNoteIdMarkAndAbsent(notesId, marks) {
    $('#journalEditingModal').find('tbody tr').each(function (index) {
        let noteIdInput = $(this).children('input').eq(1);
        let studentId = $(this).children('input').eq(0).val();
        noteIdInput.attr('name', 'note-' + studentId);
        noteIdInput.val(notesId[index]);
        let options = $(this).find('option');
        let checkbox = $(this).find(':checkbox');
        let mark = marks[index];

        options.each(function () {
            if ($(this).text() === mark){
                $(this).attr('selected', 'selected');
            } else {
                $(this).removeAttr("selected");
            }
        });

        if (mark === 'H'){
           checkbox.attr('checked', '') ;
        } else {
            checkbox.removeAttr('checked');
        }
    })
}



$(document).ready(init);