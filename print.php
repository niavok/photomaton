<?php
require_once('config.php');
$fileToPrint = $_GET['picture'];

$pdfName = basename($fileToPrint, ".JPG").".pdf";

exec('convert '.$SAVE_DIR.$fileToPrint.' '.$SAVE_DIR.$pdfName);
exec('lpr '.$SAVE_DIR.$pdfName);

?>
