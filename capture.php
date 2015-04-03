<?php
require_once('config.php');
$captureOutput =  exec("cd $SAVE_DIR && gphoto2 --capture-image-and-download", $output);

$capturedFile = NULL;
foreach($output as $line){
	if(substr($line, 0, 14) === "Saving file as") {
		$capturedFile = substr($line , 15);
	}
} 


if($capturedFile != NULL) {
	if(!copy ( $SAVE_DIR.$capturedFile  , $BACKUP_DIR.$capturedFile))
	{
		// Copy failed
	}
} else {
	echo "Capture failed : <br/>";
	print_r($output);
	exit;
}

display($SAVE_DIR.$capturedFile, $capturedFile);

?>
