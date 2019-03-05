$('.ui.sticky')
    .sticky({
        context: '#fruits-selection'
    })
;

var applesInput = document.getElementById('apples-input');
var orangesInput = document.getElementById('oranges-input');
var bananasInput = document.getElementById('bananas-input');

var appleCost = 0.69;
var orangeCost = 0.59;
var bananaCost = 0.39;

var applesError = document.getElementById('apples-error');
var orangesError = document.getElementById('oranges-error');
var bananasError = document.getElementById('bananas-error');

var totalCost = document.getElementById('total-cost');

var total = 0;

$(document).ready(function () {
    $('#apples-error').hide();
    $('#oranges-error').hide();
    $('#bananas-error').hide();

    // $('total-cost').contentEditable = "false";
    totalCost.contentEditable = "false";

    applesInput.addEventListener("input", function (evt) {
        if (isNaN(this.value)) {
            $('#apples-error').show();
        } else {
            total = total + (this.value) * appleCost;
            $('#apples-error').hide();
        }
    });
    orangesInput.addEventListener("input", function (evt) {
        if (isNaN(this.value)) {
            $('#oranges-error').show();
        } else {
            total = total + (this.value) * orangeCost;
            $('#oranges-error').hide();
        }
    });
    bananasInput.addEventListener("input", function (evt) {
        if (isNaN(this.value)) {
            $('#bananas-error').show();
        } else {
            total = total + (this.value) * bananaCost;
            $('#bananas-error').hide();
        }
    });

    totalCost.addEventListener("focus", function (evt) {
        document.getElementById("total-cost").blur();
    });
});