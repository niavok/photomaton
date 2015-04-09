<?php
$SAVE_DIR='/home/fred/photomaton/';
$BACKUP_DIR='/media/store/photomaton/';



function display($path, $name)
{
	$fp = fopen($path, 'rb');

	// send the right headers
	header("Content-Type: image/jpeg");
	header("Content-Length: " . filesize($path));
	header('Content-Disposition: attachment; filename="'.$name.'"');
	// dump the picture and stop the script
	fpassthru($fp);
	exit;
}

?>
