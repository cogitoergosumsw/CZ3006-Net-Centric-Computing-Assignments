<html>
<head>
    <title>Your Receipt</title>
    <link rel="stylesheet" type="text/css" href="styles/semantic-css/semantic.min.css">
    <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css"
          integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
    <link rel="stylesheet" href="styles/style.css">
</head>

<body>
<?php
//    initialize the number of apples, oranges and bananas
$numApples = 0;
$numOranges = 0;
$numBananas = 0;
$totalCost = 0;

//    declaring the output file name
$fileName = "order.txt";

//    check if the form submitted at the frontend is done through POST method
if ($SERVER["REQUEST_METHOD"] == "POST") {

//    fetch the data from the submitted form
    $userName = $_POST['user'];
    $numApples = $_POST['apples-input'];
    $numOranges = $_POST['oranges-input'];
    $numBananas = $_POST['bananas-input'];
    $totalCost = $_POST['total-cost'];

}
?>
<div class="ui hidden divider"></div>
<div class="ui container">
    <h2>Hello <?php echo $_POST['user']; ?>!</h2>
    <div class="ui clearing divider"></div>
    <div class="row">
        <h3>Thank you for your submission.</h3>
        <p>This is your Receipt.</p>
    </div>
    <div class="ui raised segment">
    <table class="ui celled table">
        <thead>
        <tr>
            <th>Number of Apples</th>
            <th>Number of Oranges</th>
            <th>Number of Bananas</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td data-label="apples"><?php echo $_POST['apples-input']; ?></td>
            <td data-label="oranges"><?php echo $_POST['oranges-input']; ?></td>
            <td data-label="bananas"><?php echo $_POST['bananas-input']; ?></td>
        </tr>
        </tbody>
    </table>
    <h3 class="ui">Your preferred mode of payment is <u><?php echo $_POST['payment']?></u>.</h3>
    </div>
</div>


</body>

</html>
