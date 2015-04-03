<?php
$SAVE_DIR='/home/fred/photomaton/';
$BACKUP_DIR='/home/fred/store/photomaton/';

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

$fp = fopen($SAVE_DIR.$capturedFile, 'rb');

// send the right headers
header("Content-Type: image/jpeg");
header("Content-Length: " . filesize($SAVE_DIR.$capturedFile));
header('Content-Disposition: attachment; filename="'.$capturedFile.'"');
// dump the picture and stop the script
fpassthru($fp);
exit;

?>
