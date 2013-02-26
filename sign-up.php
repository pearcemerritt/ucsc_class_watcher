<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title></title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">

        <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

        <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/main.css">
        <script src="js/vendor/modernizr-2.6.2.min.js"></script>
    </head>
    <body>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->

<!--=============================================================================-->
<!-- end HTML5 boilerplate starter code =========================================-->
<!--=============================================================================-->

<?php
  // Get the phone number as a 10 character string with just the digits
  // so it can be used to access the correct class_list file.
  $filename = preg_replace('/\D/', '', $_GET["phone"]);
  $filename = 'class_lists/' . $filename . '.class_list';

  echo '<h1 style="color:red;">PHP file output!!!!</h1>';
  echo '<p>This is the filename we\'re about to write to: ' . $filename . '</p>';

  $file = fopen($filename, 'a');

  $num_bytes = fwrite($file, $_GET["name"] . "\n");
  $num_bytes = fwrite($file, $_GET["email"] . "\n");
  $num_bytes = fwrite($file, $_GET["ref"] . "\n");
  
  echo 'number of bytes written: ' . $num_bytes;

  fclose($file);
?>

  <h1>UCSC Class Watcher</h1>

  <p>
Thank you <?php echo $_GET["name"] ?>. <br/>
We will send a confirmation email to you within 72 hours with further instructions. <br/>
<br/>
-- UCSC Class Watcher
  </p>

<!--===============================================================================-->
<!-- start HTML5 boilerplate starter code =========================================-->
<!--===============================================================================-->

        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-1.8.3.min.js"><\/script>')</script>
        <script src="js/plugins.js"></script>
        <script src="js/main.js"></script>

        <!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
        <script>
            var _gaq=[['_setAccount','UA-XXXXX-X'],['_trackPageview']];
            (function(d,t){var g=d.createElement(t),s=d.getElementsByTagName(t)[0];
            g.src=('https:'==location.protocol?'//ssl':'//www')+'.google-analytics.com/ga.js';
            s.parentNode.insertBefore(g,s)}(document,'script'));
        </script>
    </body>
</html>
