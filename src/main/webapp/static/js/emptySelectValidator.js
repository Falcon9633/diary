/*<![CDATA[*/
/*
Put attr 'type="button"' and 'data-type="submit"' in your submit button
to check all empty selects witch has class 'valid'. Also works with bootstrap modal.
 */

var $button = $('button[data-type]');
var $select = $('select.valid');
var $errorElement = $('label>h3.errors');

function init() {
    if ($button.data('type') === 'submit'){
        $button.attr('type', 'button');
        $button.on('click', handleButtonClick);
        $select.on('change', handleSelectChange);
    }
    if ($button.data('type') === 'button'){
        $button.on('click', handleButtonClick);
        $select.on('change', handleSelectChange);
    }
}

function handleSelectChange() {
    var newValue = $(this).val();

    if ($button.data('type') === 'submit'){
        if (newValue === '') {
            $button.attr('type', 'button');
            return;
        }
        $button.attr('type', 'submit');
    }

    if ($button.data('type') === 'button'){
        if (newValue === ''){
            $button.removeAttr('data-toggle');
            return;
        }
        $button.attr('data-toggle', 'modal');
    }
}

function handleButtonClick() {
    if ($button.attr('type') === 'button' && $select.val() === '') {
        alert($errorElement.text());
        $errorElement.removeClass('hide');
    }
}

$(document).ready(init);

/*]]>*/