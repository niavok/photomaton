<?php
require_once('config.php');
$fileToDisplay = $_GET['picture'];

display($SAVE_DIR.$fileToDisplay, $fileToDisplay);
?>
