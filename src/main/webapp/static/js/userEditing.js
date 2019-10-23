const USER_EDITING_MODAL = $('#userEditingModal');
const USER_ID = $('#userId');
const CURRENT_SURNAME = $('#currentSurname');
const CURRENT_NAME = $('#currentName');
const CURRENT_EMAIL = $('#currentEmail');
const CURRENT_ROLE_IS_ADMIN = $('#isAdmin');

function init() {
    USER_EDITING_MODAL.modal({'show' : false});

    $('#dataTable tbody').on('click', 'tr', showModal);
}

function showModal() {
    USER_EDITING_MODAL.modal('show');

    let currentRow = $(this).closest('tr');
    let id = currentRow.find('td:eq(0)').text();
    let surname = currentRow.find('td:eq(2)').text();
    let name = currentRow.find('td:eq(3)').text();
    let email = currentRow.find('td:eq(4)').text();
    let isAdmin = currentRow.find('td:eq(6) :checkbox').prop('checked');

    initModalContent(id, surname, name, email, isAdmin);
}

function initModalContent(id, surname, name, email, isAdmin) {
    USER_ID.val(id);
    CURRENT_SURNAME.text("Прізвище :\xa0\xa0\xa0\xa0" + surname + "\xa0\xa0\xa0\xa0--->\xa0\xa0\xa0\xa0");
    CURRENT_NAME.text("Ім’я :\xa0\xa0\xa0\xa0" + name + "\xa0\xa0\xa0\xa0--->\xa0\xa0\xa0\xa0");
    CURRENT_EMAIL.text("Емеіл :\xa0\xa0\xa0\xa0" + email + "\xa0\xa0\xa0\xa0--->\xa0\xa0\xa0\xa0");
    CURRENT_ROLE_IS_ADMIN.prop('checked', isAdmin);
}

$(document).ready(init);