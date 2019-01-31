function compare2Objects(obj1, obj2) {
    if (obj1 && obj2) {
        return obj1.id === obj2.id;
    }
    return false;
}

function createOption(value, text, selected) {
    let $option = $('<option>').attr('value', value).text(text);

    if (selected) {
        $option.attr('selected', 'selected');
    }

    return $option;
}

function createSelect(name, className, options, currentOption, prompt) {
    let $select = $('<select>').attr('name', name).addClass(className);

    $select.append(createOption('', prompt, !currentOption));

    options.map(function (item) {
        if (typeof item.surname !== "undefined") {
            $select.append(createOption(item.id, item.surname + ' ' + item.name, compare2Objects(item, currentOption)));
        } else {
            $select.append(createOption(item.id, item.name, compare2Objects(item, currentOption)));
        }
    });

    return $select;
}

function updateSelect(name, options) {
    let $select = $(`select[name=${name}]`);

    options.map(function (item) {
        if (typeof item.surname !== "undefined") {
            $select.append(createOption(item.id, item.surname + ' ' + item.name, false));
        } else {
            $select.append(createOption(item.id, item.name, false));
        }
    });

    return $select;
}

function getSubjectsForBand(bands, subjects, selectedBandId) {
    let selectedBand = bands.find(function (item) {
        return item.id === selectedBandId;
    });

    if (selectedBand === undefined) {
        return [];
    }

    return subjects.filter(function (item) {
        let selectedBandSubjectId = selectedBand.subjectList.map(function (item) {
            return item.id;
        });
        return selectedBandSubjectId.indexOf(item.id) !== -1;
    })
}

function getTeachersForSubject(subjects, teachers, selectedSubjectId) {
    let selectedSubject = subjects.find(function (item) {
        return item.id === selectedSubjectId;
    });

    if (selectedSubject === undefined) {
        return [];
    }

    return teachers.filter(function (item) {
        let selectedSubjectTeacherId = selectedSubject.teacherList.map(function (item) {
            return item.id;
        });
        return selectedSubjectTeacherId.indexOf(item.id) !== -1;
    })
}

function updateTeacherSelect(subjects, teachers, selectedSubjectId, teacherSelectName) {
    let availableTeachers = getTeachersForSubject(subjects, teachers, selectedSubjectId);
    updateSelect(teacherSelectName, availableTeachers);
}