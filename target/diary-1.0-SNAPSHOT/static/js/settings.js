const $PASSWORD = $('#password');
const $NEW_PASSWORD = $('#newPassword');
const $MATCH_NEW_PASSWORD = $('#matchNewPassword');
const $PASSWORD_ERROR = $('#passwordError');
const $NEW_PASSWORD_ERROR = $('#newPasswordError');
const $SUBMIT_BUTTON = $('#submit');

function checkOldPassword() {
    $.getJSON("/student/passwordCheck", {password: $PASSWORD.val()}, function (json) {
        if (!json) {
            $PASSWORD_ERROR.show();
        } else {
            matchNewPasswords();
        }
    })
}

function matchNewPasswords() {
    if ($NEW_PASSWORD.val() !== $MATCH_NEW_PASSWORD.val()) {
        $NEW_PASSWORD_ERROR.show();
    } else {
        changePassword();
    }
}

function changePassword(url) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: '/student/changePassword',
        type: 'POST',
        data: {password: $NEW_PASSWORD.val()},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            location.reload();
        },
        error: function () {
            alert("Сталась помилка");
        }
    })
}

$(document).ready(function () {
    $SUBMIT_BUTTON.click(function () {
        checkOldPassword();
    });
});