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
    // open file on server
    // throw exception when there's an error opening the file
    $file = fopen("order.txt", "c+") or die ("Error opening file!")
?>
<div class="ui hidden divider"></div>
<div class="ui container">
    <h2>Hello <?php echo $_POST['user']; ?>!</h2>
    <div class="ui clearing divider"></div>
    <div class="row">
        <h3>Thank you for your submission.</h3>
    </div>
    <div class="ui hidden divider"></div>
    <div class="ui placeholder raised segment">
        <h2 class="ui center aligned header">Receipt</h2>
        <a class="ui green right corner label">
            <i class="check icon"></i>
        </a>
        <p><strong>Customer's Name</strong>: <?php echo $_POST['user']; ?></p>
        <h4>Summary</h4>
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
        <div class="ui statistic">
            <div class="label">
                Total Cost
            </div>
            <div class="value">
                $<?php
                $total_cost = 0;

                //listing the cost of each fruit
                $apple_cost = 0.69;
                $orange_cost = 0.59;
                $banana_cost = 0.39;

                //computing total cost from number of fruits
                $total_cost =
                    $apple_cost * $_POST['apples-input'] +
                    $orange_cost * $_POST['oranges-input'] +
                    $banana_cost * $_POST['bananas-input'];
                echo ($total_cost);
                ?>
            </div>
        </div>
        <h3 class="ui">Your preferred mode of payment is <u><?php echo $_POST['payment'] ?></u>.</h3>
    </div>
</div>


</body>

</html>
