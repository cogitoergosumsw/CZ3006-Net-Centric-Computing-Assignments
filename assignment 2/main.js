$('.ui.sticky')
    .sticky({
        context: '#fruits-selection'
    })
;

var applesInput = document.getElementById('apples-input');
var orangesInput = document.getElementById('oranges-input');
var bananasInput = document.getElementById('bananas-input');

1

$(document).ready(function() {
    $('#apples-error').hide();
    $('#oranges-error').hide();
    $('#bananas-error').hide();

    applesInput.addEventListener("click", validateFruitsInput);
});

// function validateFruitsInput() {
//     if
// }