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

    applesInput.addEventListener("input", function (evt) {
        if (isNaN(this.value)) {
            $('#apples-error').show();
            totalCost.value = "NaN";
        } else {
            total = ((this.value) * appleCost) + (orangesInput.value * orangeCost) + (bananasInput.value * bananaCost);
            total = parseFloat(total).toFixed(2);
            if (!isNaN(orangesInput.value) && !isNaN(bananasInput.value)) {
                totalCost.value = total;
            }
            $('#apples-error').hide();
        }
    });
    orangesInput.addEventListener("input", function (evt) {
        if (isNaN(this.value)) {
            totalCost.value = "NaN";
            $('#oranges-error').show();
        } else {
            total = ((this.value) * orangeCost) + (applesInput.value * appleCost) + (bananasInput.value * bananaCost);
            total = parseFloat(total).toFixed(2);
            if (!isNaN(applesInput.value) && !isNaN(bananasInput.value)) {
                totalCost.value = total;
            }
            $('#oranges-error').hide();
        }
    });
    bananasInput.addEventListener("input", function (evt) {
        if (isNaN(this.value)) {
            totalCost.value = "NaN";
            $('#bananas-error').show();
        } else {
            total = ((this.value) * bananaCost) + (orangesInput.value * orangeCost) + (applesInput.value * appleCost);
            total = parseFloat(total).toFixed(2);
            if (!isNaN(orangesInput.value) && !isNaN(applesInput.value)) {
                totalCost.value = total;
                console.log("total:" + total);
            }
            $('#bananas-error').hide();
        }
    });

    // blur the Total Cost text input field whenever it got focused
    totalCost.addEventListener("focus", function (evt) {
        console.log("total cost got focused, gonna blur!");
        totalCost.blur();
    });

});

function setTwoNumberDecimal(event) {
    this.value = parseFloat(this.value).toFixed(2);
}