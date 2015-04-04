<?php
require_once('config.php');
$fileToPrint = $_GET['picture'];

$pdfName = basename($fileToPrint, ".JPG").".pdf";

exec('convert '.$SAVE_DIR.$fileToPrint.' '.$SAVE_DIR.$pdfName);
//exec('lpr -o PageSize=Custom.10x15cm -o scaling=100 -o fit-to-page -o media=Custom.10x15cm -o position=top'.$SAVE_DIR.$pdfName);
exec('lpr '.$SAVE_DIR.$pdfName);

?>
