<?php
require_once('config.php');

foreach (glob("$SAVE_DIR/*") as $path) { // lists all files in folder called "test"
//foreach (glob("*.php") as $path) { // lists all files with .php extension in current folder
	$docs[$path] = filectime($path);
} arsort($docs); // sort by value, preserving keys

header("Content-Type:text/plain");
foreach ($docs as $path => $timestamp) {
    print basename($path)."\n";
}

?>
