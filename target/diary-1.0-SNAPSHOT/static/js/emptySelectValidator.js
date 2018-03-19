var $button = $('button');
var $select = $('select');

function init() {
    $button.attr('type', 'button');
    $('label>h3.errors').hide();
    $button.on('click', handleButtonClick);
    $select.on('change', handleSelectChange);
}

function handleSelectChange() {
    var newValue = $(this).val();

    if (newValue === ''){
        return;
    }

    $('label>h3.errors').hide();
    $button.attr('type', 'submit');
}

function handleButtonClick() {
    if ($button.attr('type') === 'button'){
        alert($('label>h3.errors').text());
        $('label>h3.errors').show();
    }
    return;
}

init();