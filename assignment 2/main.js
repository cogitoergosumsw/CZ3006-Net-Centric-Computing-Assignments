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

    // initialize error dialog for invalid form submission
    $('.ui.modal')
        .modal()
    ;

    // hide the error messages of invalid input upon loading of website
    $('#apples-error').hide();
    $('#oranges-error').hide();
    $('#bananas-error').hide();

    // add an event listener to the Apples input
    // to check for any input from user
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

    // add an event listener to the Oranges input
    // to check for any input from user
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

    // add an event listener to the Bananas input
    // to check for any input from user
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

    // add an event listener to the Total Cost text input field
    // blur the Total Cost text input field whenever it got focused
    totalCost.addEventListener("focus", function (evt) {
        console.log("total cost got focused, gonna blur!");
        totalCost.blur();
    });

});

// callback function to validate the form data
const form = document.querySelector('form');
form.addEventListener('submit', event => {
    // check if the form has invalid input from the value of Total Cost element
    if (totalCost.value == "NaN") {
        // validate the form submission
        console.log('Form submission cancelled.');
        event.preventDefault();
        $('.modal')
            .modal('show')
        ;
    }

});

// function to close error dialog modal
function closeModal() {
    $('.modal')
        .modal('hide')
    ;
}
