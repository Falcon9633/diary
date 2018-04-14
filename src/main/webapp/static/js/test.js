var bands;


var selectedBand1;
var selectedBand2;

$.getJSON("/getAllBands", function (json) {
    bands = json;
    selectedBand1 = bands.find(function (item) {
        return item.id === 1;
    });

    selectedBand2 = bands.find(function (item) {
        return item.id === 2;
    });
    console.log(selectedBand1.studentsList);
    print();
});

function print () {
    var newSelectedBand1 = selectedBand1;
    selectedBand1.studentsList = selectedBand2.studentsList;
    console.log(selectedBand1.studentsList);
}
